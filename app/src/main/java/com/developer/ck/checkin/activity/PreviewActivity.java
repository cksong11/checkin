package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    int checkType;
    TextView txtTitle;
    TextView txtSubmit;
    TextView txtBack;
    ImageView imgBack;
    ImageView imgPhone;
    TextView txtUserName;
    TextView txtUserCount;
    TextView txtBuildName;
    TextView txtRoomCount;
    TextView txtDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        txtSubmit = findViewById(R.id.txt_submit);
        txtSubmit.setOnClickListener(this);
        txtBack = findViewById(R.id.txt_back);
        txtTitle = findViewById(R.id.txt_title);
        txtBack.setOnClickListener(this);

        checkType = Preferences.getValue_Int(this, Constants.KEY_CHECK_TYPE);
        if(checkType == 2) {
            txtTitle.setText(getResources().getString(R.string.confirm_check_out));
        } else {
            txtTitle.setText(getResources().getString(R.string.confirm_check));
        }

        txtUserName = findViewById(R.id.txt_user_name);
        txtUserCount = findViewById(R.id.txt_user_count);
        txtBuildName = findViewById(R.id.txt_build_name);
        txtRoomCount = findViewById(R.id.txt_room_count);
        txtDuration = findViewById(R.id.txt_duration);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
        txtBuildName.setText(Preferences.getValue_String(this, Constants.Build.BUILD_NAME));
        getBookingInfo();
    }

    private void getBookingInfo() {
        String code = Preferences.getValue_String(this, Constants.BOOKING_CODE);
        Utils.showProgress(this);
        ApiAccessor.getBookingDetail(code, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.closeProgress();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int status = object.getInt("status");
                        if(status == 0) {
                            Toast.makeText(PreviewActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject bookInfo = object.getJSONObject("book");
                            String userName = bookInfo.getString("guest_kanji_name");
                            int totalCount = bookInfo.getInt("total_pax_count");
                            String checkInDate = bookInfo.getString("check_in_date");
                            String checkOutDate = bookInfo.getString("check_out_date");
                            txtUserName.setText(userName);
                            txtUserCount.setText(String.valueOf(totalCount));
                            txtDuration.setText(checkInDate + " ~ " + checkOutDate);
                            Preferences.setValue_String(PreviewActivity.this, Constants.BOOKING_NAME, userName);
                            String secondName = bookInfo.getString("guest_double_name");
                            String email = bookInfo.getString("guest_email");
                            String phone = bookInfo.getString("guest_phone");
                            Preferences.setValue_String(PreviewActivity.this, Constants.BOOKING_SECOND_NAME, secondName);
                            Preferences.setValue_String(PreviewActivity.this, Constants.BOOKING_PHONE, email);
                            Preferences.setValue_String(PreviewActivity.this, Constants.BOOKING_PHONE, phone);
                            Preferences.setValue_Int(PreviewActivity.this, Constants.BOOKING_COUNT, totalCount);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PreviewActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PreviewActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(PreviewActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                if(checkType == 1) {
                    startActivity(new Intent(PreviewActivity.this, WelcomeActivity.class));
                } else {
                    startActivity(new Intent(PreviewActivity.this, AskCheckoutActivity.class));
                }
                break;
            case R.id.img_back:
                finish();
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
            case R.id.txt_back:
                finish();
                return ;
        }
    }
}