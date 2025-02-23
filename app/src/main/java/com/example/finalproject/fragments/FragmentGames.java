package com.example.finalproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapters.GameAdapter;
import com.example.finalproject.models.Game;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;

public class FragmentGames extends Fragment {
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private List<Game> gameList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private Spinner filterSpinner;
    private String selectedFilter = "All";

    public FragmentGames() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // קישור לאלמנטים ב-XML
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        filterSpinner = view.findViewById(R.id.filterSpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameAdapter = new GameAdapter(gameList, getContext());
        recyclerView.setAdapter(gameAdapter);

        // טעינת נתונים מ-Firebase
        loadGamesFromFirebase();

        // חיפוש חופשי
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        // סינון לפי קטגוריה ב-Spinner
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilter = parent.getItemAtPosition(position).toString();
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    // פונקציה לטעינת הנתונים מה-Firebase
    private void loadGamesFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("VideoGames");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameList.clear();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if (game != null) {
                        gameList.add(game);
                    }
                }
                gameAdapter.updateList(gameList); // מציגים את הרשימה המקורית
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load games: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // חיפוש לפי שם המשחק
    private void filterList(String text) {
        List<Game> tempList = new ArrayList<>();
        for (Game game : gameList) {
            if (game.getTitle().toLowerCase().contains(text.toLowerCase())) {
                tempList.add(game);
            }
        }
        gameAdapter.updateList(tempList);
    }

    // סינון לפי שנה / מפיצה / ז'אנר
    private void applyFilters() {
        List<Game> tempList = new ArrayList<>();

        for (Game game : gameList) {
            boolean matchesFilter = selectedFilter.equals("All")
                    || (selectedFilter.equals("Year") && game.getReleaseDate().contains("2022"))
                    || (selectedFilter.equals("Publisher") && game.getPublisher().equalsIgnoreCase("Nintendo"))
                    || (selectedFilter.equals("Genre") && game.getGenre().equalsIgnoreCase("Action-Adventure"));

            if (matchesFilter) {
                tempList.add(game);
            }
        }
        gameAdapter.updateList(tempList);
    }
}