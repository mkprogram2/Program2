package com.mk.admin.payroll.model;

import java.util.Date;

/**
 * Created by admin on 2/1/2018.
 */

public class Place
{
    public Integer id;
    public String name;
    public String description;
    public Date created;
    public Integer placetype;
    public Location Location = new Location();
}
