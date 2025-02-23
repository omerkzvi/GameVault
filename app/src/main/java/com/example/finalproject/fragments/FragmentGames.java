package com.example.finalproject.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    // ספינרים לסינון
    private Spinner yearSpinner, genreSpinner, publisherSpinner, seriesSpinner;
    private String selectedYear = "", selectedGenre = "", selectedPublisher = "", selectedSeries = "";
    private String searchText = "";
    private static final String API_KEY = "41ce34c117204cf696fd040cc43dc20c"; // עדכני ל-API Key שלך

    public FragmentGames() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    // פונקציה לטעינת הנתונים מה-RAWG API
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadGamesFromAPI() {
        String dates = "2020-01-01," + java.time.LocalDate.now(); // כל המשחקים מ-2020 עד היום
        String platforms = "4"; // 4 = PC

        Call<GameResponse> call = RetrofitClient.getApiService().getGames(API_KEY, dates, platforms);
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
            boolean matchesSearch = searchText.isEmpty() || game.getTitle().toLowerCase().contains(searchText.toLowerCase());
            boolean matchesYear = selectedYear.isEmpty() || game.getReleaseDate().startsWith(selectedYear);
            boolean matchesGenre = selectedGenre.isEmpty(); // RAWG API מחזיר ג'אנרים ברשימה, דורש טיפול אחר
            boolean matchesPublisher = selectedPublisher.isEmpty(); // RAWG API לא מחזיר Publisher ישירות
            boolean matchesSeries = selectedSeries.isEmpty(); // אין שדה ישיר לסדרות

            if (matchesSearch && matchesYear && matchesGenre && matchesPublisher && matchesSeries) {
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
}
