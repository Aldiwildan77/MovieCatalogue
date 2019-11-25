package com.example.moviecatalogue.models;

public class Movie {

    private String id;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;
    private double vote_average;
    private int[] genre_ids;

    public Movie(String id, String original_title, String overview, String poster_path, String release_date, double vote_average, int[] genre_ids) {
        this.id = id;
        this.original_title = original_title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.genre_ids = genre_ids;
    }

    @Override
    public String toString(){
        return "TITLE : " + original_title;
    }

    public String getPosterImageUrl(){
        return "https://image.tmdb.org/t/p/w342" + poster_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getDescription() {
        return overview;
    }

    public void setDescription(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }
}
