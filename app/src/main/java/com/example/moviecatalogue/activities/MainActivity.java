package com.example.moviecatalogue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.adapters.MovieAdapter;
import com.example.moviecatalogue.fragments.MovieFragment;
import com.example.moviecatalogue.fragments.SettingFragment;
import com.example.moviecatalogue.interfaces.MovieRequest;
import com.example.moviecatalogue.models.Movie;
import com.example.moviecatalogue.services.ApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentTransaction settingFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        MovieFragment movieFragment = new MovieFragment();
        fragmentTransaction.replace(R.id.frame_test, movieFragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_setting){
            settingFragmentTransaction = fragmentManager.beginTransaction();
            SettingFragment settingFragment = new SettingFragment();

            settingFragmentTransaction.replace(R.id.frame_test, settingFragment);
            settingFragmentTransaction.addToBackStack(null);
            settingFragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
