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

public class CheckoutComplteActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtSubmit;
    LinearLayout linearAskMoveTop;
    TextView txtCancel;
    TextView txtNext;
    ImageView imgBack;
    ImageView imgPhone;
    TextView txtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_complte);

        txtSubmit = findViewById(R.id.txt_submit);
        txtCancel = findViewById(R.id.txt_cancel);
        txtNext = findViewById(R.id.txt_next);

        linearAskMoveTop = findViewById(R.id.dialog_ask);

        txtSubmit.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);

        txtDescription = findViewById(R.id.txt_description);
        String userName = Preferences.getValue_String(this, Constants.BOOKING_NAME);
        txtDescription.setText(getResources().getString(R.string.description_finish_check).replace("xxx", userName)
                .replace("yyy", Preferences.getValue_String(this, Constants.Build.BUILD_NAME)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                linearAskMoveTop.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_next:
                startActivity(new Intent(CheckoutComplteActivity.this, LanguageActivity.class));
                break;
            case R.id.txt_cancel:
                linearAskMoveTop.setVisibility(View.GONE);
                break;
            case R.id.img_back:
                linearAskMoveTop.setVisibility(View.VISIBLE);
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
        }
    }
}