package com.example.sandeeplamsal123.testapplication2.userdatabases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.support.annotation.NonNull;

@Entity(tableName = "user_table")
//use tableName=some string if you want your table name be different from class name
public class User {
    @ColumnInfo
    private String userName;

    @PrimaryKey
    @NonNull
    @ColumnInfo
    private String userPhoneNumber;
    @ColumnInfo
    private String userMemo;
    @ColumnInfo
    private String userCurrentTime;

    public User(String userName, @NonNull  String userPhoneNumber, String userMemo, String userCurrentTime) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userMemo = userMemo;
        this.userCurrentTime = userCurrentTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    public String getUserName() {
        return userName;
    }

    @NonNull
    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public String getUserCurrentTime() {
        return userCurrentTime;
    }

    public void setUserCurrentTime(String userCurrentTime) {
        this.userCurrentTime = userCurrentTime;
    }


}
