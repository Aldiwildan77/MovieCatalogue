package com.example.moviecatalogue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movieList;

    public MovieAdapter(Context context, ArrayList<Movie> movieList){
        this.context = context;
        this.movieList = movieList;
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        String imagePosterUrl = movieList.get(position).getPosterImageUrl();

        Picasso.with(context)
                .load(imagePosterUrl)
                .into(holder.mImageView);

        holder.title.setText(movieList.get(position).getOriginal_title());
        holder.overview.setText(movieList.get(position).getDescription());
        holder.releaseDate.setText(movieList.get(position).getRelease_date());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ConstraintLayout container;
        ImageView mImageView;
        TextView title;
        TextView overview;
        TextView releaseDate;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            container = itemView.findViewById(R.id.container);
            mImageView = itemView.findViewById(R.id.movieImage);
            title = itemView.findViewById(R.id.title);
            overview = itemView.findViewById(R.id.overview);
            releaseDate = itemView.findViewById(R.id.releaseDate);
        }
    }

}
