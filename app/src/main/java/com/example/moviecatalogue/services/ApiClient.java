package com.example.moviecatalogue.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.moviecatalogue.BuildConfig;
import com.example.moviecatalogue.interfaces.OnMovieRequestCompleteListener;
import com.example.moviecatalogue.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiClient {

    private final String TAG = "MYAPP";
    private final String MOVIE_API = BuildConfig.MOVIE_API;
    private ArrayList<Movie> movieList;
    private Movie mv;
    private Activity activity;

    public ApiClient() {
    }

    public ApiClient(Context context) {
        AndroidNetworking.initialize(context);
        movieList = new ArrayList<>();
        activity = (Activity) context;
    }

    public void getMovies(final OnMovieRequestCompleteListener listener) {
        Log.d(TAG, "GET MOVIE");
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/popular")
                .addQueryParameter("api_key", MOVIE_API)
                .addQueryParameter("language", "id")
                .addQueryParameter("page", "1")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject data = results.getJSONObject(i);

                                JSONArray genres = data.getJSONArray("genre_ids");
                                int[] genre_ids = new int[genres.length()];

                                for (int j = 0; j < genres.length(); j++) {
                                    genre_ids[j] = genres.getInt(j);
                                }

                                Movie movie = new Movie(
                                        data.getString("id"),
                                        data.getString("original_title"),
                                        data.getString("overview"),
                                        data.getString("poster_path"),
                                        data.getString("release_date"),
                                        data.getDouble("vote_average"),
                                        genre_ids
                                );
                                Log.d(TAG, "movie id : " + movie.getId());
                                movieList.add(movie);
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onMovieRequestComplete(movieList);
                                }
                            });

                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                    }
                });
    }

    public Movie getMovieById(String id) {
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/" + id)
                .addQueryParameter("api_key", MOVIE_API)
                .addQueryParameter("language", "id")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray genres = response.getJSONArray("genres");
                            int[] genres_ids = new int[genres.length()];

                            for (int i = 0; i < genres.length(); i++) {
                                genres_ids[i] = genres.getJSONObject(i).getInt("id");
                            }

                            mv = new Movie(
                                    response.getString("id"),
                                    response.getString("original_title"),
                                    response.getString("overview"),
                                    response.getString("poster_path"),
                                    response.getString("release_date"),
                                    response.getDouble("vote_average"),
                                    genres_ids
                            );

                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                    }
                });
        return mv;
    }

    public void getMovieGenres(String id) {

    }

}
