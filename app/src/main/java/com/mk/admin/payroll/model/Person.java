package com.mk.admin.payroll.model;

/**
 * Created by admin on 1/9/2018.
 */

public class Person
{
    public String id;
    public String name;
    public String apppassword;
    public java.sql.Date birthdate;
    public String card;
    public java.sql.Date created;
    public String email;
    public char gender;
    public byte[] image;
    public String npwp;
    public String phone;
    public Role Role = new Role();
    public Place Place = new Place();
    public PersonsDetail persondetail = new PersonsDetail();
}