package com.mk.admin.payroll.main.employee.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mk.admin.payroll.main.employee.adapter.OvertimeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.OvertimeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOvertimeFragment extends android.support.v4.app.Fragment {

    private RecyclerView rvOvertime;
    private List<Overtime> overtimes;
    private View viewFrag2;
    private OvertimeService overtimeService;
    private String persons;
    private Session session;
    private Overtime overtime;

    private ImageView cancel_approved;
    private ImageView approved_overtime;

    public ListOvertimeFragment () {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFrag2 = inflater.inflate(R.layout.activity_list_overtime_fragment, container, false);

        rvOvertime = (RecyclerView)viewFrag2.findViewById(R.id.rv_overtime);
        rvOvertime.setHasFixedSize(true);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(viewFrag2.getContext(),"Persons","");
        session = new Session(viewFrag2.getContext());

        GetOvertime(persons, session.getId());

        return viewFrag2;
    }

    private void showRecyclerList()
    {
        rvOvertime.setLayoutManager(new LinearLayoutManager(viewFrag2.getContext()));
        OvertimeAdapter OvertimeAdapter = new OvertimeAdapter(viewFrag2.getContext());
        OvertimeAdapter.setOvertimes(overtimes);
        rvOvertime.setAdapter(OvertimeAdapter);

        ItemClickSupport.addTo(rvOvertime).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(overtimes.get(position));
            }
        });
    }

    private void showSelectedPerson(Overtime overtime)
    {
        final Dialog dialog = new Dialog(viewFrag2.getContext());
        dialog.setContentView(R.layout.approved_overtime_dialog);
        dialog.setTitle("Delete Overtime");
        TextView aprroved_question = (TextView)dialog.findViewById(R.id.aprroved_question);
        cancel_approved = (ImageView)dialog.findViewById(R.id.cancel_approved);
        approved_overtime = (ImageView)dialog.findViewById(R.id.delete_approved);
        aprroved_question.setText("Approved Overtime On " + overtime.date + " ?");
        dialog.show();

        GetOvertimeId(overtime.id);

        cancel_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        approved_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetOvertime();
                dialog.dismiss();
            }
        });
    }

    private void GetOvertime (String persons, String id)
    {
        Call<List<Overtime>> call = overtimeService.GetOvertime(persons, id, session.getAccesstoken());
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
                else
                {

                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Overtime>> call, Throwable t)
            {
                Toast.makeText(viewFrag2.getContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetOvertimeId (Integer id)
    {
        Call<Overtime> call = overtimeService.GetOvertimeId(id, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag2.getContext(),"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ApprovedOvertime (Overtime overtimes)
    {
        Call<Overtime> call = overtimeService.ApprovedOvertime(overtimes, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    GetOvertime(persons, session.getId());
                    Toast.makeText(viewFrag2.getContext(),"Overtime Approved", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag2.getContext(),"Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetOvertime ()
    {
        overtime.status = 1;
        ApprovedOvertime(overtime);
    }
}
