package com.example.moviecatalogue.interfaces;

import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

public interface ReleaseMovieCallbacks {
    void onSuccess(ArrayList<Movie> movies);
    void onFailure(boolean failure);
}