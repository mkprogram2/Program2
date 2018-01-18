package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Workhour;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by admin on 1/11/2018.
 */

public interface WorkhourService
{
    @GET("workhours/check/{id}")
    Call<Workhour> getCheckworkhour(@Header("Person") String person,
                                    @Path("id") String id);
}