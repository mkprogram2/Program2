package com.example.admin.program2.model;

/**
 * Created by admin on 1/11/2018.
 */

public class Shift
{
    public String id;
    public String workstart;
    public String workend;

    public String getId () { return id; }

    public void setId (String id) { this.id = id; }

    public String getWorkstart () { return workstart; }

    public void setWorkstart (String workstart) { this.workstart = workstart; }

    public String getWorkend () { return workend; }

    public void setWorkend (String workend) { this.workend = workend; }
}
