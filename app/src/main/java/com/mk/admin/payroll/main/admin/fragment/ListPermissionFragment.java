package com.mk.admin.payroll.main.admin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.admin.payroll.R;

public class ListPermissionFragment extends Fragment
{
    private TextView employee_name;

    public ListPermissionFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View viewfrag;
        viewfrag = inflater.inflate(R.layout.fragment_list_permission, container, false);
        employee_name = (TextView)viewfrag.findViewById(R.id.employee_name);
        return inflater.inflate(R.layout.fragment_list_permission, container, false);
    }
}