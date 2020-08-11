package com.bazukaa.secured.viewmodelhelper;

import androidx.lifecycle.LiveData;

import com.bazukaa.secured.models.PasswordDetails;

import java.util.List;

public interface PasswordDetailsViewModelHelper {

    void insert(PasswordDetails passwordDetails);

    void update(PasswordDetails passwordDetails);

    void delete(PasswordDetails passwordDetails);

    LiveData<List<PasswordDetails>> getPasswordDetailsList();

}
