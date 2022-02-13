package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import sqip.Card;
import sqip.CardDetails;
import sqip.CardEntry;

public class AskCheckoutActivity extends AppCompatActivity implements View.OnClickListener {


    TextView txtSubmit;
    LinearLayout linearAskCheck;
    TextView txtCancel;
    TextView txtNext;
    ImageView imgBack;
    ImageView imgPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_checkout);
        txtSubmit = findViewById(R.id.txt_submit);
        txtCancel = findViewById(R.id.txt_cancel);
        txtNext = findViewById(R.id.txt_next);

        linearAskCheck = findViewById(R.id.dialog_ask);

        txtSubmit.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
    }

    private void startCheckOut() {
        String code = Preferences.getValue_String(this, Constants.BOOKING_CODE);
        Utils.showProgress(this);
        ApiAccessor.checkOut(code, new Callback<ResponseBody>() {
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
                                Toast.makeText(AskCheckoutActivity.this, getResources().getString(R.string.invalid_code_exist), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.INVALID_CODE_NOT_IN)) {
                                Toast.makeText(AskCheckoutActivity.this, getResources().getString(R.string.invalid_code_not_in), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(new Intent(AskCheckoutActivity.this, CheckoutComplteActivity.class));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AskCheckoutActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AskCheckoutActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(AskCheckoutActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                linearAskCheck.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_next:
                startCheckOut();
                //startActivity(new Intent(AskCheckoutActivity.this, CheckoutActivity.class));
                break;
            case R.id.txt_cancel:
                linearAskCheck.setVisibility(View.GONE);
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