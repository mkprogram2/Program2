package com.mk.admin.payroll.main.manage.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.main.manage.adapter.EmployeeOvertimeAdapter;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.OvertimeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2/8/2018.
 */

public class ListEmployeeOvertime extends android.support.v4.app.Fragment {

    private RecyclerView rvOvertime;
    private List<Overtime> overtimes;
    private View viewFrag1;
    private OvertimeService overtimeService;
    private String persons;
    private Session session;

    public ListEmployeeOvertime ()
    {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFrag1 = inflater.inflate(R.layout.activity_list_overtime_fragment, container, false);

        rvOvertime = (RecyclerView)viewFrag1.findViewById(R.id.rv_employee_overtime);
        rvOvertime.setHasFixedSize(true);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(viewFrag1.getContext(),"Persons","");
        session = new Session(viewFrag1.getContext());

        GetOvertime(persons, session.getId());

        return viewFrag1;
    }

    private void showRecyclerList()
    {
        rvOvertime.setLayoutManager(new LinearLayoutManager(viewFrag1.getContext()));
        EmployeeOvertimeAdapter employeeOvertimeAdapter = new EmployeeOvertimeAdapter(viewFrag1.getContext());
        employeeOvertimeAdapter.setOvertimes(overtimes);
        rvOvertime.setAdapter(employeeOvertimeAdapter);

        ItemClickSupport.addTo(rvOvertime).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(overtimes.get(position));
            }
        });
    }

    private void showSelectedPerson(Overtime overtime)
    {
        Toast.makeText(viewFrag1.getContext(), "You Choose "+overtime.date.toString(), Toast.LENGTH_SHORT).show();
    }


    private void GetOvertime (String persons, String id)
    {
        Call<List<Overtime>> call = overtimeService.GetOvertime(persons, id, session.getAccesstoken());
        call.enqueue(new Callback<List<Overtime>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Overtime>> call, Response<List<Overtime>> response)
            {
                overtimes = response.body();

                showRecyclerList();
            }
            @Override
            public void onFailure(retrofit2.Call<List<Overtime>> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
