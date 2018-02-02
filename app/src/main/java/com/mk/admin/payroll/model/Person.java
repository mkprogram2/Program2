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
    public PersonsDetail PersonDetail = new PersonsDetail();

    /*public String password;
    public double salary;
    public String assignwork;
    public Shift Shift = new Shift();

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public double getSalary () { return salary; }

    public void setSalary (double salary) { this.salary = salary;}*/
}
