package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by admin on 1/12/2018.
 */

public interface SalaryService
{
    @GET("remunerations/salary/{month}/{year}/{id}")
    Call<Double> getSalary(@Header("Person") String person,
                                                    @Path("month") String month,
                                                    @Path("year") String year,
                                                    @Path("id") String id);

    @GET("workhours/{month}/{year}/{id}")
    Call<List<Workhour>> getDay (@Header("Person") String person,
                             @Path("month") String month,
                             @Path("year") String year,
                             @Path("id") String id);

    @GET("remunerations/totalwim/{month}/{year}")
    Call<Integer> GetWorkdays (@Header("Person") String person,
                               @Path("month") String month,
                               @Path("year") String year);

    @PUT("remunerations/salary")
    Call<Person> PutSalary (@Header("Person") String person,
                            @Body Person persons);
}
