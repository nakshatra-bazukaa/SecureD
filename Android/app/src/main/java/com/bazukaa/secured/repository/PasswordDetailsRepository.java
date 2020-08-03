package com.bazukaa.secured.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bazukaa.secured.db.PasswordDetailsDao;
import com.bazukaa.secured.db.PasswordDetailsDatabase;
import com.bazukaa.secured.models.PasswordDetails;

import java.util.List;

public class PasswordDetailsRepository {

    private PasswordDetailsDao passwordDetailsDao;
    private LiveData<List<PasswordDetails>> passwordDetailsList;

    public PasswordDetailsRepository(Application application){
        PasswordDetailsDatabase passwordDetailsDatabase = PasswordDetailsDatabase.getInstance(application);
        passwordDetailsDao = passwordDetailsDatabase.passwordDetailsDao();
        passwordDetailsList = passwordDetailsDao.getPasswordDetailsList();
    }

    public void insert(PasswordDetails passwordDetails) { new PasswordDetailsAsyncTask(passwordDetailsDao, PasswordDetailsAsyncTask.INSERT_PASSWORD_DETAILS_REQUEST).execute(passwordDetails); }
    public void update(PasswordDetails passwordDetails) { new PasswordDetailsAsyncTask(passwordDetailsDao, PasswordDetailsAsyncTask.UPDATE_PASSWORD_DETAILS_REQUEST).execute(passwordDetails); }
    public void delete(PasswordDetails passwordDetails) { new PasswordDetailsAsyncTask(passwordDetailsDao, PasswordDetailsAsyncTask.DELETE_PASSWORD_DETAILS_REQUEST).execute(passwordDetails); }
    public LiveData<List<PasswordDetails>> getPasswordDetailsList() { return passwordDetailsList; }
}
