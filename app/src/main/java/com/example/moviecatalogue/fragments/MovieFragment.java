package com.example.moviecatalogue.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.activities.DetailActivity;
import com.example.moviecatalogue.adapters.MovieAdapter;
import com.example.moviecatalogue.interfaces.OnMovieRequestCompleteListener;
import com.example.moviecatalogue.models.Movie;
import com.example.moviecatalogue.services.ApiClient;
import com.example.moviecatalogue.services.NotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MovieFragment extends Fragment {

    private ArrayList<Movie> movieList;
    private MovieAdapter adapter;
    private RecyclerView recyclerView;

    private ProgressBar progressCircular;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressCircular = getActivity().findViewById(R.id.progress_circular);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getActivity().getApplicationContext(), movieList);

        recyclerView = getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        init();

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.getExtraMovie(), movie);
                startActivity(intent);
            }
        });
    }

    private void init() {

        OnMovieRequestCompleteListener listener = new OnMovieRequestCompleteListener() {
            @Override
            public void onMovieRequestComplete(ArrayList<Movie> movieList) {
                // Toast.makeText(getApplicationContext(), Arrays.toString(movieList.toArray()), Toast.LENGTH_LONG).show();
                progressCircular.setVisibility(View.GONE);
                adapter.setMovieList(movieList);
                adapter.notifyDataSetChanged();
            }
        };

        new ApiClient(getContext()).getMovies(listener);
    }

}
