package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.util.Utils;

import java.util.ArrayList;
import android.widget.ArrayAdapter;


public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtPhone;
    EditText txtGuest;
    Spinner spinnerMonth;
    Spinner spinnerDay;
    TextView txtSubmit;
    TextView txtCall;
    ImageView imgBack;
    ImageView imgPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        txtPhone = findViewById(R.id.txt_phone);
        txtGuest = findViewById(R.id.txt_guest);

        spinnerMonth = findViewById(R.id.spinner_month);
        spinnerDay = findViewById(R.id.spinner_day);

        txtSubmit = findViewById(R.id.txt_submit);
        txtCall = findViewById(R.id.txt_call);

        txtSubmit.setOnClickListener(this);
        txtCall.setOnClickListener(this);
        createMonth();
        createDay();

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);
    }

    private void createMonth() {

        ArrayList<Integer> months = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }

        ArrayAdapter<Integer> adapter =
                new ArrayAdapter<Integer>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, months);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth.setAdapter(adapter);
    }

    private void createDay() {

        ArrayList<Integer> days = new ArrayList<>();

        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }

        ArrayAdapter<Integer> adapter =
                new ArrayAdapter<Integer>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, days);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerDay.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                startActivity(new Intent(CreateActivity.this, PreviewActivity.class));
                break;
            case R.id.txt_call:
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