package com.developer.ck.checkin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.adapter.PinAdapter;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.model.PinModel;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordInputTextActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtCode;
    TextView txtSubmit;

    ArrayList<PinModel> pinList;
    PinAdapter adapterPin;
    String password = "";
    int pinLength = 6;
    ImageView imgBack;
    ImageView imgPhone;
    int checkType;
    Thread timerThread;
    Date lastActiveDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_input_text);
        txtCode = findViewById(R.id.txt_code);
        txtSubmit = findViewById(R.id.txt_submit);
        txtSubmit.setOnClickListener(this);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
        checkType = Preferences.getValue_Int(this, Constants.KEY_CHECK_TYPE);
        if(checkType == 1) {
            txtSubmit.setText(getResources().getString(R.string.title_check_in));
        } else {
            txtSubmit.setText(getResources().getString(R.string.title_check_out));
        }
        startTimer();
        txtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastActiveDate = new Date();
            }
        });
        txtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lastActiveDate = new Date();
            }
        });
    }

    private void startTimer() {
        lastActiveDate = new Date();
        timerThread = null;
        Runnable runnable = new CountDownRunner();
        timerThread= new Thread(runnable);
        timerThread.start();
    }

    private void stopTimer() {
        if(timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
    }

    private void checkActiveTime() {
        Date date = new Date();
        long diffInMillies = Math.abs(date.getTime() - lastActiveDate.getTime());
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if(diff >= 2 * 60) {
            stopTimer();
            startActivity(new Intent(PasswordInputTextActivity.this, LanguageActivity.class));
        }
    }

    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    checkActiveTime();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    private void movePreview() {
        lastActiveDate = new Date();
        String code = txtCode.getText().toString();
        Utils.showProgress(this);
        int buildId = Preferences.getValue_Int(this, Constants.Build.BUILD_ID);
        ApiAccessor.checkTravelCode(code, buildId, new Callback<ResponseBody>() {
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
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.invalid_code_exist), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.INVALID_CODE_OUT)) {
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.invalid_code_out), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.INVALID_BUILD)) {
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.invalid_build), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int checkStatus = object.getInt("check_status");
                            int roomStatus = object.getInt("room_status");
                            int validCheckInStatus = object.getInt("valid_check_in");
                            if(checkStatus == 1 && checkType == 1) {
                                JSONObject data = object.getJSONObject("data");
                                String roomNo = data.getString("room");
                                String password = data.getString("password");
                                Utils.saveWelComeMessage(PasswordInputTextActivity.this, data);
                                Intent intent = new Intent(PasswordInputTextActivity.this, CompleteActivity.class);
                                intent.putExtra(Constants.ROOM_NUMBER, roomNo);
                                intent.putExtra(Constants.ROOM_PASSWORD, password);
                                startActivity(intent);
                            } else if(roomStatus == 0 && checkType == 1) {
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.not_setting_room), Toast.LENGTH_SHORT).show();
                            } else if(roomStatus == 1 && checkType == 1) {
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.other_check_room), Toast.LENGTH_SHORT).show();
                            } else if(checkStatus == 0 && checkType == 2) {
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.invalid_code_out), Toast.LENGTH_SHORT).show();
                            } else if(validCheckInStatus == 1 && checkType == 1) {
                                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.invalid_time_in), Toast.LENGTH_SHORT).show();
                            } else {
                                stopTimer();
                                Preferences.setValue_String(PasswordInputTextActivity.this, Constants.BOOKING_CODE, code);
                                startActivity(new Intent(PasswordInputTextActivity.this, PreviewActivity.class));
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(PasswordInputTextActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                movePreview();
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