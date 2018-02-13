package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Remuneration;
import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/12/2018.
 */

public interface SalaryService
{
    @GET("workhours/{month}/{year}/{id}")
    Call<List<Workhour>> getDay (@Path("month") Integer month,
                                 @Path("year") Integer year,
                                 @Path("id") String id,
                                 @Query("access_token") String access_token);

    @GET("remunerations/totalwim/{month}/{year}")
    Call<Integer> GetWorkdays (@Path("month") Integer month,
                               @Path("year") Integer year,
                               @Query("access_token") String access_token);

    @GET("remunerations/totalwfy/{month}/{year}")
    Call<Integer> GetWorkdaysToday (@Path("month") Integer month,
                                    @Path("year") Integer year,
                                    @Query("access_token") String access_token);

    @GET("remunerations/{id}/{month}/{year}")
    Call<Remuneration> GetRemuneration (@Path("id") String id,
                                        @Path("month") Integer month,
                                        @Path("year") Integer year,
                                        @Query("access_token") String access_token);

    @POST("remunerations/")
    Call<Remuneration> PostRemuneration (@Body Remuneration remuneration,
                                         @Query("access_token") String access_token);
}