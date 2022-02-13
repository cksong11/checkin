package com.developer.ck.checkin.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CheckCodeService {

    @POST("book/checkCode")
    Call<ResponseBody> checkCode(
            @Query("code") String code,
            @Query("build_id") int buildId
    );

}
