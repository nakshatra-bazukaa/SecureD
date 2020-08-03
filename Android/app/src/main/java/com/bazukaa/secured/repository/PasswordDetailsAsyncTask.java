package com.bazukaa.secured.repository;

import android.os.AsyncTask;

import com.bazukaa.secured.db.PasswordDetailsDao;
import com.bazukaa.secured.models.PasswordDetails;

public class PasswordDetailsAsyncTask extends AsyncTask<PasswordDetails, Void, Void> {
    public static final int INSERT_PASSWORD_DETAILS_REQUEST = 1;
    public static final int UPDATE_PASSWORD_DETAILS_REQUEST = 2;
    public static final int DELETE_PASSWORD_DETAILS_REQUEST = 3;

    private PasswordDetailsDao passwordDetailsDao;
    private int request;

    public PasswordDetailsAsyncTask(PasswordDetailsDao passwordDetailsDao, int request) {
        this.passwordDetailsDao = passwordDetailsDao;
        this.request = request;
    }


    @Override
    protected Void doInBackground(PasswordDetails... passwordDetails) {
        if(request == INSERT_PASSWORD_DETAILS_REQUEST)
            passwordDetailsDao.insert(passwordDetails[0]);
        else if(request == UPDATE_PASSWORD_DETAILS_REQUEST)
            passwordDetailsDao.update(passwordDetails[0]);
        else if(request == DELETE_PASSWORD_DETAILS_REQUEST)
            passwordDetailsDao.delete(passwordDetails[0]);
        return null;
    }
}
