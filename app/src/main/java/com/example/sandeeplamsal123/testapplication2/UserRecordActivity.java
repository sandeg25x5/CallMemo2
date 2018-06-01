package com.example.sandeeplamsal123.testapplication2;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sandeeplamsal123.testapplication2.adapters.UserListRecyclerAdapter;
import com.example.sandeeplamsal123.testapplication2.models.ContactModel;
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserRepository;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserViewModel;
import com.example.sandeeplamsal123.testapplication2.utils.ContactsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRecordActivity extends AppCompatActivity {
    @BindView(R.id.user_list_recycler)
    RecyclerView userListRecycler;
    @BindView(R.id.no_records_txt)
    TextView noRecordTxt;

    private RecyclerView.LayoutManager manager;
    private UserRepository repository;
    private List<User> users;
    private UserListRecyclerAdapter adapter;
    private ProgressDialog dialog;
    private UserViewModel userViewModel;

    private static final Integer MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private ContactsUtils utils;
    private static String TAG = UserRecordActivity.class.getSimpleName();
    private List<ContactModel> utilsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    2);
        } else {
            utils = new ContactsUtils(this);
            utilsList = utils.getContactList();
            HashMap<String, String> contactsMap = new HashMap<>();
            ArrayList<HashMap<String, String>> contactNames = new ArrayList<>();
            for (int i = 0; i < utilsList.size(); i++) {
                String name = utilsList.get(i).getContactName();
                String phone = utilsList.get(i).getContactNumber();
                String formattedPhone = phone.replaceAll("[\\s\\-()]", "");
                contactsMap.put(name, formattedPhone);
                contactNames.add(contactsMap);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            manager = new LinearLayoutManager(UserRecordActivity.this);
            userListRecycler.setLayoutManager(manager);
            adapter = new UserListRecyclerAdapter(UserRecordActivity.this);
            userListRecycler.setAdapter(adapter);

            // Get a new or existing ViewModel from the ViewModelProvider.
            userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            // Add an observer on the LiveData returned by getAllUsers().
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    //Update the cached copy of the users in the adapter.
                    if (users != null) {
                        adapter.setUsers(users);
                    } else {
                        noRecordTxt.setVisibility(View.VISIBLE);
                        noRecordTxt.setText("NO Records Are Found...");
                    }
                }
            });
        }
    }
}
