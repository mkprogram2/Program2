package com.example.admin.program2.model;

/**
 * Created by admin on 1/9/2018.
 */

public class person
{
    public String id;
    public String name;
    public String password;
    public double salary;
    public Shift Shift;
    public Role Role;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public double getSalary () { return salary; }

    public void setSalary (double salary) { this.salary = salary;}
}
