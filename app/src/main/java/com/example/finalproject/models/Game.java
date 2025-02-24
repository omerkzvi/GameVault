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

    @SerializedName("developers")
    private List<Developer> developers; // רשימת מפתחים

    @SerializedName("genres")
    private List<Genre> genres; // רשימת ז'אנרים

    @SerializedName("rating")
    private double rating; // דירוג המשחק

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

    // מחזיר את שם המפתח הראשון, או "Unknown" אם אין נתון
    public String getDeveloper() {
        return (developers != null && !developers.isEmpty() && developers.get(0).getName() != null)
                ? developers.get(0).getName()
                : "Unknown";
    }

    // מחזיר את שם הז'אנר הראשון, או "Unknown" אם אין נתון
    public String getFirstGenre() {
        return (genres != null && !genres.isEmpty() && genres.get(0).getName() != null)
                ? genres.get(0).getName()
                : "Unknown";
    }

    // מחזיר רשימה של כל הז'אנרים כטקסט מופרד בפסיקים
    public List<Genre> getGenres() {
        return genres != null ? genres : new ArrayList<>();
    }


    // מחזיר רשימה של כל המפתחים כטקסט מופרד בפסיקים
    public String getNames() {
        return (developers != null && !developers.isEmpty())
                ? developers.stream().map(Developer::getName).collect(Collectors.joining(", "))
                : "Unknown";
    }

    // מחלקות פנימיות לטיפול בז'אנרים ומפתחים
    public static class Genre {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name != null ? name : "Unknown";
        }
    }

    public static class Developer {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name != null ? name : "Unknown";
        }
    }
}
