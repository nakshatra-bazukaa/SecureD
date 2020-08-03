package com.bazukaa.secured.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bazukaa.secured.models.PasswordDetails;

@Database(entities = {PasswordDetails.class}, version = 1)
public abstract class PasswordDetailsDatabase extends RoomDatabase {

    private static PasswordDetailsDatabase passwordDetailsDatabaseInstance;

    public abstract PasswordDetailsDao passwordDetailsDao();

    public static synchronized PasswordDetailsDatabase getInstance(Context context){
        if(passwordDetailsDatabaseInstance == null){
            passwordDetailsDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    PasswordDetailsDatabase.class, "password_details_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return passwordDetailsDatabaseInstance;
    }
}
