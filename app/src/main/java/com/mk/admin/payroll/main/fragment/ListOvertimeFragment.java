package com.mk.admin.payroll.main.fragment;

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
import com.mk.admin.payroll.main.adapter.OvertimeAdapter;
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
        Toast.makeText(viewFrag2.getContext(), "You Choose "+overtime.date.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(viewFrag2.getContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
