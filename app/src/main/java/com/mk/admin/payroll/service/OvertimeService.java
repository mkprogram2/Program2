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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/30/2018.
 */

public interface OvertimeService
{
    @POST("overtimes/")
    Call<Overtime> PostOvertime(@Body Overtime overtime,
                                @Query("access_token") String access_token);

    @GET("overtimes/persons/{id}") // Get all overtime in this Person ID
    Call<List<Overtime>> GetOvertime(@Path("id") String id,
                                     @Query("access_token") String access_token);

    @GET("overtimes")   // Get all overtimes
    Call<List<Overtime>> GetAllOvertime(@Query("access_token") String access_token);

    @DELETE("overtimes/{id}")
    Call<Integer> DeleteOvertime (@Path("id") Integer id,
                                   @Query("access_token") String access_token);

    @PUT("overtimes")
    Call<Overtime> ApprovedOvertime (@Body Overtime overtime,
                                     @Query("access_token") String access_token);

    @GET("overtimes/{id}") //Get one overtime in this overtime id
    Call<Overtime> GetOvertimeId(@Path("id") Integer id,
                                 @Query("access_token") String access_token);

    @GET("overtimes/persons/{id}/{date}")
    Call<Overtime> GetOvertimeByDate(@Path("id") String id,
                                     @Path("date") String date,
                                     @Query("access_token") String access_token);

    @GET("overtimes/persons/{id}/now") //Get one overtime in this id today
    Call<Overtime> GetMyOvertime(@Path("id") String id,
                                 @Query("access_token") String access_token);
}