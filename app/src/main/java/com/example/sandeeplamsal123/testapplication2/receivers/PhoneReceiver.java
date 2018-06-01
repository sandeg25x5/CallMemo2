package com.example.sandeeplamsal123.testapplication2.receivers;

import android.Manifest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Handler;

import android.support.v4.content.ContextCompat;

import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.sandeeplamsal123.testapplication2.MainActivity;
import com.example.sandeeplamsal123.testapplication2.UserRecordDetailsActivity;



public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission is not granted ", Toast.LENGTH_SHORT).show();
            } else {
                final String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, " Receiver start ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, UserRecordDetailsActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }, 2000);
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission is not granted ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, " Receiver started ", Toast.LENGTH_SHORT).show();
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
