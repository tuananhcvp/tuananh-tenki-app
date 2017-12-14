package com.example.anh.itenki.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by anh on 2017/12/14.
 */

public class MyNotifyReceiver extends BroadcastReceiver {
    private String notifyState = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String checkState = intent.getStringExtra("Notify State");
        Log.d("Check state", "=> start send :" + checkState);
        Intent service = new Intent(context, MyNotifyService.class);

        if (checkState == null) {
            notifyState = "isSet";
            if (Utils.isNetworkConnected(context)) {
                context.startService(service);
                Log.d("Notify State", notifyState);
            }
        } else if (checkState.equalsIgnoreCase("On")) {
            notifyState = "On";
            if (Utils.isNetworkConnected(context)) {
                context.startService(service);
                Log.d("Notify State", notifyState);
            }
        } else if (checkState.equalsIgnoreCase("Off")) {
            notifyState = "Off";
            context.stopService(service);
            Log.d("Notify State", notifyState);
        }
    }
}
