package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

import org.json.JSONObject;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtJapan;
    TextView txtChina;
    TextView txtEnglish;
    TextView txtKorea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        txtJapan = findViewById(R.id.txt_japan);
        txtEnglish = findViewById(R.id.txt_english);
        txtChina = findViewById(R.id.txt_china);
        txtKorea = findViewById(R.id.txt_korea);

        txtJapan.setOnClickListener(this);
        txtEnglish.setOnClickListener(this);
        txtChina.setOnClickListener(this);
        txtKorea.setOnClickListener(this);
    }

    private void moveCheckType(String languageCode) {
        String uuid = Utils.getUUID(this);
        Utils.showProgress(this);
        ApiAccessor.getDeviceInfo(uuid, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.closeProgress();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int status = object.getInt("status");
                        if(status == 1) {
                            JSONObject buildInfo = object.getJSONObject("build");
                            int id = buildInfo.getInt("id");
                            String name = buildInfo.getString("name");

                            String welcomeMessage = "";
                            String privacyMessage = "";
                            String moneyPrivacyMessage = "";
                            welcomeMessage = buildInfo.getString("message_welcome_"+ languageCode);
                            privacyMessage = buildInfo.getString("message_privacy_" + languageCode);
                            moneyPrivacyMessage = buildInfo.getString("message_money_privacy_" + languageCode);

                            Locale locale = new Locale(languageCode);
                            Locale.setDefault(locale);
                            Resources resources = getResources();
                            Configuration config = resources.getConfiguration();
                            config.setLocale(locale);
                            resources.updateConfiguration(config, resources.getDisplayMetrics());
                            //id = 18;

                            Preferences.setValue_Int(LanguageActivity.this, Constants.Build.BUILD_ID, id);
                            Preferences.setValue_String(LanguageActivity.this, Constants.Build.BUILD_NAME, name);
                            Preferences.setValue_String(LanguageActivity.this, Constants.Build.MESSAGE_WELCOME, welcomeMessage);
                            Preferences.setValue_String(LanguageActivity.this, Constants.Build.MESSAGE_PRIVACY, privacyMessage);
                            Preferences.setValue_String(LanguageActivity.this, Constants.Build.MESSAGE_MONEY_PRIVACY, moneyPrivacyMessage);
                            Preferences.setValue_String(LanguageActivity.this, Constants.Build.LANGUAGE_CODE, languageCode);
                            startActivity(new Intent(LanguageActivity.this, SelectCheckTypeActivity.class));
                        } else {
                            Toast.makeText(LanguageActivity.this, getResources().getString(R.string.invalid_device), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LanguageActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LanguageActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(LanguageActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        String languageCode = "";
        switch (v.getId()) {
            case R.id.txt_japan:
                languageCode = "jp";
                break;
            case R.id.txt_english:
                languageCode = "en";
                break;
            case R.id.txt_china:
                languageCode = "cn";
                break;
            case R.id.txt_korea:
                languageCode = "ko";
                break;
        }
        moveCheckType(languageCode);
    }

    @Override
    public void onBackPressed() { }
}