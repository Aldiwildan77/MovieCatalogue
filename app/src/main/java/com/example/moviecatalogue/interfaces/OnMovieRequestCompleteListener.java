package com.example.moviecatalogue.interfaces;

import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

public interface OnMovieRequestCompleteListener {
    void onMovieRequestComplete(ArrayList<Movie> movieList);
}