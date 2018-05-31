package com.example.sandeeplamsal123.testapplication2.receivers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.sandeeplamsal123.testapplication2.MainActivity;
import com.example.sandeeplamsal123.testapplication2.UserRecordDetailsActivity;
import com.example.sandeeplamsal123.testapplication2.services.PhoneService;


public class PhoneReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneReceiver.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission is not granted ", Toast.LENGTH_SHORT).show();

                // context.stopService(intent);
            } else {
                final String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Call Received");
                        System.out.println("Receiver start");
                        Toast.makeText(context, " Receiver start ", Toast.LENGTH_SHORT).show();
                        //context.startService(intent);

                        //intent = new Intent(context, MainActivity.class);
                        Intent intent = new Intent(context, UserRecordDetailsActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }, 5000);


            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission is not granted ", Toast.LENGTH_SHORT).show();
//                ActivityCompat.requestPermissions(context,
//                        new String[]{Manifest.permission.READ_PHONE_STATE},
//                        MY_PERMISSIONS_REQUEST_PHONE_STATE);
//                // context.stopService(intent);
            } else {


                Log.i(TAG, "Call disconnected");
                System.out.println("Receiver started");
                Toast.makeText(context, " Receiver started ", Toast.LENGTH_SHORT).show();
                //context.startService(intent);
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }

        }


    }
}
