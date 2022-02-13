package com.developer.ck.checkin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.QRCodeFoundListener;
import com.developer.ck.checkin.util.QRCodeImageAnalyzer;
import com.developer.ck.checkin.util.Utils;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrcodeInputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private String qrCode;
    private String previousQRCode = "";
    ImageView imgBack;
    ImageView imgPhone;
    int checkType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_input);

        previewView = findViewById(R.id.previewView);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        requestCamera();

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
        checkType = Preferences.getValue_Int(this, Constants.KEY_CHECK_TYPE);
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(QrcodeInputActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCode = _qrCode;
                if(!qrCode.equals(previousQRCode)) {
                    previousQRCode = qrCode;
                    movePreview();
                }

                //qrCodeFoundButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void qrCodeNotFound() {
                qrCode = "";
                //qrCodeFoundButton.setVisibility(View.INVISIBLE);
            }
        }));

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageAnalysis, preview);
    }

    private void movePreview() {
        String code = Utils.getLastDigits(qrCode, 6);
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
                                Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.invalid_code_exist), Toast.LENGTH_SHORT).show();
                            } else if(message.equals(Constants.Error.INVALID_CODE_OUT)) {
                                Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.invalid_code_out), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int checkStatus = object.getInt("check_status");
                            int roomStatus = object.getInt("room_status");
                            if (checkStatus == 1 && checkType == 1) {
                                Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.invalid_code_in), Toast.LENGTH_SHORT).show();
                            } else if (roomStatus == 0 && checkType == 1) {
                                Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.not_empty_room), Toast.LENGTH_SHORT).show();
                            } else if (checkStatus == 0 && checkType == 2) {
                                Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.invalid_code_out), Toast.LENGTH_SHORT).show();
                            } else {
                                Preferences.setValue_String(QrcodeInputActivity.this, Constants.BOOKING_CODE, code);
                                startActivity(new Intent(QrcodeInputActivity.this, PreviewActivity.class));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(QrcodeInputActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
        }
    }
}