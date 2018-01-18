package com.mk.admin.payroll.model;

/**
 * Created by admin on 1/12/2018.
 */

public class Workhour
{
    public String personid;
    public String workstart;
    public String workend;
    public String workstartinterval;
    public String workendinterval;
    public String workinterval;

    public String getPersonid () { return  personid; }

    public void setPersonid (String personid) { this.personid = personid; }

    public String getWorkstart () { return workstart; }

    public void setWorkstart (String workstart) { this.workstart = workstart; }

    public String getWorkend () { return workend; }

    public void setWorkend (String workend) { this.workend = workend; }

    public String getWorkstartinterval () { return workstartinterval; }

    public void setWorkstartinterval (String workstartinterval) { this.workstartinterval = workstartinterval; }

    public String getWorkendinterval () { return  workendinterval; }

    public void setWorkendinterval (String workendinterval) { this.workendinterval = workendinterval; }

    public String getWorkinterval () { return workinterval; }

    public void setWorkinterval (String workinterval) { this.workinterval = workinterval; }
}
