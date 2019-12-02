package com.example.moviecatalogue.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.moviecatalogue.BuildConfig;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.activities.MainActivity;
import com.example.moviecatalogue.interfaces.ReleaseMovieCallbacks;
import com.example.moviecatalogue.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String TYPE_RELEASEREMINDER = "ReleaseReminder";
    public static final String TYPE_DAILYREMINDER = "DailyReminder";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private final static int ID_DAILYREMINDER = 100;
    private final static int ID_RELEASEREMINDER = 101;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private int notifId;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        if (type.equalsIgnoreCase(TYPE_DAILYREMINDER)) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            String title = "DAILY REMINDER";
            notifId = ID_DAILYREMINDER;
            showAlarmNotification(context, title, message, notifId);
        } else {
            notifId = ID_RELEASEREMINDER;
            checkNewReleaseMovies(new ReleaseMovieCallbacks() {
                @Override
                public void onSuccess(ArrayList<Movie> movies) {
                    listMovies = movies;
                    for (int i = 0; i < listMovies.size(); i++) {
                        showAlarmNotification(context, listMovies.get(i).getOriginal_title(), listMovies.get(i).getOriginal_title() + " has been release today", notifId++);
                    }
                }

                @Override
                public void onFailure(boolean failure) {
                    if (failure) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {

        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent intent;
        intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }

    public void setRepeatingAlarmRelease(Context context, boolean isChecked) {
        if (isChecked) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra(EXTRA_TYPE, TYPE_RELEASEREMINDER);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASEREMINDER, intent, 0);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
            Toast.makeText(context, "Release today alarm turned on", Toast.LENGTH_SHORT).show();
        } else {
            cancelAlarm(context, TYPE_RELEASEREMINDER);
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASEREMINDER) ? ID_DAILYREMINDER : ID_RELEASEREMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        if (type.equalsIgnoreCase(TYPE_DAILYREMINDER)) {
            Toast.makeText(context, "Daily reminder has turned off", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Release today alarm has turned off", Toast.LENGTH_LONG).show();
        }
    }

    private void checkNewReleaseMovies(final ReleaseMovieCallbacks callbacks) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todaydate = simpleDateFormat.format(date);

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        final String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.MOVIE_API + "&primary_release_date.gte=" + todaydate + "&primary_release_date.lte=" + todaydate;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {

                        JSONObject data = list.getJSONObject(i);

                        JSONArray genres = data.getJSONArray("genre_ids");
                        int[] genre_ids = new int[genres.length()];

                        for (int j = 0; j < genres.length(); j++) {
                            genre_ids[j] = genres.getInt(j);
                        }

                        double vote_average = 0.0;
                        String id, original_title, overview, poster_path, release_date;
                        id = original_title = overview = poster_path = release_date = "";

                        if (data.has("id")) {
                            id = data.getString("id");
                        }

                        if (data.has("original_title")) {
                            original_title = data.getString("original_title");
                        }

                        if (data.has("overview")) {
                            overview = data.getString("overview");
                        }

                        if (data.has("poster_path")) {
                            poster_path = data.getString("poster_path");
                        }

                        if (data.has("release_date")) {
                            release_date = data.getString("release_date");
                        }

                        if (data.has("vote_average")) {
                            vote_average = data.getDouble("vote_average");
                        }

                        Movie movie = new Movie(
                                id,
                                original_title,
                                overview,
                                poster_path,
                                release_date,
                                vote_average,
                                genre_ids
                        );

                        listItems.add(movie);
                    }
                    callbacks.onSuccess(listItems);
                    callbacks.onFailure(false);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
                callbacks.onFailure(true);
                callbacks.onSuccess(new ArrayList<Movie>());
            }
        });
    }

}
