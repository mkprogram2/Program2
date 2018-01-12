package com.example.admin.program2.service;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by admin on 1/12/2018.
 */

public interface SalaryService
{
    @GET("workhours/check/{id}")
    Call<HashMap<String, String>> getSalary(@Header("person") String person,
                                                   @Path("id") String id);
}
