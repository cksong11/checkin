package com.developer.ck.checkin.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UploadUserService {

    @POST("book/addUsers")
    Call<ResponseBody> uploadUsers(
            @Query("code") String code,
            @Query("body") String body
    );

}
