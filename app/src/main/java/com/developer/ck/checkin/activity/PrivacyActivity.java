package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;


import com.developer.ck.checkin.R;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtSubmit;
    ImageView imgBack;
    ImageView imgPhone;
    TextView txtBack;
    CheckBox chkAccept;
    TextView txtPrivacy;
    int status = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        txtSubmit = findViewById(R.id.txt_submit);
        txtSubmit.setOnClickListener(this);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
        txtBack = findViewById(R.id.txt_back);
        txtBack.setOnClickListener(this);
        chkAccept = findViewById(R.id.chk_accept);
        status = getIntent().getIntExtra(Constants.KEY_PAY_TYPE, 0);
        txtPrivacy = findViewById(R.id.txt_privacy);
        if(status == 1) {
            txtPrivacy.setText(Preferences.getValue_String(this, Constants.Build.MESSAGE_MONEY_PRIVACY));
        } else {
            txtPrivacy.setText(Preferences.getValue_String(this, Constants.Build.MESSAGE_PRIVACY));
        }

    }

    private void moveComplete(String roomNo, String password) {
        Intent intent = new Intent(PrivacyActivity.this, CompleteActivity.class);
        intent.putExtra(Constants.ROOM_NUMBER, roomNo);
        intent.putExtra(Constants.ROOM_PASSWORD, password);
        startActivity(intent);
    }

    private void startCheckMoney() {
        String code = Preferences.getValue_String(this, Constants.BOOKING_CODE);
        Utils.showProgress(this);
        ApiAccessor.checkMoney(code, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.closeProgress();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int status = object.getInt("status");
                        if(status == 0) {
                            String message = object.getString("message");
                            if(message.equals(Constants.Error.INVALID_CODE_EXIST)) {
                                Toast.makeText(PrivacyActivity.this, getResources().getString(R.string.invalid_code_exist), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.INVALID_CODE_NOT_IN)) {
                                Toast.makeText(PrivacyActivity.this, getResources().getString(R.string.invalid_code_not_in), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.NOT_EMPTY_ROOM)) {
                                Toast.makeText(PrivacyActivity.this, getResources().getString(R.string.other_check_room), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            JSONObject data = object.getJSONObject("data");
                            String roomNo = data.getString("room");
                            String password = data.getString("password");
                            Utils.saveWelComeMessage(PrivacyActivity.this, data);
                            moveComplete(roomNo, password);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PrivacyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PrivacyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(PrivacyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                if(chkAccept.isChecked()) {
                    if(status == 1) {
                        startCheckMoney();
                    } else {
                        startActivity(new Intent(PrivacyActivity.this, CameraActivity.class));
                    }

                }

                break;
            case R.id.img_back:
            case R.id.txt_back:
                finish();
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
        }
    }
}