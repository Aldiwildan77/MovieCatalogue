package com.example.moviecatalogue.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.fragments.MovieFragment;
import com.example.moviecatalogue.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentTransaction settingFragmentTransaction;
    MovieFragment movieFragment;
    SettingFragment settingFragment;

//    SharedPreferences mPrefences;
//    boolean isReminderOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mPrefences = getSharedPreferences(getApplication().toString(), MODE_PRIVATE);
//        isReminderOn = mPrefences.getBoolean(SettingFragment.getKeyReminder(), SettingFragment.isDefaultReminder());
//        if (isReminderOn) setupNotification();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        movieFragment = new MovieFragment();
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
        if (item.getItemId() == R.id.menu_item_setting) {

            settingFragmentTransaction = fragmentManager.beginTransaction();
            settingFragment = new SettingFragment();

            settingFragmentTransaction.replace(R.id.frame_test, settingFragment);
            settingFragmentTransaction.addToBackStack(null);
            settingFragmentTransaction.commit();
            return true;

        } else if (item.getItemId() == R.id.popular){
            movieFragment.reloadMovie("popular");
            return true;
        } else if (item.getItemId() == R.id.top_rated){
            movieFragment.reloadMovie("top_rated");
            return true;
        } else if (item.getItemId() == R.id.upcoming){
            movieFragment.reloadMovie("upcoming");
            return true;
        } else if (item.getItemId() == R.id.now_playing){
            movieFragment.reloadMovie("now_playing");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void setupNotification() {
//        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar updateTime = Calendar.getInstance();
//        updateTime.setTimeInMillis(System.currentTimeMillis());
//        updateTime.set(Calendar.HOUR_OF_DAY, 7);
//        updateTime.set(Calendar.MINUTE, 0);
//
//        int offset = updateTime.getTimeZone().getOffset(updateTime.getTimeInMillis());
//        long futureInMillis = updateTime.getTimeInMillis() + offset;
//        System.out.println(futureInMillis + " " + updateTime.getTimeInMillis());
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
//    }
}
