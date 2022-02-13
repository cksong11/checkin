package com.developer.ck.checkin.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PayAmountService {

    @POST("book/payAmount")
    Call<ResponseBody> payAmount(
            @Query("code") String code,
            @Query("source_id") String sourceId
    );

}
