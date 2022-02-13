package com.developer.ck.checkin.api;

import com.developer.ck.checkin.api.service.CheckCodeService;
import com.developer.ck.checkin.api.service.CheckMoneyService;
import com.developer.ck.checkin.api.service.CheckOutService;
import com.developer.ck.checkin.api.service.GetBookingService;
import com.developer.ck.checkin.api.service.GetBuildInfoService;
import com.developer.ck.checkin.api.service.GetCheckInService;
import com.developer.ck.checkin.api.service.PayAmountService;
import com.developer.ck.checkin.api.service.UploadRoomImageService;
import com.developer.ck.checkin.api.service.UploadUserService;
import com.developer.ck.checkin.util.Constants;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAccessor {
    private static Retrofit buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_SERVER_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static void getDeviceInfo(String uuid, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        GetBuildInfoService service = retrofit.create(GetBuildInfoService.class);
        Call<ResponseBody> call = service.getDeviceInfo(uuid);
        call.enqueue(callback);
    }

    public static void checkTravelCode(String code, int buildId, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        CheckCodeService service = retrofit.create(CheckCodeService.class);
        Call<ResponseBody> call = service.checkCode(code, buildId);
        call.enqueue(callback);
    }

    public static void getBookingDetail(String code, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        GetBookingService service = retrofit.create(GetBookingService.class);
        Call<ResponseBody> call = service.getBookingInfo(code);
        call.enqueue(callback);
    }

    public static void payAmount(String code, String sourceId, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        PayAmountService service = retrofit.create(PayAmountService.class);
        Call<ResponseBody> call = service.payAmount(code, sourceId);
        call.enqueue(callback);
    }

    public static void uploadRoomImage(String path, String code, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        UploadRoomImageService service = retrofit.create(UploadRoomImageService.class);
        RequestBody codeBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), code);
        Call<ResponseBody> call = service.uploadImage(codeBody, fileBody);
        call.enqueue(callback);
    }

    public static void uploadUsers(String code, String body, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        UploadUserService service = retrofit.create(UploadUserService.class);
        Call<ResponseBody> call = service.uploadUsers(code, body);
        call.enqueue(callback);
    }

    public static void checkMoney(String code, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        CheckMoneyService service = retrofit.create(CheckMoneyService.class);
        Call<ResponseBody> call = service.checkMoney(code);
        call.enqueue(callback);
    }

    public static void checkOut(String code, Callback<ResponseBody> callback) {
        Retrofit retrofit = buildRetrofit();
        CheckOutService service = retrofit.create(CheckOutService.class);
        Call<ResponseBody> call = service.checkOut(code);
        call.enqueue(callback);
    }
}

