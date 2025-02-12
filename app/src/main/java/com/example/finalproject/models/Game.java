package com.example.finalproject.models;
public class Game {
    private String title, publisher, genre, description, releaseDate, seriesName, trailerUrl, developer;
    private boolean partOfSeries;
    private int seriesNumber;

    // קונסטרקטור ריק (דרוש ל-Firebase)
    public Game() {}

    // קונסטרקטור מלא
    public Game(String title, String publisher, String genre, String description, String releaseDate,
                boolean partOfSeries, String seriesName, int seriesNumber, String trailerUrl, String developer) {
        this.title = title;
        this.publisher = publisher;
        this.genre = genre;
        this.description = description;
        this.releaseDate = releaseDate;
        this.partOfSeries = partOfSeries;
        this.seriesName = seriesName;
        this.seriesNumber = seriesNumber;
        this.trailerUrl = trailerUrl;
        this.developer = developer;
    }

    // Getters & Setters
    public String getTitle() { return title; }
    public String getPublisher() { return publisher; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
    public String getReleaseDate() { return releaseDate; }
    public boolean isPartOfSeries() { return partOfSeries; }
    public String getSeriesName() { return seriesName; }
    public int getSeriesNumber() { return seriesNumber; }
    public String getTrailerUrl() { return trailerUrl; }
    public String getDeveloper() { return developer; }
}
