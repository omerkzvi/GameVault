package com.example.finalproject.api;

import com.example.finalproject.models.GameResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RAWGApiService {
    @GET("games")
    Call<GameResponse> getGames(
            @Query("key") String apiKey,
            @Query("dates") String dates,
            @Query("platforms") String platforms
    );
}
