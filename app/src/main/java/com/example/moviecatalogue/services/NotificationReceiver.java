package com.example.moviecatalogue.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notif Receiver", "Execute: on receive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            createNotification(context);
        } else {
            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();
        }
    }

    private void createNotification(Context context){
        NotificationTrigger.startNotification(context);
    }
}
