package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtStart;
    TextView txtAcceptPermission;
    LinearLayout errorPermissionDialog;
    TextView txtLoading;
    LinearLayout passwordDialog;
    TextView txtCancel;
    TextView txtNext;
    EditText txtPassword;
    TextView txtUUID;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    double latitude;
    double longitude;

    protected String[] needPermissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA",
            "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.CALL_PHONE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        txtStart = findViewById(R.id.txt_start);
        txtAcceptPermission = findViewById(R.id.txt_accept_permission);
        txtLoading = findViewById(R.id.txt_permission_loading);
        errorPermissionDialog = findViewById(R.id.dialog_permission);

        txtAcceptPermission.setOnClickListener(this);
        txtStart.setOnClickListener(this);

        passwordDialog = findViewById(R.id.dialog_password);
        txtCancel = findViewById(R.id.txt_cancel);
        txtNext = findViewById(R.id.txt_next);

        txtCancel.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        txtPassword = findViewById(R.id.txt_password);

        if (checkPermissions(this.needPermissions)) {
            showStartButton();

        }
        txtUUID = findViewById(R.id.txt_uuid);
        txtUUID.setText(Utils.getUUID(this));
    }

    private void showPermissionDialog() {
        txtAcceptPermission.setVisibility(View.VISIBLE);
        txtLoading.setVisibility(View.VISIBLE);
        txtStart.setVisibility(View.GONE);
        errorPermissionDialog.setVisibility(View.VISIBLE);
        passwordDialog.setVisibility(View.GONE);
    }

    private void hidePermissionDialog() {
        txtAcceptPermission.setVisibility(View.GONE);
        txtLoading.setVisibility(View.VISIBLE);
        txtStart.setVisibility(View.GONE);
        errorPermissionDialog.setVisibility(View.GONE);
        passwordDialog.setVisibility(View.GONE);
    }

    private void showStartButton() {
        errorPermissionDialog.setVisibility(View.GONE);
        txtAcceptPermission.setVisibility(View.GONE);
        txtStart.setVisibility(View.VISIBLE);
        txtLoading.setVisibility(View.GONE);
        passwordDialog.setVisibility(View.GONE);
    }

    private void showPasswordDialog() {
        passwordDialog.setVisibility(View.VISIBLE);
    }

    @Override // android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == 0 && !verifyPermissions(paramArrayOfInt)) {
            showPermissionDialog();
        } else {
            showStartButton();
        }
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
    }

    private boolean checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (needRequestPermissonList != null && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(PermissionActivity.this, (String[]) needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), 0);
            return false;
        }
        startLocationManager();
        return true;
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void startLocationManager() {
        Location location = getLastKnownLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(PermissionActivity.this, perm) != 0) {
                needRequestPermissonList.add(perm);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    private void moveLanguage(String userId, String password) {
        //Preferences.setValue_String(PermissionActivity.this, Constants.BUILD_NAME, getResources().getString(R.string.company_name));
        Preferences.setValue_String(PermissionActivity.this, Constants.USER_ID, userId);
        Preferences.setValue_String(PermissionActivity.this, Constants.USER_PASSWORD, password);
        startActivity(new Intent(PermissionActivity.this, LanguageActivity.class));
    }

//    private void moveLanguage() {
//        String deviceId = Utils.getUUID(this);
//        String password = txtPassword.getText().toString();
//        Utils.showProgress(this);
//        ApiAccessor.getCheckIn(deviceId, password, latitude, longitude, new Callback<GetCheckInResult>() {
//            @Override
//            public void onResponse(Call<GetCheckInResult> call, Response<GetCheckInResult> response) {
//                Utils.closeProgress();
//                if (response.isSuccessful()) {
//                    GetCheckInResult result = response.body();
//                    if(result.status == 0) {
//                        if(result.message.equals("invalid_device")) {
//                            Toast.makeText(PermissionActivity.this, getResources().getString(R.string.invalid_device), Toast.LENGTH_SHORT).show();
//                        } else if(result.message.equals("invalid_password")) {
//                            Toast.makeText(PermissionActivity.this, getResources().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
//                        } else if(result.message.equals("invalid_position")) {
//                            Toast.makeText(PermissionActivity.this, getResources().getString(R.string.invalid_position), Toast.LENGTH_SHORT).show();
//                        }
//                    } else if(result.status == 1) {
//                        Preferences.setValue_String(PermissionActivity.this, Constants.USER_ID, result.info.userId);
//                        Preferences.setValue_String(PermissionActivity.this, Constants.USER_PASSWORD, result.info.userPassword);
//                        startActivity(new Intent(PermissionActivity.this, LanguageActivity.class));
//                    }
//                } else {
//                    Toast.makeText(PermissionActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
//                    // TODO : error
//                }
//            }
//            @Override
//            public void onFailure(Call<GetCheckInResult> call, Throwable t) {
//                Utils.closeProgress();
//                Toast.makeText(PermissionActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_start:
                moveLanguage(Constants.API_USER_ID, Constants.API_USER_PASSWORD);
                //showPasswordDialog();
                break;
            case R.id.txt_accept_permission:
                hidePermissionDialog();
                checkPermissions(this.needPermissions);
                break;
            case R.id.txt_cancel:
                showStartButton();
                break;
            case R.id.txt_next:
                //moveLanguage();
                break;
        }
    }


}