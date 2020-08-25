package com.bazukaa.secured.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bazukaa.secured.models.PasswordDetails;
import com.bazukaa.secured.repository.PasswordDetailsRepository;
import com.bazukaa.secured.viewmodelhelper.PasswordDetailsViewModelHelper;

import java.util.List;

public class PasswordDetailsViewModel extends AndroidViewModel implements PasswordDetailsViewModelHelper {
    private PasswordDetailsRepository passwordDetailsRepository;
    private LiveData<List<PasswordDetails>> passwordDetailsList;

    public PasswordDetailsViewModel(@NonNull Application application) {
        super(application);

        passwordDetailsRepository = new PasswordDetailsRepository(application);
        passwordDetailsList = passwordDetailsRepository.getPasswordDetailsList();
    }

    @Override
    public void insert(PasswordDetails passwordDetails) {
        passwordDetailsRepository.insert(passwordDetails);
    }

    @Override
    public void update(PasswordDetails passwordDetails) {
        passwordDetailsRepository.update(passwordDetails);
    }

    @Override
    public void delete(PasswordDetails passwordDetails) {
        passwordDetailsRepository.delete(passwordDetails);
    }

    @Override
    public void deleteAllPasswords() {
        passwordDetailsRepository.deleteAllPasswords();
    }

    @Override
    public LiveData<List<PasswordDetails>> getPasswordDetailsList() {
        return passwordDetailsList;
    }
}
