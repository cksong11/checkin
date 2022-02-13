package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import com.developer.ck.checkin.R;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack;
    ImageView imgPhone;
    TextView txtSubmit;
    TextView txtUserName;
    TextView txtWelcome;
    TextView txtBuildName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        txtSubmit = findViewById(R.id.txt_submit);
        txtSubmit.setOnClickListener(this);

        txtUserName = findViewById(R.id.txt_user_name);
        String userName = Preferences.getValue_String(WelcomeActivity.this, Constants.BOOKING_NAME);
        txtUserName.setText(userName);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);

        txtWelcome = findViewById(R.id.txt_welcome);
        txtBuildName = findViewById(R.id.txt_build_name);
        txtWelcome.setText(Preferences.getValue_String(this, Constants.Build.MESSAGE_WELCOME));
        txtBuildName.setText(Preferences.getValue_String(this, Constants.Build.BUILD_NAME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                startActivity(new Intent(WelcomeActivity.this, PrivacyActivity.class));
                break;
            case R.id.img_back:
                finish();
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
        }
    }
}