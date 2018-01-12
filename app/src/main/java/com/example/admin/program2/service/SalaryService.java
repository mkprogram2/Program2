package com.example.admin.program2.service;

import com.example.admin.program2.model.Workhour;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by admin on 1/12/2018.
 */

public interface SalaryService
{
    @GET("remunerations/salary/{month}/{year}/{id}")
    Call<Double> getSalary(@Header("person") String person,
                                                    @Path("month") String month,
                                                    @Path("year") String year,
                                                    @Path("id") String id);

    @GET("workhours/{month}/{year}/{id}")
    Call<List<Workhour>> getDay(@Header("person") String person,
                             @Path("month") String month,
                             @Path("year") String year,
                             @Path("id") String id);
}
