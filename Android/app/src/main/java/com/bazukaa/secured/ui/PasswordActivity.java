package com.bazukaa.secured.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bazukaa.secured.R;
import com.bazukaa.secured.adapters.PasswordAdapter;
import com.bazukaa.secured.models.PasswordDetails;
import com.bazukaa.secured.viewmodel.PasswordDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordActivity extends AppCompatActivity {

    public static final int ADD_PASSWORD_REQUEST = 1;

    private PasswordDetailsViewModel passwordDetailsViewModel;

    private Toolbar toolbar;
    @BindView(R.id.act_pwd_rv)
    RecyclerView passwordDetailsRecyclerView;
    @BindView(R.id.act_pwd_fab_add)
    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);

        // Setting up toolbar
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        // Setting up recyclerview
        final PasswordAdapter adapter = new PasswordAdapter();
        passwordDetailsRecyclerView.setAdapter(adapter);
        passwordDetailsViewModel = ViewModelProviders.of(this).get(PasswordDetailsViewModel.class);
        passwordDetailsViewModel.getPasswordDetailsList().observe(this, new Observer<List<PasswordDetails>>() {
            @Override
            public void onChanged(List<PasswordDetails> passwordDetails) {
                adapter.setPasswords(passwordDetails);
            }
        });
        adapter.setOnItemClickListener(new PasswordAdapter.OnItemClickListener() {
            // To delete a password
            @Override
            public void onDeleteButtonClick(int position) {
                PasswordDetails passwordDetails = adapter.getPasswordDetailsFromPosition(position);
                passwordDetailsViewModel.delete(passwordDetails);
                adapter.notifyItemChanged(position);
            }
            // To copy pwd to clip board
            @Override
            public void onPwdTvClicked(int position) {
                PasswordDetails passwordDetails = adapter.getPasswordDetailsFromPosition(position);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password copied to clipboard", passwordDetails.getPassword());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(PasswordActivity.this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // To create a new password
    @OnClick(R.id.act_pwd_fab_add)
    public void onFabClicked(){
        Intent intent = new Intent(PasswordActivity.this, MakePasswordActivity.class);
        startActivityForResult(intent, ADD_PASSWORD_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_PASSWORD_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(MakePasswordActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(MakePasswordActivity.EXTRA_DESC);
            String pwd = data.getStringExtra(MakePasswordActivity.EXTRA_PWD);
            long timeStamp = data.getLongExtra(MakePasswordActivity.EXTRA_TIMESTAMP, 10000);

            PasswordDetails passwordDetails = new PasswordDetails(title, desc, pwd, timeStamp);
            passwordDetailsViewModel.insert(passwordDetails);

            Toast.makeText(this, "Password Generated Successfully", Toast.LENGTH_SHORT).show();
        }else if(requestCode == ADD_PASSWORD_REQUEST){
            Toast.makeText(this, "Password Not Generated", Toast.LENGTH_SHORT).show();
        }
    }
}
