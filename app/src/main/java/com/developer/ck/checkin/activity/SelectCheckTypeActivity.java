package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

public class SelectCheckTypeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout checkIn;
    LinearLayout checkOut;
    ImageView imgBack;
    ImageView imgPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_check_type);
        checkIn = findViewById(R.id.check_in);
        checkOut = findViewById(R.id.check_out);

        checkIn.setOnClickListener(this);
        checkOut.setOnClickListener(this);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_in:
                Preferences.setValue_Int(this, Constants.KEY_CHECK_TYPE, 1);
                break;
            case R.id.check_out:
                Preferences.setValue_Int(this, Constants.KEY_CHECK_TYPE, 2);
                break;
            case R.id.img_back:
                finish();
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
        }
        startActivity(new Intent(SelectCheckTypeActivity.this, PasswordInputTextActivity.class));
    }
}