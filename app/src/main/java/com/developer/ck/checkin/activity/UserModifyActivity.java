package com.developer.ck.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.adapter.UserAdapter;
import com.developer.ck.checkin.api.ApiAccessor;
import com.developer.ck.checkin.model.UserInfoModel;
import com.developer.ck.checkin.util.Constants;
import com.developer.ck.checkin.util.Preferences;
import com.developer.ck.checkin.util.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sqip.Card;
import sqip.CardDetails;
import sqip.CardEntry;

public class UserModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private final int DEFAULT_CARD_ENTRY_REQUEST_CODE = 101;
    ArrayList<UserInfoModel> userList = new ArrayList<>();
    TextView txtBack;
    TextView txtSubmit;
    ImageView imgBack;
    ImageView imgPhone;
    RecyclerView recyclerUser;
    UserAdapter userAdapter;

    LinearLayout dialogUser;
    TextView txtName;
    TextView txtSecondName;
    TextView txtEmail;
    TextView txtPhone;

    TextView txtCancel;
    TextView txtNext;

    TextView txtAdd;

    TextView txtMoney;
    TextView txtCancelPay;
    TextView txtRealPay;
    TextView txtPay;
    LinearLayout dialogPay;

    int userCount;
    int selectedId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);
        txtBack = findViewById(R.id.txt_back);
        txtBack.setOnClickListener(this);
        txtSubmit = findViewById(R.id.txt_submit);

        txtSubmit.setOnClickListener(this);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgPhone = findViewById(R.id.img_phone);
        imgPhone.setOnClickListener(this);

        dialogUser = findViewById(R.id.dialog_user);
        txtName = findViewById(R.id.txt_name);
        txtSecondName = findViewById(R.id.txt_second_name);
        txtEmail = findViewById(R.id.txt_email);
        txtPhone = findViewById(R.id.txt_phone);

        txtCancel = findViewById(R.id.txt_cancel);
        txtNext = findViewById(R.id.txt_next);
        txtAdd = findViewById(R.id.txt_add);

        txtAdd.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        dialogPay = findViewById(R.id.dialog_pay);
        txtMoney = findViewById(R.id.txt_money);
        txtPay = findViewById(R.id.txt_next_pay);
        txtCancelPay = findViewById(R.id.txt_cancel_pay);
        txtRealPay = findViewById(R.id.txt_real_pay);
        txtCancelPay.setOnClickListener(this);
        txtPay.setOnClickListener(this);
        txtRealPay.setOnClickListener(this);

        recyclerUser = findViewById(R.id.recycler_user);

        userCount = Preferences.getValue_Int(this, Constants.BOOKING_COUNT);

        String mainName = Preferences.getValue_String(this, Constants.BOOKING_NAME);
        String mainSecondName = Preferences.getValue_String(this, Constants.BOOKING_SECOND_NAME);
        String mainEmail = Preferences.getValue_String(this, Constants.BOOKING_EMAIL);
        String mainPhone = Preferences.getValue_String(this, Constants.BOOKING_PHONE);
        userList.add(new UserInfoModel(mainName, mainSecondName, mainEmail, mainPhone, getDate(), true, 1));
        checkAddStatus();
        userAdapter = new UserAdapter(this, userList);
        recyclerUser.setAdapter(userAdapter);
        recyclerUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void checkAddStatus() {
        if(userList.size() == userCount) {
            txtAdd.setText(getResources().getString(R.string.complete_check));
        } else {
            txtAdd.setText(getResources().getString(R.string.add_user));
        }
    }


    private String getDate() {
        String pattern = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    public void modifyUser(UserInfoModel model) {
        if(model != null) {
            selectedId = model.getId();
            txtName.setText(model.getName());
            txtSecondName.setText(model.getSecondName());
            txtEmail.setText(model.getEmail());
            txtPhone.setText(model.getPhone());
        } else {
            selectedId = 0;
            txtName.setText("");
            txtSecondName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
        }
        dialogUser.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        dialogUser.setVisibility(View.GONE);
    }

    private void showMoneyDialog(int money) {
        txtMoney.setText(getResources().getString(R.string.ask_pay_money).replace("xxx", String.valueOf(money)));
        dialogPay.setVisibility(View.VISIBLE);
    }

    private void hideMoneyDialog() {
        dialogPay.setVisibility(View.GONE);
    }

    public void changeUser() {
        String name = txtName.getText().toString();
        String secondName = txtSecondName.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        if(name.length() == 0) {
            Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.please_input_name), Toast.LENGTH_SHORT).show();
        } else {
            if(selectedId > 0) {
                for(int i = 0; i < userList.size(); i ++) {
                    if(userList.get(i).getId() == selectedId) {
                        userList.get(i).setName(name);
                        userList.get(i).setSecondName(secondName);
                        userList.get(i).setEmail(email);
                        userList.get(i).setPhone(phone);
                    }
                }
            } else {
                userList.add(new UserInfoModel(name, secondName, email, phone, getDate(), false, userList.size() + 1));
            }
        }
        hideDialog();
        checkAddStatus();
        userAdapter.notifyDataSetChanged();
    }

    private void moveComplete(String roomNo, String password) {
        Intent intent = new Intent(UserModifyActivity.this, CompleteActivity.class);
        intent.putExtra(Constants.ROOM_NUMBER, roomNo);
        intent.putExtra(Constants.ROOM_PASSWORD, password);
        startActivity(intent);
    }

    private void startPay() {
        hideMoneyDialog();
        CardEntry.startCardEntryActivity(UserModifyActivity.this, true,
                DEFAULT_CARD_ENTRY_REQUEST_CODE);
    }

    private void upload() {
        String json = new Gson().toJson(userList);
        String code = Preferences.getValue_String(this, Constants.BOOKING_CODE);
        Utils.showProgress(this);
        ApiAccessor.uploadUsers(code, json, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.closeProgress();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        JSONObject data = object.getJSONObject("data");
                        String roomNo = data.getString("room");
                        String password = data.getString("password");
                        Utils.saveWelComeMessage(UserModifyActivity.this, data);
                        int money = data.getInt("money");
                        if(money <= 0) {
                            moveComplete(roomNo, password);
                        } else {
                            showMoneyDialog(money);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_submit:
                upload();
                break;
            case R.id.txt_add:
                if(userCount > userList.size()) {
                    modifyUser(null);
                }
                break;
            case R.id.txt_cancel:
                hideDialog();
                break;
            case R.id.txt_next:
                changeUser();
                break;
            case R.id.txt_back:
            case R.id.img_back:
                finish();
                return ;
            case R.id.img_phone:
                Utils.callManager(this);
                return ;
            case R.id.txt_cancel_pay:
                hideMoneyDialog();
                return ;
            case R.id.txt_next_pay:
                startPay();
                return ;
            case R.id.txt_real_pay:
                Intent intent = new Intent(UserModifyActivity.this, PrivacyActivity.class);
                intent.putExtra(Constants.KEY_PAY_TYPE, 1);
                startActivity(intent);
                return ;
        }


    }

    private void payAmount(String sourceId) {
        String code = Preferences.getValue_String(this, Constants.BOOKING_CODE);
        Utils.showProgress(this);
        ApiAccessor.payAmount(code, sourceId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.closeProgress();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int status = object.getInt("status");
                        if(status == 0) {
                            String message = object.getString("message");
                            if(message.equals(Constants.Error.NOT_EMPTY_ROOM)) {
                                Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.other_check_room), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserModifyActivity.this,
                                        getResources().getString(R.string.failed_pay),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                        } else {
                            String languageCode = Preferences.getValue_String(UserModifyActivity.this, Constants.Build.LANGUAGE_CODE);
                            JSONObject data = object.getJSONObject("data");
                            String roomNo = data.getString("room");
                            String password = data.getString("password");
                            Utils.saveWelComeMessage(UserModifyActivity.this, data);
                            moveComplete(roomNo, password);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    // TODO : error
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgress();
                Toast.makeText(UserModifyActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEFAULT_CARD_ENTRY_REQUEST_CODE){
            CardEntry.handleActivityResult(data, result -> {
                if (result.isSuccess()) {
                    CardDetails cardResult = result.getSuccessValue();
                    Card card = cardResult.getCard();
                    String nonce = cardResult.getNonce();
                    payAmount(nonce);


                } else if (result.isCanceled()) {
                    Toast.makeText(UserModifyActivity.this,
                            getResources().getString(R.string.failed_card_input),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

    }
}