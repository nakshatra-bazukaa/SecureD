package com.bazukaa.secured.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pwd_details_table")
public class PasswordDetails {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String details;

    private String password;

    private long timeStamp;

    public PasswordDetails(int id, String title, String details, String password, long timeStamp) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.password = password;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
