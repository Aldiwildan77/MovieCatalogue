package com.example.moviecatalogue.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.adapters.MovieAdapter;
import com.example.moviecatalogue.interfaces.MovieRequest;
import com.example.moviecatalogue.models.Movie;
import com.example.moviecatalogue.services.ApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movieList;
    MovieAdapter adapter;
    RecyclerView recyclerView;

    ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressCircular = findViewById(R.id.progress_circular);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getApplicationContext(), movieList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        init();
    }

    public void init(){

        MovieRequest.OnMovieRequestCompleteListener listener = new MovieRequest.OnMovieRequestCompleteListener() {
            @Override
            public void onMovieRequestComplete(ArrayList<Movie> movieList) {
                // Toast.makeText(getApplicationContext(), Arrays.toString(movieList.toArray()), Toast.LENGTH_LONG).show();
                progressCircular.setVisibility(View.GONE);
                adapter.setMovieList(movieList);
                adapter.notifyDataSetChanged();
            }
        };

        new ApiClient(this).getMovies(listener);
    }
}
