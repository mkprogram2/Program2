package com.mk.admin.payroll.model;

import java.sql.Timestamp;

/**
 * Created by admin on 1/12/2018.
 */

public class Workhour
{
    public String personid;
    public Timestamp workstart;
    public Timestamp workend;
    public Long workstartinterval;
    public Long workendinterval;
    public Long workinterval;
    public Integer status;
}
