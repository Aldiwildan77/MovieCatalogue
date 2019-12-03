package com.example.moviecatalogue.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.activities.DetailActivity;
import com.example.moviecatalogue.adapters.MovieAdapter;
import com.example.moviecatalogue.interfaces.OnMovieRequestCompleteListener;
import com.example.moviecatalogue.models.Movie;
import com.example.moviecatalogue.services.ApiClient;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

    private ArrayList<Movie> movieList;
    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressCircular;
    OnMovieRequestCompleteListener listener = new OnMovieRequestCompleteListener() {
        @Override
        public void onMovieRequestComplete(ArrayList<Movie> list) {
            // Toast.makeText(getApplicationContext(), Arrays.toString(movieList.toArray()), Toast.LENGTH_LONG).show();
            adapter.setMovieList(list);
            progressCircular.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    };
    private ApiClient apiClient;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        apiClient = new ApiClient(getActivity());

        progressCircular = getActivity().findViewById(R.id.progress_circular);

        ArrayList<Movie> movieList = new ArrayList<>();
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

        searchView = getActivity().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getMovieList().clear();
                adapter.notifyDataSetChanged();
                progressCircular.setVisibility(View.VISIBLE);
                apiClient.searchMovie(listener, query.trim().toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void init() {
        apiClient.getMovies(listener, "popular");
    }

    public void reloadMovie(String category){
        adapter.getMovieList().clear();
        adapter.notifyDataSetChanged();
        progressCircular.setVisibility(View.VISIBLE);
        apiClient.getMovies(listener, category);
    }

}
