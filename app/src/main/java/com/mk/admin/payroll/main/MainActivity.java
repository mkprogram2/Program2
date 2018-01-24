package com.mk.admin.payroll.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.common.IDs;
import com.mk.admin.payroll.main.admin.AddEmployeeActivity;
import com.mk.admin.payroll.main.admin.EmployeeRecyclerActivity;
import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.service.CheckinService;
import com.mk.admin.payroll.service.WorkhourService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private CheckinService service2;
    private WorkhourService service3;
    public String mid, mname, shift_workstart, shift_workend, interval_work, role_name, role_id;
    public double gross_salary;
    public Integer mshiftid;
    private String persons;

    @BindView(R.id.employee_name)
    TextView employee_name;
    @BindView(R.id.employee_role)
    TextView employee_role;
    @BindView(R.id.calendar)
    Button calendar;
    @BindView(R.id.checkin)
    Button checkin;
    @BindView(R.id.workhours)
    Button workhours;
    @BindView(R.id.checkout)
    Button checkout;
    @BindView(R.id.salary)
    Button salary;
    @BindView(R.id.remuneration)
    Button remuneration;
    @BindView(R.id.employee)
    Button employee;
    @BindView(R.id.add_employee)
    Button add_employee;
    @BindView(R.id.human_resource)
    LinearLayout human_resource;
    @BindView(R.id.manage)
    LinearLayout manage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mid = getIntent().getExtras().getString("id");
        mname = getIntent().getExtras().getString("name");
        role_name = getIntent().getExtras().getString("role_name");
        role_id = getIntent().getExtras().getString("role");
        gross_salary = getIntent().getExtras().getDouble("salary");
        mshiftid = Integer.parseInt(getIntent().getExtras().getString("shiftid")) ;
        shift_workstart = getIntent().getExtras().getString("shift_workstart");
        shift_workend = getIntent().getExtras().getString("shift_workend");

        if (role_id.equals("1"))
        {
            manage.setVisibility(View.GONE);
        }
        else if (role_id.equals("2"))
        {
            human_resource.setVisibility(View.GONE);
        }
        else
        {
            human_resource.setVisibility(View.GONE);
            manage.setVisibility(View.GONE);
        }

        employee_name.setText(mname);
        employee_role.setText(role_name);
        IDs.setIdUser(mid);
        IDs.setLoginUser(mname);

        GetIntervalWork();

        service2 = ClientService.createService().create(CheckinService.class);
        service3 = ClientService.createService().create(WorkhourService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        String strRequestBody = mid;
        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        checkin.setOnClickListener (new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkIn(persons, requestBody);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CalendarActivity.class));
            }
        });

        workhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workhour(persons, mid);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut(persons, requestBody);
            }
        });

        salary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, SalaryActivity.class);
                intent.putExtra("id", mid);
                intent.putExtra("name", mname);
                intent.putExtra("role_name", role_name);
                startActivity(intent);
            }
        });

        remuneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmployeeRecyclerActivity.class);
                intent.putExtra("activity", "remuneration");
                startActivity(intent);
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmployeeRecyclerActivity.class);
                intent.putExtra("activity", "employee");
                startActivity(intent);
            }
        });

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddEmployeeActivity.class));
            }
        });
    }

    private void GetIntervalWork ()
    {
        try
        {
            SimpleDateFormat date = new SimpleDateFormat("hh:mm");
            Date tglAwal = (Date) date.parse(shift_workstart);
            Date tglAkhir = (Date) date.parse(shift_workend);

            long bedaHari = Math.abs(tglAkhir.getTime() - tglAwal.getTime());
            interval_work = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toHours(bedaHari),
                    TimeUnit.MILLISECONDS.toMinutes(bedaHari) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(bedaHari)));
        }
        catch(Exception e)
        {}
    }

    private void checkIn(final String persons, RequestBody id)
    {
        Call<Integer> call = service2.postCheckin(persons, id);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer data = response.body();

                if (data == 1)
                {
                    workhour(persons, mid);
                }
                else if(data == 2)
                {
                    Toast.makeText(MainActivity.this,"Anda Telah CheckIn Hari Ini", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Holiday!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkOut(final String persons, RequestBody id)
    {
        Call<Integer> call = service2.checkout(persons, id);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer data = response.body();

                if (data == 1)
                {
                    workhour(persons, mid);
                }
                else if (data == 2)
                {
                    Toast.makeText(MainActivity.this,"Please Check In", Toast.LENGTH_LONG).show();
                }
                else if (data == 3)
                {
                    Toast.makeText(MainActivity.this,"You Have Been Checked Out", Toast.LENGTH_LONG).show();
                }
                else if (data == 4)
                {
                    Toast.makeText(MainActivity.this,"Cannot Check Out in 30 Minutes After Checked In", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void workhour (final String persons, String id)
    {
        Call<Workhour> call = service3.getCheckworkhour(persons, id);
        call.enqueue(new Callback<Workhour>()
        {
            private String responseCode, responseMessage;

            @Override
            public void onResponse(retrofit2.Call<Workhour> call, Response<Workhour> response)
            {
                final Workhour data = response.body();

                Intent intent = new Intent(MainActivity.this, WorkhoursActivity.class);
                intent.putExtra("workstart", data.workstart.toString());
                intent.putExtra("workstartinterval", data.workstartinterval.toString());
                intent.putExtra("interval_work", interval_work);
                intent.putExtra("personid", data.personid);
                intent.putExtra("checkout_status", data.status.toString());
                if (data.workend == null)
                {
                    intent.putExtra("workend", "null_workend");
                }
                else
                {
                    intent.putExtra("workend", data.workend.toString());
                }
                startActivity(intent);
            }

            @Override
            public void onFailure(retrofit2.Call<Workhour> call, Throwable t)
            {
                Toast.makeText(MainActivity.this,"Please Check In", Toast.LENGTH_LONG).show();
            }

        });
    }
}
