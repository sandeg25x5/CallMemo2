package com.example.sandeeplamsal123.testapplication2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sandeeplamsal123.testapplication2.receivers.PhoneReceiver;
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserRepository;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_memo)
    TextInputEditText edtMemo;
    @BindView(R.id.btn_save_memo)
    Button btnSaveMemo;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_date)
    TextView txtDate;

    private UserRepository repository;
    private User user;
    private int count = 0;
    private BroadcastReceiver broadcastReceiver;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        broadcastReceiver = new PhoneReceiver();

        // These flags ensure that the activity can be launched when the screen is locked.
//                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                +WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        txtPhone.setText("The incoming call is = " + phoneNumber);
        txtDate.setText(getCurrentTimeStamp());
        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        //registerPhoneReceiver();
    }

    @OnClick(R.id.btn_save_memo)
    public void saveMemo(View view) {
        repository = new UserRepository(getApplication());
        String userName = "User " + count++;
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String userMemo = edtMemo.getText().toString().trim();
        String userCurrentTime=getCurrentTimeStamp();
        user = new User(userName, phoneNumber, userMemo,userCurrentTime);
        viewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    ArrayList<String> userPhoneList = new ArrayList<>();
                    for (int i = 0; i < users.size(); i++) {
                        userPhoneList.add(users.get(i).getUserPhoneNumber());
                    }
                    onPhoneArrayReceived(userPhoneList,user);

                } else {
                    Toast.makeText(MainActivity.this, "Your Memo cannot be Saved...", Toast.LENGTH_LONG).show();

                }
            }
        });

//        if (repository.getAllUsers().getValue().contains(phoneNumber)) {
//            repository.update(user);
//        } else {
//            repository.insert(user);
//        }
        finish();
    }

    public void onPhoneArrayReceived(ArrayList<String> phoneNumbers,User user) {
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        if (phoneNumbers.contains(phoneNumber)) {
            repository.update(user);
            Toast.makeText(MainActivity.this, "Your Memo Has been updated...", Toast.LENGTH_LONG).show();
        } else {
            repository.insert(user);
            Toast.makeText(MainActivity.this, "Your Memo Has been inserted...", Toast.LENGTH_LONG).show();

        }

    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View view) {
        Toast.makeText(MainActivity.this, "Not any new memo has been saved", Toast.LENGTH_LONG).show();
        finish();
    }

    public void registerPhoneReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unRegisterPhoneReceiver() {
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unRegisterPhoneReceiver();
    }

    /**
     *
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    //for bitbucket pushing and github...
}
