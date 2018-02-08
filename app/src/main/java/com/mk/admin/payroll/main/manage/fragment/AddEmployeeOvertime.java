package com.mk.admin.payroll.main.manage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.admin.payroll.R;

/**
 * Created by admin on 2/8/2018.
 */

public class AddEmployeeOvertime extends android.support.v4.app.Fragment  {
    private View viewFrag2;
    public AddEmployeeOvertime () {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewFrag2 = inflater.inflate(R.layout.fragment_add_employee_overtime, container, false);

        return viewFrag2;
    }
}
