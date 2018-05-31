package com.example.sandeeplamsal123.testapplication2.userdatabases;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
// In this way, the implementation of insert() is completely hidden from the UI.
    public void insertUser(User user) {
        repository.insert(user);
    }

}
