package com.example.finalproject.api;

import com.example.finalproject.models.GameResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RAWGApiService {
    //this method retrieves a list of games based on the given parameters.
    @GET("games")
    Call<GameResponse> getGames(
            @Query("key") String apiKey, // API key for authentication
            @Query("dates") String dates, // date range for filtering games
            @Query("platforms") String platforms // filter games by platforms
    );
}
