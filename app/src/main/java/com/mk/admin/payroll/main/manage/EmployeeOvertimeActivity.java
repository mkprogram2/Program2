package com.mk.admin.payroll.main.manage;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.main.manage.adapter.EmployeeOvertimeAdapter;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.OvertimeService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeOvertimeActivity extends AppCompatActivity
{
    @BindView(R.id.rv_employee_overtime)
    RecyclerView rv_employee_overtime;
    @BindView(R.id.back_icon)
    ImageView back_icon;
    @BindView(R.id.add_overtime)
    ImageView add_overtime;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.data_not_found)
    LinearLayout data_not_found;
    @BindView(R.id.info)
    TextView info;

    private List<Overtime> overtimes;
    private OvertimeService overtimeService;
    private Session session;
    private ImageView cancel_approved;
    private ImageView delete_approved;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_overtime);

        ButterKnife.bind(this);
        rv_employee_overtime.setHasFixedSize(true);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        session = new Session(this);

        GetAllOvertime();

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });

        data_not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                GetAllOvertime();
            }
        });

        add_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(EmployeeOvertimeActivity.this, FormEmployeeOvertime.class));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void showRecyclerList()
    {
        rv_employee_overtime.setLayoutManager(new LinearLayoutManager(this));
        EmployeeOvertimeAdapter employeeOvertimeAdapter = new EmployeeOvertimeAdapter(this);
        employeeOvertimeAdapter.setOvertimes(overtimes);
        rv_employee_overtime.setAdapter(employeeOvertimeAdapter);

        ItemClickSupport.addTo(rv_employee_overtime).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(overtimes.get(position));
            }
        });
    }

    private void showSelectedPerson(final Overtime overtime)
    {
        final Dialog dialog = new Dialog(this);
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
        progress_bar.setVisibility(View.VISIBLE);
        Call<List<Overtime>> call = overtimeService.GetAllOvertime(session.getAccesstoken());
        call.enqueue(new Callback<List<Overtime>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Overtime>> call, Response<List<Overtime>> response)
            {
                if (response.isSuccessful())
                {
                    overtimes = response.body();
                    if (overtimes.size() == 0)
                    {
                        progress_bar.setVisibility(View.GONE);
                        rv_employee_overtime.setVisibility(View.GONE);
                        data_not_found.setVisibility(View.VISIBLE);
                        info.setText("No Data !");
                    }
                    else
                    {
                        progress_bar.setVisibility(View.GONE);
                        rv_employee_overtime.setVisibility(View.VISIBLE);
                        data_not_found.setVisibility(View.GONE);
                        showRecyclerList();
                    }
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    rv_employee_overtime.setVisibility(View.GONE);
                    data_not_found.setVisibility(View.VISIBLE);
                    info.setText("Data Not Found !");
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Overtime>> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                rv_employee_overtime.setVisibility(View.GONE);
                data_not_found.setVisibility(View.VISIBLE);
                info.setText("Data Not Found !");
                Toast.makeText(EmployeeOvertimeActivity.this,"Server Failed !", Toast.LENGTH_LONG).show();
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
