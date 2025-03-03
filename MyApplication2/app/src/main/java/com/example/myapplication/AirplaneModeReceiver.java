package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
            boolean isEnabled = intent.getBooleanExtra("state", false);
            String message = isEnabled ? "Airplane mode is ON" : "Airplane mode is OFF";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}

