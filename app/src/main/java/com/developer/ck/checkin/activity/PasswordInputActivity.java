package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.adapter.KeyboardAdapter;
import com.developer.ck.checkin.adapter.PinAdapter;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.model.KeyboardModel;
import com.developer.ck.checkin.model.PinModel;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordInputActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerPin;
    RecyclerView recyclerKeyboard;
    TextView txtSubmit;

    ArrayList<PinModel> pinList;
    PinAdapter adapterPin;
    String travelCode = "";
    int pinLength = 6;
    ImageView imgBack;
    ImageView imgPhone;
    int checkType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_input);
        recyclerPin = findViewById(R.id.recycler_pin);
        recyclerKeyboard = findViewById(R.id.recycler_keyboard);
        txtSubmit = findViewById(R.id.txt_submit);
        txtSubmit.setEnabled(false);
        txtSubmit.setOnClickListener(this);
        createKeyboard();
        createPin();

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
        checkType = Preferences.getValue_Int(this, Constants.KEY_CHECK_TYPE);
    }

    private void createKeyboard() {
        ArrayList<KeyboardModel> keyboardModelArrayList = new ArrayList<>();
        for(int i = 1; i <= 9; i ++) {
            keyboardModelArrayList.add(new KeyboardModel(i));
        }
        keyboardModelArrayList.add(new KeyboardModel(0));
        keyboardModelArrayList.add(new KeyboardModel(-1));
        KeyboardAdapter adapter = new KeyboardAdapter(this, keyboardModelArrayList);
        recyclerKeyboard.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 9) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerKeyboard.setLayoutManager(gridLayoutManager);
    }



    public int getScreenWidth() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void createPin() {
        pinList = new ArrayList<>();
        for(int i = 0; i < 6; i ++) {
            if(i == 0) {
                pinList.add(new PinModel("", true));
            } else {
                pinList.add(new PinModel("", false));
            }
        }
        float width = getScreenWidth() - getResources().getDimension(R.dimen.dimen_30) * 2;
        adapterPin = new PinAdapter(this, pinList, (int) (width / pinList.size()));
        recyclerPin.setAdapter(adapterPin);
        recyclerPin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void changeCode() {
        int length = travelCode.length();
        for(int i = 0; i < 6; i ++) {
            PinModel pinModel = pinList.get(i);
            if(i > length) {
                pinModel.setStatus(false);
                pinModel.setValue("");
            } else if(i == length) {
                pinModel.setStatus(true);
                pinModel.setValue("");
            } else {
                pinModel.setStatus(false);
                pinModel.setValue("" + travelCode.charAt(i));
            }
        }
        adapterPin.notifyDataSetChanged();
        if(travelCode.length() == pinLength) {
            txtSubmit.setEnabled(true);
            txtSubmit.setBackground(getResources().getDrawable(R.drawable.btn_language));
        } else {
            txtSubmit.setEnabled(false);
            txtSubmit.setBackground(getResources().getDrawable(R.drawable.btn_gray_round));
        }
    }

    public void clickKeyboard(int value) {
        if(value == -1) {
            if(travelCode.length() > 0) {
                travelCode = travelCode.substring(0, travelCode.length() - 1);
            }
        } else if(travelCode.length() < pinLength) {
            travelCode = travelCode + value;
        }
        changeCode();
    }

    private void movePreview(String code) {
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
                                Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.invalid_code_exist), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.INVALID_CODE_OUT)) {
                                Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.invalid_code_out), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int checkStatus = object.getInt("check_status");
                            int roomStatus = object.getInt("room_status");
                            if(checkStatus == 1 && checkType == 1) {
                                Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.invalid_code_in), Toast.LENGTH_SHORT).show();
                            } else if(roomStatus == 0 && checkType == 1) {
                                Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.not_empty_room), Toast.LENGTH_SHORT).show();
                            } else if(checkStatus == 0 && checkType == 2) {
                                Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.invalid_code_out), Toast.LENGTH_SHORT).show();
                            } else {
                                Preferences.setValue_String(PasswordInputActivity.this, Constants.BOOKING_CODE, code);
                                startActivity(new Intent(PasswordInputActivity.this, PreviewActivity.class));
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(PasswordInputActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                movePreview(travelCode);
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