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

    @SerializedName("platforms")
    private List<PlatformWrapper> platforms; // רשימת פלטפורמות

    @SerializedName("genres")
    private List<Genre> genres; // רשימת ז'אנרים

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

    // מחזיר רשימה של כל הפלטפורמות הנתמכות כטקסט
    public String getPlatforms() {
        if (platforms == null || platforms.isEmpty()) {
            return "Unknown Platform";
        }
        return platforms.stream()
                .map(wrapper -> wrapper.platform.name)
                .collect(Collectors.joining(", "));
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

    // מחלקה פנימית לטיפול בז'אנרים
    public static class Genre {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name != null ? name : "Unknown";
        }
    }
    // מחלקה פנימית לפלטפורמות
    public static class PlatformWrapper {
        @SerializedName("platform")
        private Platform platform;
    }

    public static class Platform {
        @SerializedName("name")
        private String name;
    }
}
