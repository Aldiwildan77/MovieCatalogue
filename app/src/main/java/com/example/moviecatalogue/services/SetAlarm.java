package com.example.moviecatalogue.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class SetAlarm {

    public static void alarmSetter(Context cOntext){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 32);

        AlarmManager alarmManager = (AlarmManager) cOntext.getSystemService(ALARM_SERVICE);

        NotificationReceiver notificationReceiver = new NotificationReceiver(cOntext);
        Intent intent = new Intent(cOntext, notificationReceiver.getClass());
        Log.d("Notif Receiver", "Execute: executed intent notification");
        PendingIntent sender = PendingIntent.getBroadcast(cOntext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
    }

}
