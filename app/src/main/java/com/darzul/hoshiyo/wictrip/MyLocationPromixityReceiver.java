package com.darzul.hoshiyo.wictrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class MyLocationPromixityReceiver extends BroadcastReceiver {

    Context context = null;

    public MyLocationPromixityReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean enter = intent.getBooleanExtra(key, false);
        if(enter) {
            Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Goodbye", Toast.LENGTH_SHORT).show();
        }
    }
}
