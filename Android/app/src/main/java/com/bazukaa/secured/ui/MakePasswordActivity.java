package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.bazukaa.secured.R;

import butterknife.BindView;

public class MakePasswordActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.bazukaa.secured.ui.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.bazukaa.secured.ui.EXTRA_DESC";
    public static final String EXTRA_PWD = "com.bazukaa.secured.ui.EXTRA_PWD";
    public static final String EXTRA_TIMESTAMP = "com.bazukaa.secured.ui.EXTRA_TIMESTAMP";

    @BindView((R.id.act_mk_pwd_et_title))
    EditText titleEditText;
    @BindView(R.id.act_mk_pwd_et_desc)
    EditText descEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_password);
    }
}
