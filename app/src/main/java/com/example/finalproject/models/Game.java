package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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

    // קונסטרקטור ריק (דרוש ל-Retrofit)
    public Game() {}

    // Getters
    public String getTitle() { return title; }
    public String getReleaseDate() { return releaseDate; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }

    // מחזיר את שם המפתח הראשון, או "Unknown" אם אין נתון
    public String getDeveloper() {
        return developers != null && !developers.isEmpty() ? developers.get(0).getName() : "Unknown";
    }

    // מחזיר את שם הז'אנר הראשון, או "Unknown" אם אין נתון
    public String getGenre() {
        return genres != null && !genres.isEmpty() ? genres.get(0).getName() : "Unknown";
    }

    // מחלקות פנימיות לטיפול בז'אנרים ומפתחים
    public static class Genre {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }

    public static class Developer {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }
}
