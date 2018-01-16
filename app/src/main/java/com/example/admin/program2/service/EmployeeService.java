package com.example.admin.program2.service;

import com.example.admin.program2.model.person;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by admin on 1/15/2018.
 */

public interface EmployeeService
{
    @GET("persons/")
    Call<List<person>> GetPerson(@Header("person") String person);
}
