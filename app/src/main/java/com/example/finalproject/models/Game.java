package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    @SerializedName("name")
    private String title;  // the title of the game

    @SerializedName("released")
    private String releaseDate; // release date of the game

    @SerializedName("background_image")
    private String imageUrl; // URL of the game's image

    @SerializedName("description_raw")
    private String description; // description of the game

    @SerializedName("genres")
    private List<Genre> genres; // list of game genres


    @SerializedName("publishers")
    private List<Publisher> publishers; // list of publishers of the game

    @SerializedName("rating")
    private double rating; // rating of the game

    @SerializedName("trailer")
    private String trailerLink; // URL link to the game's trailer

    // empty constructor required by Retrofit
    public Game() {}

    // getters with null checks and default values
    public String getTitle() {
        return title != null ? title : "Unknown Title"; // return "Unknown Title" if title is null
    }

    public String getReleaseDate() {
        return releaseDate != null ? releaseDate : "Unknown Date";// return "Unknown Date" if releaseDate is null
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl : "";// return an empty string if imageUrl is null
    }

    public String getDescription() {
        return description != null && !description.isEmpty() ? description : "No description available."; // Return default if description is null or empty
    }

    public double getRating() {
        return Double.isNaN(rating) ? 0.0 : rating;// return 0.0 if rating is NaN
    }


    // returns the name of the first genre, or "Unknown Genre" if none is available
    public String getFirstGenre() {
        return (genres != null && !genres.isEmpty() && genres.get(0).getName() != null)
                ? genres.get(0).getName()
                : "Unknown Genre";
    }

    // returns the name of the first publisher, or "Unknown Publisher" if none is available
    public String getPublisher() {
        return (publishers != null && !publishers.isEmpty() && publishers.get(0).getName() != null)
                ? publishers.get(0).getName()
                : "Unknown Publisher";
    }

    // returns the list of all genres or an empty list if none exists
    public List<Genre> getGenres() {
        return genres != null ? genres : new ArrayList<>();
    }


    // returns a comma-separated list of all publishers, or "Unknown Publisher" if none
    public String getPublishersList() {
        return (publishers != null && !publishers.isEmpty())
                ? publishers.stream().map(Publisher::getName).collect(Collectors.joining(", "))
                : "Unknown Publisher";
    }

    // getter for the trailer link; if not available, returns a YouTube search link for the game trailer
    public String getTrailerLink() {
        return (trailerLink != null && !trailerLink.isEmpty())
                ? trailerLink
                : "https://www.youtube.com/results?search_query=" + title.replace(" ", "+") + "+game+trailer";
    }

    // inner classes to handle Genre and Publisher
    public static class Genre {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name != null ? name : "Unknown";
        }
    }

    public static class Publisher {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name != null ? name : "Unknown";
        }
    }
}
