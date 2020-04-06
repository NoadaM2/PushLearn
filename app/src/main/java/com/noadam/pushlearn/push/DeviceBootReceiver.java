package com.noadam.pushlearn.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.noadam.pushlearn.activities.MenuActivity;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // on device boot compelete, reset the alarm
            MenuActivity menuActivity = new MenuActivity();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("ShownCards", 0);
            editor.apply();
            menuActivity.sendNotify(true);
        }
    }
}