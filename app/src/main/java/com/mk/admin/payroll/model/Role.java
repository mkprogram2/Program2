package com.mk.admin.payroll.model;

/**
 * Created by admin on 1/16/2018.
 */

public class Role
{
    public Integer id;
    public String name;
    public Double maxsalary;
    public Double minsalary;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Double getMaxsalary() { return maxsalary; }

    public void setMaxsalary(Double maxsalary) { this.maxsalary = maxsalary; }

    public Double getMinsalary() { return minsalary; }

    public void setMinsalary(Double minsalary) { this.minsalary = minsalary; }
}
