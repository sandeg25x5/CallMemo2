package com.example.sandeeplamsal123.testapplication2.userdatabases;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao              //Dao stands for data access object
public interface UserDao {

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();

//    @Query("SELECT * FROM user_table WHERE userId IN (:userIds)")
//    LiveData<List<User>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user_table WHERE userName LIKE :first AND "
            + "userPhoneNumber LIKE :number AND " + "userMemo LIKE :memo LIMIT 1")
    User findByName(String first, String number, String memo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        //if the duplicate data is inserted only new data is inserted or previous data is replaced
    void insert(User user);

    @Delete
    void deleteUser(User user);

    @Query("UPDATE user_table SET userMemo = :userMemo,userName = :userName,userCurrentTime = :userCurrentTime WHERE userPhoneNumber = :userPhoneNumber")
    void updateMemo(String userMemo, String userName, String userCurrentTime, String userPhoneNumber);

    @Update
    void updateUser(User user);

}
