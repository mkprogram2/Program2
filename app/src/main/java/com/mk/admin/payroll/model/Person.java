package com.mk.admin.payroll.model;

import java.util.Date;

/**
 * Created by admin on 1/9/2018.
 */

public class Person
{
    public String id;
    public String name;
    public String apppassword;
    public Date birthdate;
    public String card;
    public Date created;
    public String email;
    public String gender;
    public String image;
    public String npwp;
    public String phone;
    public Role Role = new Role();
    public Place Place = new Place();
    public PersonsDetail persondetail = new PersonsDetail();
}