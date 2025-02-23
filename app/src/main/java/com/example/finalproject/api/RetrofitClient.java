package com.example.finalproject.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static final String API_KEY = "41ce34c117204cf696fd040cc43dc20c";

    private static Retrofit retrofit = null;

    public static RAWGApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RAWGApiService.class);
    }

    public static String getApiKey() {
        return API_KEY;
    }
}
