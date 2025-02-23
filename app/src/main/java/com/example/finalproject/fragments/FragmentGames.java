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

    // ספינרים לסינון
    private Spinner yearSpinner, genreSpinner, publisherSpinner, seriesSpinner;
    private String selectedYear = "", selectedGenre = "", selectedPublisher = "", selectedSeries = "";
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
        yearSpinner = view.findViewById(R.id.yearSpinner);
        genreSpinner = view.findViewById(R.id.genreSpinner);
        publisherSpinner = view.findViewById(R.id.publisherSpinner);
        seriesSpinner = view.findViewById(R.id.seriesSpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameAdapter = new GameAdapter(filteredList, getContext());
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

        // הגדרת ספינרים והאזנה לבחירות המשתמשים
        setupSpinner(yearSpinner, new SelectionListener() {
            @Override
            public void onItemSelected(String value) {
                selectedYear = value.equals("All") ? "" : value;
                applyFilters();
            }
        });

        setupSpinner(genreSpinner, new SelectionListener() {
            @Override
            public void onItemSelected(String value) {
                selectedGenre = value.equals("All") ? "" : value;
                applyFilters();
            }
        });

        setupSpinner(publisherSpinner, new SelectionListener() {
            @Override
            public void onItemSelected(String value) {
                selectedPublisher = value.equals("All") ? "" : value;
                applyFilters();
            }
        });

        setupSpinner(seriesSpinner, new SelectionListener() {
            @Override
            public void onItemSelected(String value) {
                selectedSeries = value.equals("All") ? "" : value;
                applyFilters();
            }
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

    // מסנן את הרשימה לפי שם המשחק וגם לפי הפילטרים שנבחרו
    private void applyFilters() {
        filteredList.clear();

        for (Game game : gameList) {
            String gameYear = extractYear(game.getReleaseDate());

            boolean matchesSearch = searchText.isEmpty() || game.getTitle().toLowerCase().contains(searchText.toLowerCase());
            boolean matchesYear = selectedYear.isEmpty() || gameYear.equals(selectedYear);
            boolean matchesPublisher = selectedPublisher.isEmpty() || game.getPublisher().equalsIgnoreCase(selectedPublisher);
            boolean matchesGenre = selectedGenre.isEmpty() || game.getGenre().equalsIgnoreCase(selectedGenre);
            boolean matchesSeries = selectedSeries.isEmpty() || game.getSeriesName().equalsIgnoreCase(selectedSeries);

            if (matchesSearch && matchesYear && matchesPublisher && matchesGenre && matchesSeries) {
                filteredList.add(game);
            }
        }
        gameAdapter.updateList(filteredList);
    }

    // פונקציה כללית לטיפול בבחירת ספינר
    private void setupSpinner(Spinner spinner, final SelectionListener listener) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemSelected(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // ממשק לניהול בחירת פריטים בספינר
    interface SelectionListener {
        void onItemSelected(String value);
    }

    // פונקציה שמוציאה רק את השנה מהתאריך
    private String extractYear(String date) {
        if (date == null || date.isEmpty()) return "";
        String[] parts = date.split(" ");
        return (parts.length >= 3) ? parts[2] : "";
    }
}
