package com.example.sandeeplamsal123.testapplication2.userdatabases;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        new InsertAsyncTask(userDao).execute(user);
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    public void update(User user) {
        new UpdateAsyncTask(userDao).execute(user);
    }

    private static class UpdateAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public UpdateAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }


        @Override
        protected Void doInBackground(User... users) {
            userDao.updateMemo(users[0].getUserMemo(), users[0].getUserName(), users[0].getUserCurrentTime(), users[0].getUserPhoneNumber());
            return null;
        }
    }

    public void delete(String userPhoneNumber) {
        new DeleteAsyncTask(userDao).execute(userPhoneNumber);
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {
        private UserDao userDao;

        public DeleteAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            userDao.deleteMemo(strings[0]);
            return null;
        }
    }
}
