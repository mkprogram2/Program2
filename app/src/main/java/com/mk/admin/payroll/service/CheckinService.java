package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Workhour;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by admin on 1/5/2018.
 */

public interface CheckinService
{
    @POST("workhours/checkin")
    Call<Integer> postCheckin(@Body RequestBody id,
                              @Query("access_token") String access_token);

    @PUT("workhours/checkout/")
    Call<Integer> checkout(@Body RequestBody id,
                           @Query("access_token") String access_token);
}
