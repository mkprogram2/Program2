package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Overtime;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/30/2018.
 */

public interface OvertimeService
{
    @POST("overtimes/")
    Call<Overtime> PostOvertime(@Header("persons") String persons,
                                @Body Overtime overtime,
                                @Query("access_token") String access_token);

    @GET("overtimes/{id}")
    Call<List<Overtime>> GetOvertime(@Header("persons") String persons,
                                     @Path("id") String id,
                                     @Query("access_token") String access_token);

    @GET("overtimes")
    Call<List<Overtime>> GetAllOvertime(@Header("persons") String person,
                                         @Query("access_token") String access_token);

    @DELETE("overtimes/{id}")
    Call<Integer> DeleteOvertime (@Path("id") Integer id,
                                   @Query("access_token") String access_token);
}