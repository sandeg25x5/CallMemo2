package com.example.sandeeplamsal123.testapplication2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.sandeeplamsal123.testapplication2.receivers.PhoneReceiver;
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserViewModel;
import com.example.sandeeplamsal123.testapplication2.utils.ContactsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserRecordDetailsActivity extends AppCompatActivity {
    private static String TAG = UserRecordDetailsActivity.class.getSimpleName();

    @BindView(R.id.txt_user_record)
    TextView txtUserRecord;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private BroadcastReceiver broadcastReceiver;
    private ContactsUtils utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record_details);
        ButterKnife.bind(this);
        broadcastReceiver = new PhoneReceiver();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                +WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        getWindow().addFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    ArrayList<String> userPhoneList = new ArrayList<>();
                    for (int i = 0; i < users.size(); i++) {
                        userPhoneList.add(users.get(i).getUserPhoneNumber());
                    }
                    onUserPhoneArrayListReceived(userPhoneList, users);
                } else {
                    txtUserRecord.setText("No Record For this Phone Number is found...");
                }
            }
        });

        //registerPhoneReceiver();
    }

    private void onUserPhoneArrayListReceived(ArrayList<String> phoneNumbers, List<User> users) {

        Log.i("PhoneNums", String.valueOf(phoneNumbers));

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        if (phoneNumbers.contains(phoneNumber)) {
            txtUserRecord.setText("the memo = " + users.get(phoneNumbers.indexOf(phoneNumber)).getUserMemo());
            Log.i(TAG, users.get(phoneNumbers.indexOf(phoneNumber)).getUserMemo());
        } else {
            txtUserRecord.setText("No Record For this Phone Number is found here at onUserPhoneArrayListReceived...");
        }
    }

    @OnClick(R.id.btn_ok)
    public void onOkClicked(View view) {
        finish();
    }

    public void registerPhoneReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unRegisterPhoneReceiver() {
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unRegisterPhoneReceiver();
    }
}
