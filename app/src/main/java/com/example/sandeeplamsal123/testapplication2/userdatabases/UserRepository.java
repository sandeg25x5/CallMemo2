package com.example.sandeeplamsal123.testapplication2.userdatabases;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private String TAG = UserRepository.this.getClass().getSimpleName();

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

//    public void updateMemo(String userMemo, String userPhoneNumber) {
//        new UpdateMemoAsyncTask(userDao).execute(userMemo, userPhoneNumber);
//    }
//
//    private static class UpdateMemoAsyncTask extends AsyncTask<String, Void, Void> {
//        private UserDao userDao;
//
//        public UpdateMemoAsyncTask(UserDao userDao) {
//            this.userDao = userDao;
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            String userMemo = strings[0];
//            String userPhoneNumber = strings[1];
//            userDao.updateMemo(userMemo, userPhoneNumber);
//            return null;
//        }
//    }


//    public List<User> getUsers() {
//        class GetUsersAsyncTask extends AsyncTask<Void, Void, List<User>> {
//            private UserDao userDao;
//            private List<User> allUsers;
//
//            public GetUsersAsyncTask(UserDao userDao, List<User> allUsers) {
//                this.userDao = userDao;
//                this.allUsers = allUsers;
//            }
//
//            @Override
//            protected List<User> doInBackground(Void... voids) {
//                allUsers = userDao.getAllUsers();
//                return allUsers;
//            }
//
//            @Override
//            protected void onPostExecute(List<User> users) {
//                super.onPostExecute(users);
//                if (users.size() == 0) {
//                    allUsers = null;
//                } else {
//                    allUsers = users;
//                }
//            }
//
//        }
//        new GetUsersAsyncTask(userDao, allUsers).execute();
//        return allUsers;
//    }

}
