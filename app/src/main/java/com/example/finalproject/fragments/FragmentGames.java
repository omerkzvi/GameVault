package com.example.finalproject.fragments;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapters.GameAdapter;
import com.example.finalproject.api.RetrofitClient;
import com.example.finalproject.models.Game;
import com.example.finalproject.models.GameResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentGames extends Fragment {
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private List<Game> gameList = new ArrayList<>();
    private List<Game> filteredList = new ArrayList<>();
    private SearchView searchView;
    private Button filterButton;

    // משתנים לסינון
    private String selectedYear = "", selectedGenre = "", selectedPublisher = "", selectedRating = "";
    private double minRating = 0, maxRating = 5;
    private String searchText = "";
    private static final String API_KEY = "41ce34c117204cf696fd040cc43dc20c"; // API Key שלך

    public FragmentGames() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // קישור לאלמנטים ב-XML
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        filterButton = view.findViewById(R.id.filterButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameAdapter = new GameAdapter(filteredList, getContext());
        recyclerView.setAdapter(gameAdapter);

        // טעינת נתונים מה-API
        loadGamesFromAPI();

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

        // כפתור סינון
        filterButton.setOnClickListener(v -> showFilterDialog());

        return view;
    }

    // פונקציה לפתיחת חלון הסינון
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Filters");

        // יצירת Layout דינמי לדיאלוג
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        final Spinner yearSpinner = dialogView.findViewById(R.id.yearSpinner);
        final Spinner genreSpinner = dialogView.findViewById(R.id.genreSpinner);
        final Spinner ratingSpinner = dialogView.findViewById(R.id.ratingSpinner);
        final Spinner publisherSpinner = dialogView.findViewById(R.id.publisherSpinner);

        // הגדרת ערכים לכל Spinner
        setUpSpinner(yearSpinner, new String[]{"All", "2024", "2023", "2022", "2021", "2020"});
        setUpSpinner(genreSpinner, new String[]{"All", "Action", "RPG", "Shooter", "Adventure", "Strategy"});
        setUpSpinner(ratingSpinner, new String[]{"All", "0-1", "1-2", "2-3", "3-4", "4-5"});
        setUpSpinner(publisherSpinner, new String[]{"All", "Ubisoft", "Electronic Arts", "Activision", "Rockstar Games"});

        builder.setPositiveButton("Apply", (dialog, which) -> {
            selectedYear = getSelectedSpinnerValue(yearSpinner);
            selectedGenre = getSelectedSpinnerValue(genreSpinner);
            selectedPublisher = getSelectedSpinnerValue(publisherSpinner);

            selectedRating = getSelectedSpinnerValue(ratingSpinner);

            if (!selectedRating.isEmpty()) {
                try {
                    String[] ratingRange = selectedRating.split("-");
                    if (ratingRange.length == 2) {
                        minRating = Double.parseDouble(ratingRange[0]);
                        maxRating = Double.parseDouble(ratingRange[1]);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid rating selection", Toast.LENGTH_SHORT).show();
                    minRating = 0;
                    maxRating = 5;
                }
            } else {
                minRating = 0;
                maxRating = 5;
            }

            applyFilters();
        });


        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // פונקציה כללית לטיפול בהגדרת ספינר
    private void setUpSpinner(Spinner spinner, String[] values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, values);
        spinner.setAdapter(adapter);
    }

    // מחזיר את הערך שנבחר בספינר
    private String getSelectedSpinnerValue(Spinner spinner) {
        String value = spinner.getSelectedItem().toString();
        return value.equals("All") ? "" : value;
    }

    // פונקציה לטעינת הנתונים מה-RAWG API
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadGamesFromAPI() {
        String dates = "2020-01-01," + java.time.LocalDate.now(); // כל המשחקים מ-2020 עד היום

        Call<GameResponse> call = RetrofitClient.getApiService().getGames(API_KEY, dates, null); // אין סינון פלטפורמות
        call.enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(@NonNull Call<GameResponse> call, @NonNull Response<GameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gameList.clear();
                    gameList.addAll(response.body().getResults());
                    applyFilters();
                } else {
                    Toast.makeText(getContext(), "Failed to load games", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GameResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // מסנן את הרשימה לפי שם המשחק וגם לפי הפילטרים שנבחרו
    private void applyFilters() {
        filteredList.clear();

        for (Game game : gameList) {
            boolean matchesSearch = searchText.isEmpty() ||
                    (game.getTitle() != null && game.getTitle().toLowerCase().contains(searchText.toLowerCase()));

            boolean matchesYear = selectedYear.isEmpty() ||
                    (game.getReleaseDate() != null && game.getReleaseDate().startsWith(selectedYear));

            boolean matchesGenre = selectedGenre.isEmpty() ||
                    (game.getGenres() != null && !game.getGenres().isEmpty() &&
                            game.getGenres().stream().anyMatch(genre -> genre.getName().equalsIgnoreCase(selectedGenre)));

            boolean matchesPublisher = selectedPublisher.isEmpty() ||
                    (game.getDeveloper() != null && game.getDeveloper().equalsIgnoreCase(selectedPublisher));

            boolean matchesRating = game.getRating() >= minRating && game.getRating() <= maxRating;

            if (matchesSearch && matchesYear && matchesGenre && matchesPublisher && matchesRating) {
                filteredList.add(game);
            }
        }

        // עדכון הרשימה בתצוגת ה-RecyclerView
        gameAdapter.updateList(filteredList);
    }
}
