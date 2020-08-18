package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bazukaa.secured.R;
import com.bazukaa.secured.util.PasswordGenerator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakePasswordActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.bazukaa.secured.ui.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.bazukaa.secured.ui.EXTRA_DESC";
    public static final String EXTRA_PWD = "com.bazukaa.secured.ui.EXTRA_PWD";
    public static final String EXTRA_TIMESTAMP = "com.bazukaa.secured.ui.EXTRA_TIMESTAMP";

    public static final int DEFAULT_PWD_LENGTH = 24;

    @BindView((R.id.act_mk_pwd_et_title))
    EditText titleEditText;
    @BindView(R.id.act_mk_pwd_et_desc)
    EditText descEditText;
    @BindView(R.id.act_mk_pwd_tv_len)
    TextView lengthTextView;
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
        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lengthTextView.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    // To handle click on checkbox
    @OnClick(R.id.act_mk_pwd_cb)
    public void onCheckBoxClicked(){
        if(checkBox.isChecked() == true && checkedView.getVisibility() == View.GONE){
            checkedView.setVisibility(View.VISIBLE);
        }else if(checkBox.isChecked() == false && checkedView.getVisibility() == View.VISIBLE){
            checkedView.setVisibility(View.GONE);
        }
    }

    // To process and save the generated password
    private void savePassword(){
        String generatedPwd = null;
        if(checkBox.isChecked() == true){
            boolean blockLetters = blockLetterSwitch.isChecked();
            boolean smallLetters = smallLetterSwitch.isChecked();
            boolean numeric = numSwitch.isChecked();
            boolean splChars = splCharSwitch.isChecked();
            int len = Integer.parseInt(lengthTextView.getText().toString());

            generatedPwd = PasswordGenerator.generateCustomPassword(blockLetters, smallLetters, splChars, numeric, len);
        }else{
            generatedPwd = PasswordGenerator.generateDefaultPassword(DEFAULT_PWD_LENGTH);
        }
        String title = titleEditText.getText().toString();
        String desc = descEditText.getText().toString();
        long timeStamp = System.currentTimeMillis();

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, desc);
        data.putExtra(EXTRA_PWD, generatedPwd);
        data.putExtra(EXTRA_TIMESTAMP, timeStamp);

        setResult(RESULT_OK, data);
        finish();
    }

    @OnClick(R.id.act_mk_pwd_btn_gen_pwd)
    public void onGeneratePwdButtonClicked(){
        savePassword();
    }
}
