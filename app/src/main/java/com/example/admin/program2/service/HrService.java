package com.example.admin.program2.service;

import com.example.admin.program2.model.Hr;
import com.example.admin.program2.model.postHr;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
/**
 * Created by admin on 1/3/2018.
 */

public interface HrService
{
    @GET("persons/")
    Call<List<Hr>> getHr(@Header("id") String id);

    @POST("persons/")
    Call<HashMap<Integer, String>> postHr(@Header("id") String id,
                                            @Body Hr Hrr);

    @PUT("persons/")
    Call<List<Hr>> updateHr(@Header("id") String id,
                            @Body Hr uphr);

    @DELETE("persons/{id_employee}")
    Call<HashMap<Integer, String>> deleteHr(@Header("id") String id,
                                            @Path("id_employee") String id_employee);}
