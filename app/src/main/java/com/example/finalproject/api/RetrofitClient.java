package com.example.finalproject.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.rawg.io/api/"; // the base URL for the API
    private static final String API_KEY = "41ce34c117204cf696fd040cc43dc20c"; // the API key for authentication

    private static Retrofit retrofit = null;

    /**
     * This method returns an instance of the API service interface.
     * If Retrofit has not been initialized yet, it initializes it.
     *
     * @return An instance of RAWGApiService.
     */
    public static RAWGApiService getApiService() {
        if (retrofit == null) {
            // if Retrofit instance is null, create a new one
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // set the base URL for the API
                    .addConverterFactory(GsonConverterFactory.create()) // add Gson converter to handle JSON data
                    .build(); // build the Retrofit instance
        }
        return retrofit.create(RAWGApiService.class); // return the API service instance
    }

    public static String getApiKey() {
        return API_KEY; // return the predefined API key
    }
}
