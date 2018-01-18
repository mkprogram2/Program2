package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Role;
import com.mk.admin.payroll.model.Shift;
import com.mk.admin.payroll.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by admin on 1/15/2018.
 */

public interface EmployeeService
{
    @GET("persons/")
    Call<List<Person>> GetPerson(@Header("Person") String person);

    @GET("persons/{id}")
    Call<Person> GetEmployee(@Header("Person")String person,
                             @Path("id") String id);

    @GET("shifts")
    Call<List<Shift>> GetShifts();

    @GET("roles")
    Call<List<Role>> GetRoles();

    @PUT("persons/")
    Call<Person> PutEmployee(@Header("Person") String person,
                             @Body Person persons);

    @POST("persons/")
    Call<Person> PostEmployee(@Header("Person") String person,
                              @Body Person persons);
}
