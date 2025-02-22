package com.example.finalproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Spinner;
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
    private List<Game> gameList, filteredList;
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

        // קישור ל-XML
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        filterSpinner = view.findViewById(R.id.filterSpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameList = new ArrayList<>();
        filteredList = new ArrayList<>();
        gameAdapter = new GameAdapter(filteredList, getContext());
        recyclerView.setAdapter(gameAdapter);

        // טעינת נתונים מ-Firebase
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
                applyFilters(); // עדכון אחרי טעינת הנתונים
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

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

    // סינון לפי שנת יציאה, מפיצה, ז'אנר
    private void applyFilters() {
        filteredList.clear();

        for (Game game : gameList) {
            if (selectedFilter.equals("All") ||
                    (selectedFilter.equals("Year") && game.getReleaseDate().contains("2022")) ||
                    (selectedFilter.equals("Publisher") && game.getPublisher().equalsIgnoreCase("Nintendo")) ||
                    (selectedFilter.equals("Genre") && game.getGenre().equalsIgnoreCase("Action-Adventure"))) {
                filteredList.add(game);
            }
        }
        gameAdapter.updateList(filteredList);
    }
}
