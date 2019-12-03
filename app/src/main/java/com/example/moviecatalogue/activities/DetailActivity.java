package com.example.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "extra_movie";
    private final String ACTIVITY_TITLE = "Detail Movie";
    private String id="0";

    private TextView title, description, release, rating;
    private ImageView image;
    private Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(ACTIVITY_TITLE);
        bindViewHolder();

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        setViewValue(Objects.requireNonNull(movie));

        // Set ID untuk keperluan share link
        setId(movie.getId());

        share = (Button) findViewById(R.id.btnShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String halaman = "https://www.themoviedb.org/movie/";
                halaman+=id;
                Uri webpage= Uri.parse(halaman);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                } else {
                    Log.d("ImplicitIntents", "Can't handle this intent!");
                }
            }
        });
    }

    private void setId(String id){
        this.id=id;
    }

    @SuppressLint("SetTextI18n")
    private void setViewValue(Movie movie) {
        Picasso.with(getApplicationContext())
                .load(movie.getPosterImageUrl())
                .into(image);

        title.setText(movie.getOriginal_title());
        description.setText(movie.getDescription());
        release.setText(movie.getRelease_date());
        rating.setText(Double.toString(movie.getVote_average()));
    }

    private void bindViewHolder() {
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        release = findViewById(R.id.tv_release);
        rating = findViewById(R.id.tv_rating);
        image = findViewById(R.id.image_container);
    }

    public static String getExtraMovie() {
        return EXTRA_MOVIE;
    }
}
