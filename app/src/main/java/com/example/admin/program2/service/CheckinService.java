package com.example.admin.program2.service;

import com.example.admin.program2.model.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by admin on 1/5/2018.
 */

public interface CheckinService
{
    @POST("persons/login/")
    Call<HashMap<String, String>> postCheckin(@Header("persons") String persons,
                                            @Body String id);
}
