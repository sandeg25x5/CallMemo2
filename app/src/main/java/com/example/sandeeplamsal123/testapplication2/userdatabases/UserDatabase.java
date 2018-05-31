package com.example.sandeeplamsal123.testapplication2.userdatabases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {User.class}, version = 3)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static UserDatabase INSTANCE;

    //making UserDatabase class singleton to prevent having multiple instances of the database opened at the same time.
    public static UserDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here with name user_database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "user_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            //new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more users, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final UserDao mDao;

        public PopulateDbAsync(UserDatabase db) {
            mDao = db.userDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
           // mDao.deleteUser();
            User user = new User("Sandeep", "9841055949", "call back him at 4 pm for a meeting...","");
            mDao.insert(user);
            return null;
        }
    }

}