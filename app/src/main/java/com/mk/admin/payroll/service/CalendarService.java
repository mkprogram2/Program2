package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Holiday;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/26/2018.
 */

public interface CalendarService {
    @GET("holidays/{month}/{year}")
    Call<List<Holiday>> getHoliday (@Header("Person") String person,
                                    @Path("month") Integer month,
                                    @Path("year") Integer year,
                                    @Query("access_token") String access_token);
}
