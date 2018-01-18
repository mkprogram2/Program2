package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Person;

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
    Call<Person> postLogin(@Header("persons") String persons,
                           @Body Person login);
}
