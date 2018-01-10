package com.example.admin.program2.service;

import com.example.admin.program2.model.Login;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by admin on 1/5/2018.
 */

public interface CheckinService
{
    @POST("workhours/checkin/")
    Call<HashMap<String, String>> postCheckin(@Header("persons") String persons,
                                            @Body RequestBody id);

    @PUT("workhours/checkout/")
    Call<HashMap<String, String>> checkout(@Header("person") String person,
                                           @Body RequestBody id);
}
