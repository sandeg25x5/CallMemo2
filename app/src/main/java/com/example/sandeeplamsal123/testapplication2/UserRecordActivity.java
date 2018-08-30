package com.example.sandeeplamsal123.testapplication2;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
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
        checkPermission();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    2);
            Log.e("Test Record","Hello here in oncreate test inside if permitted okay");
        } else {
            Log.e("Hello Already","Permitted");
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
            if(repository==null){
                Log.e("Repo","Is null");
            }
            adapter = new UserListRecyclerAdapter(UserRecordActivity.this,repository);
            Log.e("Test Record","Hello here in on create test inside if the pohne state not permitted");
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
                        Log.e("test","TestCase on Changed user!null");
                        adapter.setUsers(users);
                    } else {
                        Log.e("Test","Nod Records");
                        noRecordTxt.setVisibility(View.VISIBLE);
                        noRecordTxt.setText("NO Records Are Found...");
                    }
                }
            });
        }
    }
    public void checkPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(UserRecordActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }
}
