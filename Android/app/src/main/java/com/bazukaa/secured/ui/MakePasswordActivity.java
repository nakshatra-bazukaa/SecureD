package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import com.bazukaa.secured.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakePasswordActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.bazukaa.secured.ui.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.bazukaa.secured.ui.EXTRA_DESC";
    public static final String EXTRA_PWD = "com.bazukaa.secured.ui.EXTRA_PWD";
    public static final String EXTRA_TIMESTAMP = "com.bazukaa.secured.ui.EXTRA_TIMESTAMP";

    @BindView((R.id.act_mk_pwd_et_title))
    EditText titleEditText;
    @BindView(R.id.act_mk_pwd_et_desc)
    EditText descEditText;
    @BindView(R.id.act_mk_pwd_et_len)
    EditText lengthEditText;
    @BindView(R.id.act_mk_pwd_cb)
    CheckBox checkBox;
    @BindView(R.id.act_mk_pwd_slider)
    SeekBar lengthSeekBar;
    @BindView(R.id.act_mk_pwd_switch_block_letter)
    SwitchMaterial blockLetterSwitch;
    @BindView(R.id.act_mk_pwd_switch_small_letter)
    SwitchMaterial smallLetterSwitch;
    @BindView(R.id.act_mk_pwd_switch_num)
    SwitchMaterial numSwitch;
    @BindView(R.id.act_mk_pwd_switch_spl_char)
    SwitchMaterial splCharSwitch;
    @BindView(R.id.act_mk_pwd_btn_gen_pwd)
    Button generatePwdButton;
    @BindView(R.id.act_mk_pwd_cl_checked)
    ConstraintLayout checkedView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_password);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.act_mk_pwd_cb)
    public void onCheckBoxClicked(){
        if(checkBox.isChecked() == true && checkedView.getVisibility() == View.GONE){
            checkedView.setVisibility(View.VISIBLE);
        }else if(checkBox.isChecked() == false && checkedView.getVisibility() == View.VISIBLE){
            checkedView.setVisibility(View.GONE);
        }
    }
    
}
