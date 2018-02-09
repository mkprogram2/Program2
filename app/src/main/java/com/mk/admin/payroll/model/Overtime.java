package com.mk.admin.payroll.model;

import java.util.Date;

/**
 * Created by admin on 1/30/2018.
 */

public class Overtime
{
    public Integer id;
    public String date;
    public String information;
    public Integer duration;
    public Integer status;
    public Person person = new Person();
}
