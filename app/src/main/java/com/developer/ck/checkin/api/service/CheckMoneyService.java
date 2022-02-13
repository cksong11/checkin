package com.developer.ck.checkin.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CheckMoneyService {

    @POST("book/checkMoney")
    Call<ResponseBody> checkMoney(
            @Query("code") String code
    );

}
