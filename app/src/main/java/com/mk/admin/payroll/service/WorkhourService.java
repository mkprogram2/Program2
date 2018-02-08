package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Workhour;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/11/2018.
 */

public interface WorkhourService
{
    @GET("workhours/check/{id}")
    Call<Workhour> getCheckworkhour(@Path("id") String id,
                                    @Query("access_token") String access_token);

    @POST("behaviors")
    Call<Integer> postBehavior(@Body RequestBody id,
                               @Query("access_token") String access_token);
}