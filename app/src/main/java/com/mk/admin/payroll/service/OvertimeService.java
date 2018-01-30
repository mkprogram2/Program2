package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Overtime;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by admin on 1/30/2018.
 */

public interface OvertimeService
{
    @POST("overtimes/")
    Call<Overtime> PostOvertime(@Header("persons") String persons,
                               @Body Overtime overtime);
}
