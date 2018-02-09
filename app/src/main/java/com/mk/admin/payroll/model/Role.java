package com.mk.admin.payroll.model;

/**
 * Created by admin on 1/16/2018.
 */

public class Role
{
    public Integer id;
    public String name;
    public String permissions;
    public Integer rightslevel;
    public RoleDetail RoleDetail = new RoleDetail();
}
