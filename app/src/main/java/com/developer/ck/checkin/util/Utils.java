package com.developer.ck.checkin.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.activity.UserModifyActivity;

import org.json.JSONObject;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    private static final String PREF_NAME = "CheckIn";
    private static ProgressDialog progressDialog;
    public static void showProgress(Context ctx) {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(ctx.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void closeProgress() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    public static String getLastDigits(String str, int number) {
        int len = str.length();
        if(len < number) {
            String answer = str;
            for(int i = 0; i < number - len; i ++) {
                answer = "0" + answer;
            }
            return answer;
        }
        return str.substring(len - number);
    }

    public static String getTwoDigit(int number) {
        if(number < 10) {
            return "0" + number;
        }
        return "" + number;
    }

    public static String getFormateDate(int year, int month, int day) {
        return year + "-" + getTwoDigit(month) + "-" + getTwoDigit(day);
    }

    public static String convertIntArrayCommaString(int[] arr) {
        return IntStream.of(arr)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
    }

    public static String getUUID(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static void callManager(Context ctx) {
        String managerPhoneNumber = "+81 08042346544";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + managerPhoneNumber));
        ctx.startActivity(callIntent);
    }

    public static void saveWelComeMessage(Context ctx, JSONObject data) {
        try {
            String messageEN = data.getString("message_en");
            String messageJP = data.getString("message_jp");
            String messageKO = data.getString("message_ko");
            String messageCN = data.getString("message_cn");
            Preferences.setValue_String(ctx, Constants.Room.ROOM_WELCOME_EN, messageEN);
            Preferences.setValue_String(ctx, Constants.Room.ROOM_WELCOME_JP, messageJP);
            Preferences.setValue_String(ctx, Constants.Room.ROOM_WELCOME_KO, messageKO);
            Preferences.setValue_String(ctx, Constants.Room.ROOM_WELCOME_CN, messageCN);
        } catch (Exception ex) {

        }
    }
}
