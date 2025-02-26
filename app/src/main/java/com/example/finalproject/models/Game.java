package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    @SerializedName("name")
    private String title; // שם המשחק

    @SerializedName("released")
    private String releaseDate; // תאריך יציאה

    @SerializedName("background_image")
    private String imageUrl; // תמונת המשחק

    @SerializedName("description_raw")
    private String description; // תיאור המשחק

    @SerializedName("genres")
    private List<Genre> genres; // רשימת ז'אנרים

    @SerializedName("publishers")
    private List<Publisher> publishers; // רשימת מפרסמים

    @SerializedName("rating")
    private double rating; // דירוג המשחק

    @SerializedName("trailer")
    private String trailerLink; // קישור לטריילר

    // קונסטרקטור ריק (דרוש ל-Retrofit)
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

    public String getDescription() {
        return description != null && !description.isEmpty() ? description : "No description available.";
    }

    public double getRating() {
        return Double.isNaN(rating) ? 0.0 : rating;
    }


    // מחזיר את שם הז'אנר הראשון, או "Unknown" אם אין נתון
    public String getFirstGenre() {
        return (genres != null && !genres.isEmpty() && genres.get(0).getName() != null)
                ? genres.get(0).getName()
                : "Unknown Genre";
    }

    // מחזיר את שם המפרסם הראשון, או "Unknown Publisher" אם אין
    public String getPublisher() {
        return (publishers != null && !publishers.isEmpty() && publishers.get(0).getName() != null)
                ? publishers.get(0).getName()
                : "Unknown Publisher";
    }

    // מחזיר רשימה של כל הז'אנרים כטקסט מופרד בפסיקים
    public List<Genre> getGenres() {
        return genres != null ? genres : new ArrayList<>();
    }


    // מחזיר רשימה של כל המפרסמים כטקסט מופרד בפסיקים
    public String getPublishersList() {
        return (publishers != null && !publishers.isEmpty())
                ? publishers.stream().map(Publisher::getName).collect(Collectors.joining(", "))
                : "Unknown Publisher";
    }

    // Getter לקישור לטריילר (אם לא קיים, מחזיר חיפוש ביוטיוב)
    public String getTrailerLink() {
        return (trailerLink != null && !trailerLink.isEmpty())
                ? trailerLink
                : "https://www.youtube.com/results?search_query=" + title.replace(" ", "+") + "+game+trailer";
    }

    // מחלקות פנימיות לטיפול בז'אנרים, מפתחים ומפרסמים
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
