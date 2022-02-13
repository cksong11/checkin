package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

public class SelectSignTypeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearPassword;
    LinearLayout linearQrCode;
    TextView txtSubmit;
    int checkType;
    ImageView imgBack;
    ImageView imgPhone;
    TextView txtBuildName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sign_type);
        linearPassword = findViewById(R.id.linear_password);
        linearQrCode = findViewById(R.id.linear_qrcode);
        txtSubmit = findViewById(R.id.txt_submit);

        checkType = Preferences.getValue_Int(this, Constants.KEY_CHECK_TYPE);

        if(checkType == 2) {
            txtSubmit.setVisibility(View.GONE);
        }

        linearPassword.setOnClickListener(this);
        linearQrCode.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
        txtBuildName = findViewById(R.id.txt_build_name);
        txtBuildName.setText(Preferences.getValue_String(this, Constants.Build.BUILD_NAME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_password:
                startActivity(new Intent(SelectSignTypeActivity.this, PasswordInputActivity.class));
                break;
            case R.id.linear_qrcode:
                startActivity(new Intent(SelectSignTypeActivity.this, QrcodeInputActivity.class));
                break;
            case R.id.txt_submit:
                startActivity(new Intent(SelectSignTypeActivity.this, CreateActivity.class));
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
        }
    }
}