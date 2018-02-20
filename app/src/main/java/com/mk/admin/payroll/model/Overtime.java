package com.mk.admin.payroll.model;

import java.sql.Time;
import java.sql.Timestamp;
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
    public Time start;
    public Time stop;
    public Integer interval;
    public Person person = new Person();
}
