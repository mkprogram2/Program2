package com.mk.admin.payroll.common;

/**
 * Created by admin on 2/1/2018.
 */

public class Event
{

    public static int COMPLETE = 200;
    public static int ERROR = 400;
    private int id;
    private int status;

    public Event(int id, int status)
    {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }
}
