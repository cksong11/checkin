package com.developer.ck.checkin.api.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadRoomImageService {

    @Multipart
    @POST("book/uploadImage")
    Call<ResponseBody> uploadImage(
            @Part("code") RequestBody code,
            @Part MultipartBody.Part image
    );

}
