package com.bazukaa.secured.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bazukaa.secured.models.PasswordDetails;

@Dao
public interface PasswordDetailsDao {

    @Insert
    void insert(PasswordDetails passwordDetails);

    @Delete
    void delete(PasswordDetails passwordDetails);

    @Update
    void update(PasswordDetails passwordDetails);

    @Query("SELECT * FROM pwd_details_table ORDER BY id DESC")
    LiveData<PasswordDetails> getPasswordDetails();
}
