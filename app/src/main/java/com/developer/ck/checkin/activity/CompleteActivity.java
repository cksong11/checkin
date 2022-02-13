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

public class CompleteActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtMoveTop;
    ImageView imgBack;
    ImageView imgPhone;

    TextView txtRoomNo;
    TextView txtRoomPassword;
    TextView txtRoomDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        txtMoveTop = findViewById(R.id.txt_move_top);
        txtMoveTop.setOnClickListener(this);
        String roomNo = getIntent().getStringExtra(Constants.ROOM_NUMBER);
        String roomPassword = getIntent().getStringExtra(Constants.ROOM_PASSWORD);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);

        txtRoomNo = findViewById(R.id.txt_room_no);
        txtRoomPassword = findViewById(R.id.txt_room_password);
        txtRoomDescription = findViewById(R.id.txt_room_description);

        txtRoomNo.setText(roomNo);
        txtRoomPassword.setText(roomPassword);
        String languageCode = Preferences.getValue_String(this, Constants.Build.LANGUAGE_CODE);
        if(languageCode.equals("jp")) {
            txtRoomDescription.setText(Preferences.getValue_String(this, Constants.Room.ROOM_WELCOME_JP)
                    .replace(Constants.Room.ROOM_KEY_REPLACE_JP, roomPassword));
        } else if(languageCode.equals("en")) {
            txtRoomDescription.setText(Preferences.getValue_String(this, Constants.Room.ROOM_WELCOME_EN)
                    .replace(Constants.Room.ROOM_KEY_REPLACE_EN, roomPassword));
        } else if(languageCode.equals("ko")) {
            txtRoomDescription.setText(Preferences.getValue_String(this, Constants.Room.ROOM_WELCOME_KO)
                    .replace(Constants.Room.ROOM_KEY_REPLACE_KO, roomPassword));
        } else if(languageCode.equals("cn")) {
            txtRoomDescription.setText(Preferences.getValue_String(this, Constants.Room.ROOM_WELCOME_CN)
                    .replace(Constants.Room.ROOM_KEY_REPLACE_CN, roomPassword));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_move_top:
                startActivity(new Intent(CompleteActivity.this, LanguageActivity.class));
                //startActivity(new Intent(CompleteActivity.this, LanguageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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