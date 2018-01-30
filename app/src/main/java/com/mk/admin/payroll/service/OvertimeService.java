package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Overtime;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by admin on 1/30/2018.
 */

public interface OvertimeService
{
    @POST("overtimes/")
    Call<Overtime> PostOvertime(@Header("persons") String persons,
                                     @Body Overtime overtime);

    @GET("overtimes/{id}")
    Call<List<Overtime>> GetOvertime(@Header("persons") String persons,
                                     @Path("id") RequestBody id);

}
