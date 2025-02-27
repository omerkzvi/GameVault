package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    @SerializedName("name")
    private String title; // שם המשחק

    @SerializedName("released")
    private String releaseDate; // release date

    @SerializedName("background_image")
    private String imageUrl; // game image

    @SerializedName("genres")
    private List<Genre> genres; // list of genres

    @SerializedName("platforms")
    private List<PlatformWrapper> platforms; // list of platforms

    @SerializedName("rating")
    private double rating; // game rating

    @SerializedName("trailer")
    private String trailerLink; // trailer

    // Empty constructor (required for Retrofit)
    public Game() {}

    // Getters עם בדיקות ל-null
    public String getTitle() {
        return title != null ? title : "Unknown Title";
    }

    public String getReleaseDate() {
        return releaseDate != null ? releaseDate : "Unknown Date";
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl : "";
    }

    public double getRating() {
        return (rating >= 0 && rating <= 5) ? rating : 0.0;
    }

    public String getPlatform() {
        if (platforms == null || platforms.isEmpty()) {
            return "Unknown Platform";
        }

        List<String> platformNames = new ArrayList<>();
        for (PlatformWrapper wrapper : platforms) {
            if (wrapper != null && wrapper.platform != null && wrapper.platform.name != null) {
                platformNames.add(wrapper.platform.name);
            }
        }
        return !platformNames.isEmpty() ? String.join(", ", platformNames) : "Unknown Platform";
    }

    public String getFirstGenre() {
        return (genres != null && !genres.isEmpty() && genres.get(0).getName() != null)
                ? genres.get(0).getName()
                : "Unknown Genre";
    }

    // מחזיר רשימה של כל הז'אנרים כטקסט מופרד בפסיקים
    public List<Genre> getGenres() {
        return genres != null ? genres : new ArrayList<>();
    }

    // Getter לקישור לטריילר (אם לא קיים, מחזיר חיפוש ביוטיוב)
    public String getTrailerLink() {
        return (trailerLink != null && !trailerLink.isEmpty())
                ? trailerLink
                : "https://www.youtube.com/results?search_query=" + title.replace(" ", "+") + "+game+trailer";
    }

    // מחלקות פנימיות לז'אנרים, ופלטפורמות
    public static class Genre {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name != null ? name : "Unknown";
        }
    }

    public static class PlatformWrapper {
        @SerializedName("platform")
        private Platform platform;
    }

    public static class Platform {
        @SerializedName("name")
        private String name;
    }
}
