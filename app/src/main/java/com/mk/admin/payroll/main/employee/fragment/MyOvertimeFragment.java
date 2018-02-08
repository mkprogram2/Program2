package com.mk.admin.payroll.main.employee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.admin.payroll.R;

public class MyOvertimeFragment extends android.support.v4.app.Fragment {
    private View viewFrag1;
    public MyOvertimeFragment () {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewFrag1 = inflater.inflate(R.layout.activity_my_overtime_fragment, container, false);

        return viewFrag1;
    }
}
