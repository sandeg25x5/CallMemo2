package com.example.sandeeplamsal123.testapplication2;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserDatabase;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserRepository;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserViewModel;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        ButterKnife.bind(this);
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
                        Log.i("UserList",String.valueOf(users.get(0).getUserMemo()));
                    } else {
                        noRecordTxt.setVisibility(View.VISIBLE);
                        noRecordTxt.setText("NO Records Are Found...");
                    }

                }
            });

        }

        //new populateDatabaseTask().execute();


    }


    public void onPermisssionGranted() {

    }

//    private class populateDatabaseTask extends AsyncTask<Void, Void, List<User>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = new ProgressDialog(UserRecordActivity.this);
//            dialog.setMessage("Loading please wait...");
//            dialog.show();
//        }
//
//        @Override
//        protected List<User> doInBackground(Void... voids) {
//            repository = new UserRepository(getApplication());
//            users = repository.getAllUsers();
//            return users;
//        }
//
//        @Override
//        protected void onPostExecute(List<User> users) {
//            super.onPostExecute(users);
//            if (users.size()==0) {
//                dialog.dismiss();
//                noRecordTxt.setVisibility(View.VISIBLE);
//                noRecordTxt.setText("No records found...");
//            } else {
//                dialog.dismiss();
//                manager = new LinearLayoutManager(UserRecordActivity.this);
//                userListRecycler.setLayoutManager(manager);
//                adapter = new UserListRecyclerAdapter(UserRecordActivity.this, users);
//                userListRecycler.setAdapter(adapter);
//            }
//        }
//    }
}
