package com.developer.ck.checkin.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetCheckInService {

    @POST("device/getInfo")
    Call<ResponseBody> getCheckIn(
            @Query("deviceId") String deviceId,
            @Query("password") String password,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude

    );

}
