package com.example.moviecatalogue.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.fragments.MovieFragment;
import com.example.moviecatalogue.fragments.SettingFragment;

public class NotificationTrigger extends IntentService {

    private SharedPreferences mPreferences;
    private boolean isReminderOn;

    private static String ALARM;
    private static Context mContext;

    public NotificationTrigger() {
        super("NotificationTrigger");
        ALARM = "com.example.moviecatalogue.ALARM";

        mPreferences = mContext.getSharedPreferences(mContext.getApplicationInfo().toString(), MODE_PRIVATE);
        isReminderOn = mPreferences.getBoolean(SettingFragment.getKeyReminder(), SettingFragment.isDefaultReminder());
    }

    static void startNotification(Context context){
        mContext = context;
        Intent intent = new Intent(context, NotificationTrigger.class);
        intent.setAction(ALARM);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(action.equals(ALARM) && isReminderOn){
                createNotification();
            } else {
                Log.d("Notif Trigger", "onHandleIntent: Notif can't be created");
            }
        }
    }

    private void createNotification() {
        Log.d("Notif Receiver", "Execute: create Notification");
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // get movie text - blom kelar
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Movie Catalogue")
                .setContentText("yoi");

        Intent intent = new Intent(mContext, MovieFragment.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
