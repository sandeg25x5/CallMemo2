package com.example.sandeeplamsal123.testapplication2;

import android.Manifest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sandeeplamsal123.testapplication2.models.ContactModel;
import com.example.sandeeplamsal123.testapplication2.receivers.PhoneReceiver;
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserRepository;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserViewModel;
import com.example.sandeeplamsal123.testapplication2.utils.ContactsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    @BindView(R.id.txt_user_name)
    TextView txtUserName;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_date)
    TextView txtDate;

    private UserRepository repository;
    private User user;
    private BroadcastReceiver broadcastReceiver;
    private UserViewModel viewModel;

    private ContactsUtils utils;
    private static String TAG = MainActivity.class.getSimpleName();
    private List<ContactModel> utilsList;
    private String userName;
    private HashMap<String, String> contactsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        broadcastReceiver = new PhoneReceiver();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                +WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        contactsMap = new HashMap<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    3);
        } else {
            utils = new ContactsUtils(this);
            utilsList = utils.getContactList();

            ArrayList<HashMap<String, String>> contactNames = new ArrayList<>();
            for (int i = 0; i < utilsList.size(); i++) {
                String name = utilsList.get(i).getContactName();
                String phone = utilsList.get(i).getContactNumber();
                String formattedPhone = phone.replaceAll("[\\s\\-()]", "");
                contactsMap.put(formattedPhone, name);
                contactNames.add(contactsMap);
            }
            Intent intent = getIntent();
            String phoneNumber = intent.getStringExtra("phoneNumber");

            for (int i = 0; i < contactNames.size(); i++) {

                userName = contactNames.get(i).get(phoneNumber);
            }
            if (userName != null) {
                txtUserName.setText(userName);
            } else {
                txtUserName.setText("Unknown");
            }
            txtPhone.setText("The incoming call is = " + phoneNumber);
            txtDate.setText(getCurrentTimeStamp());
            viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        }
        //registerPhoneReceiver();
    }

    @OnClick(R.id.btn_save_memo)
    public void saveMemo(View view) {
        repository = new UserRepository(getApplication());
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        if (userName != null) {
            userName = contactsMap.get(phoneNumber);
        } else {
            userName = "Unknown";
        }
        String userMemo = edtMemo.getText().toString().trim();
        String userCurrentTime = getCurrentTimeStamp();
        user = new User(userName, phoneNumber, userMemo, userCurrentTime);
        viewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    ArrayList<String> userPhoneList = new ArrayList<>();
                    for (int i = 0; i < users.size(); i++) {
                        userPhoneList.add(users.get(i).getUserPhoneNumber());
                    }
                    onPhoneArrayReceived(userPhoneList, user);
                } else {
                    Toast.makeText(MainActivity.this, "Your Memo cannot be Saved...", Toast.LENGTH_LONG).show();
                }
            }
        });
        finish();
    }

    public void onPhoneArrayReceived(ArrayList<String> phoneNumbers, User user) {
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
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


}
