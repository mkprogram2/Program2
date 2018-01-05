package com.example.admin.program2.service;


import com.example.admin.program2.model.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by admin on 1/3/2018.
 */

public interface LoginService
{
    @POST("persons/login/")
    Call<HashMap<String, String>> postLogin(@Header("persons") String persons,
                                             @Body Login login);
}
