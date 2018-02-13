package com.mk.admin.payroll.main.manage.fragment;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.LoginActivity;
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
    private Overtime overtime;
    private View viewFrag1;
    private OvertimeService overtimeService;
    private Session session;

    private ImageView cancel_approved;
    private ImageView delete_approved;

    public ListEmployeeOvertime ()
    {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewFrag1 = inflater.inflate(R.layout.fragment_list_employee_overtime, container, false);

        rvOvertime = (RecyclerView)viewFrag1.findViewById(R.id.rv_employee_overtime);
        rvOvertime.setHasFixedSize(true);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        session = new Session(viewFrag1.getContext());

        GetAllOvertime();

        return viewFrag1;
    }

    private void showRecyclerList()
    {
        rvOvertime.setLayoutManager(new LinearLayoutManager(viewFrag1.getContext()));
        EmployeeOvertimeAdapter employeeOvertimeAdapter = new EmployeeOvertimeAdapter(viewFrag1.getContext());
        employeeOvertimeAdapter.setOvertimes(overtimes);
        rvOvertime.setAdapter(employeeOvertimeAdapter);
        Log.d("OVERTIMEE", overtimes.get(0).person.name);

        ItemClickSupport.addTo(rvOvertime).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(overtimes.get(position));
            }
        });
    }

    private void showSelectedPerson(final Overtime overtime)
    {
        final Dialog dialog = new Dialog(viewFrag1.getContext());
        dialog.setContentView(R.layout.approved_overtime_dialog);
        dialog.setTitle("Delete Overtime");
        TextView aprroved_question = (TextView)dialog.findViewById(R.id.aprroved_question);
        cancel_approved = (ImageView)dialog.findViewById(R.id.cancel_approved);
        delete_approved = (ImageView)dialog.findViewById(R.id.delete_approved);
        aprroved_question.setText("Delete Overtime " + overtime.person.name + " On " + overtime.date + " ?");
        dialog.show();

        cancel_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        delete_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOvertime(overtime.id);
                dialog.dismiss();
            }
        });
    }

    private void GetAllOvertime ()
    {
        Call<List<Overtime>> call = overtimeService.GetAllOvertime(session.getAccesstoken());
        call.enqueue(new Callback<List<Overtime>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Overtime>> call, Response<List<Overtime>> response)
            {
                if (response.isSuccessful())
                {
                    overtimes = response.body();
                    showRecyclerList();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Overtime>> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void DeleteOvertime(Integer id)
    {
        Call<Integer> call = overtimeService.DeleteOvertime(id, session.getAccesstoken());
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                if (response.isSuccessful())
                {
                    Integer data = response.body();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                GetAllOvertime();
            }
        });
    }
}
