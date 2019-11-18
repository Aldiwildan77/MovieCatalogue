package com.example.moviecatalogue.models;

public class Movie {

    private String title, description, imgUrl;
    private double rating;
    private String[] genres;

    public Movie(){}

    public Movie(String title, String description, String imgUrl, double rating, String[] genres) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }
}
