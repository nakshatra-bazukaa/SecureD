package com.bazukaa.secured.db;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.bazukaa.secured.models.PasswordDetails;

@Database(entities = {PasswordDetails.class}, version = 1)
public abstract class PasswordDetailsDatabase extends RoomDatabase {

    private static PasswordDetailsDatabase passwordDetailsDatabaseInstance;

    public abstract PasswordDetailsDao passwordDetailsDao();

    public static synchronized PasswordDetailsDatabase getInstance(Context context){
        if(passwordDetailsDatabaseInstance == null){
            passwordDetailsDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    PasswordDetailsDatabase.class, "password_details_database")
                    .addCallback(roomDbCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return passwordDetailsDatabaseInstance;
    }

    private static RoomDatabase.Callback roomDbCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulatePasswordDetailsDbTask(passwordDetailsDatabaseInstance).execute();
        }
    };
    private static class PopulatePasswordDetailsDbTask extends AsyncTask<Void, Void, Void> {
        private PasswordDetailsDao passwordDetailsDao;

        public PopulatePasswordDetailsDbTask(PasswordDetailsDatabase passwordDetailsDatabase) {
            passwordDetailsDao = passwordDetailsDatabase.passwordDetailsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PasswordDetails initialPwdDetails = new PasswordDetails("Title goes here", "Just jot down anything here", "hgkdbnsjd", System.currentTimeMillis());
            passwordDetailsDao.insert(initialPwdDetails);
            return null;
        }
    }
}
