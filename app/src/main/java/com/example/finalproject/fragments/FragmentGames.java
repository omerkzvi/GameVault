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
    private List<Game> filteredList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private Spinner filterSpinner;
    private String selectedFilter = "All";
    private String searchText = "";

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

        gameAdapter = new GameAdapter(filteredList, getContext()); // משתמשים ב-filteredList
        recyclerView.setAdapter(gameAdapter);

        // טעינת נתונים מ-Firebase
        loadGamesFromFirebase();

        // חיפוש חופשי
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                applyFilters();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                applyFilters();
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
                if (snapshot.exists()) { // בודק אם יש נתונים
                    for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                        Game game = gameSnapshot.getValue(Game.class);
                        if (game != null) {
                            gameList.add(game);
                        }
                    }
                }
                applyFilters(); // עדכון התצוגה לאחר טעינה
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load games: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // מסנן את הרשימה לפי שם המשחק וגם לפי הפילטר
    private void applyFilters() {
        filteredList.clear();

        for (Game game : gameList) {
            String gameYear = extractYear(game.getReleaseDate());

            boolean matchesSearch = searchText.isEmpty() || game.getTitle().toLowerCase().contains(searchText.toLowerCase());
            boolean matchesFilter = selectedFilter.equals("All")
                    || (selectedFilter.equals("Year") && gameYear.equals("2022"))
                    || (selectedFilter.equals("Publisher") && game.getPublisher().equalsIgnoreCase("Nintendo"))
                    || (selectedFilter.equals("Genre") && game.getGenre().equalsIgnoreCase("Action-Adventure"));

            if (matchesSearch && matchesFilter) {
                filteredList.add(game);
            }
        }
        gameAdapter.updateList(filteredList);
    }

    // פונקציה שמוציאה רק את השנה מהתאריך
    private String extractYear(String date) {
        if (date == null || date.isEmpty()) return ""; // אם אין תאריך מחזיר מחרוזת ריקה
        String[] parts = date.split(" ");
        return (parts.length >= 3) ? parts[2] : ""; // מחזיר רק את השנה
    }
}
