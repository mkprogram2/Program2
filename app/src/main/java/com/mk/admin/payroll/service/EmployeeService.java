package com.mk.admin.payroll.service;

import com.mk.admin.payroll.model.Role;
import com.mk.admin.payroll.model.Shift;
import com.mk.admin.payroll.model.Person;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/15/2018.
 */

public interface EmployeeService
{
    @GET("persons/") //Get all person
    Call<List<Person>> GetPerson(@Query("access_token") String access_token);

    @GET("persons/{id}") //Get one person based id
    Call<Person> GetEmployee(@Path("id") String id,
                             @Query("access_token") String access_token);

    @GET("shifts")
    Call<List<Shift>> GetShifts(@Query("access_token") String access_token);

    @GET("roles")
    Call<List<Role>> GetRoles(@Query("access_token") String access_token);

    @PUT("persons/")
    Call<Person> PutEmployee(@Body Person persons,
                             @Query("access_token") String access_token);

    @POST("persons/")
    Call<Person> PostEmployee(@Body Person persons,
                              @Query("access_token") String access_token);

    @GET("persons/sk/{sk}")
    Call<String> GetId(@Path("sk") String serial_key,
                       @Query("access_token") String access_token);
}
