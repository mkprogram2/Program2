package com.example.admin.program2.service;

import com.example.admin.program2.model.Role;
import com.example.admin.program2.model.Shift;
import com.example.admin.program2.model.person;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
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
    Call<List<person>> GetPerson(@Header("person") String person);

    @GET("persons/{id}")
    Call<person> GetEmployee(@Header("person")String person,
                                              @Path("id") String id);

    @GET("shifts")
    Call<List<Shift>> GetShifts();

    @GET("roles")
    Call<List<Role>> GetRoles();

    @PUT("persons/")
    Call<person> PutEmployee(@Header("person") String person,
                             @Body person persons);

    @POST("persons/")
    Call<person> PostEmployee(@Header("person") String person,
                              @Body person persons);
}
