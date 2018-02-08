package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.AuthUtil;
import com.mk.admin.payroll.model.Person;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by admin on 1/3/2018.
 */

public interface LoginService
{
    @POST("oauth/token")
    Call <AuthUtil> RequestToken (@Header("Authorization") String authorization,
                                  @Header("merchantCode") String kodeMerchant,
                                  @Query("grant_type") String grantType,
                                  @Query("username") String username,
                                  @Query("password") String password);

    @POST("persons/login")
    Call<Person> getPerson(@Query("access_token") String authorization,
                           @Query("email") String email,
                           @Query("serial_key") String serial_key);
}
