package com.mk.admin.payroll.main.manage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.OvertimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormEmployeeOvertime extends AppCompatActivity {

    @BindView (R.id.back_icon)
    ImageView back_icon;
    @BindView(R.id.employee_name)
    TextView employee_name;
    @BindView(R.id.overtime_date)
    TextView overtime_date;
    @BindView(R.id.overtime_duration)
    TextView overtime_duration;
    @BindView(R.id.overtime_information)
    TextView overtime_information;
    @BindView(R.id.save_overtime)
    Button save_overtime;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private EmployeeService employeeService;
    private OvertimeService overtimeService;
    private String mid;
    private Session session;
    private List<Person> list;
    private RecyclerView rvCategory;
    private Overtime overtime = new Overtime();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Date dates;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_employee_overtime);
        ButterKnife.bind(this);
        session = new Session(this);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        employeeService = ClientService.createService().create(EmployeeService.class);
        overtimeService = ClientService.createService().create(OvertimeService.class);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });

        employee_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                GetPerson();
            }
        });

        save_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(employee_name.getText()))
                {
                    employee_name.setError("Select Employee");
                    Toast.makeText(FormEmployeeOvertime.this, "Please Select Employee", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(overtime_date.getText()))
                {
                    employee_name.setError(null);
                    overtime_date.setError("Select Date");
                    Toast.makeText(FormEmployeeOvertime.this, "Please Select Calendar", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(overtime_duration.getText()) || overtime_duration.getText().equals("0"))
                {
                    overtime_duration.setError("Fill Duration");
                }
                else if (TextUtils.isEmpty(overtime_information.getText()))
                {
                    overtime_information.setError("Fill Information");
                }
                else
                {
                    Integer durations = Integer.parseInt(overtime_duration.getText().toString());
                    if (durations > 0 && durations <= 3)
                        setOvertime();
                    else
                    {
                        Toast.makeText(FormEmployeeOvertime.this, "Duration Should Have 1 - 3 Hours Only!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        overtime_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employee_name.length() == 0)
                {
                    employee_name.setError("");
                    Toast.makeText(FormEmployeeOvertime.this, "Please Select Employee", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showDateDialog();
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void GetPerson ()
    {
        progress_bar.setVisibility(View.VISIBLE);
        Call<List<Person>> call = employeeService.GetPerson(session.getAccesstoken());
        call.enqueue(new Callback<List<Person>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Person>> call, Response<List<Person>> response)
            {
                if (response.isSuccessful())
                {
                    list = response.body();
                    showRecyclerList();
                    progress_bar.setVisibility(View.GONE);
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(FormEmployeeOvertime.this,"Server Failed !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Person>> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(FormEmployeeOvertime.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList()
    {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.employee_list_dialog);
        dialog.setTitle("Employee");

        dialog.show();

        rvCategory = (RecyclerView)dialog.findViewById(R.id.rv_employees);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(this);
        EmployeeAdapter.setListPerson(list);
        rvCategory.setAdapter(EmployeeAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(list.get(position));
                dialog.dismiss();
            }
        });
    }

    private void showSelectedPerson(Person person)
    {
        mid = person.id;
        for (int i =0; i < list.size(); i++)
        {
            if (list.get(i).id == mid)
            {
                employee_name.setText(list.get(i).name);
                break;
            }
        }
    }

    private void GetOvertimeByDate (String id, final String date)
    {
        Call<Overtime> call = overtimeService.GetOvertimeByDate(id, date, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    overtime_date.setText(date);
                    Integer duration_over = overtime.duration / 3600;
                    overtime_duration.setText(duration_over.toString());
                    overtime_information.setText(overtime.information.toString());
                }
                else
                {
                    Toast.makeText(FormEmployeeOvertime.this,"Make New Overtime !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(FormEmployeeOvertime.this,"Make New Overtime !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setOvertime ()
    {
        overtime.person.id = mid;
        overtime.date = overtime_date.getText().toString();
        overtime.duration = Integer.parseInt(overtime_duration.getText().toString()) * 3600;
        overtime.information = overtime_information.getText().toString();
        overtime.status = 0;
        PostOvertime(overtime);
    }

    private void PostOvertime (Overtime overtimes)
    {
        progress_bar.setVisibility(View.VISIBLE);
        Call<Overtime> call = overtimeService.PostOvertime(overtimes, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(FormEmployeeOvertime.this, "Overtime Saved !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(FormEmployeeOvertime.this,"Server Failed !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(FormEmployeeOvertime.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDateDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                overtime_date.setText(dateFormatter.format(newDate.getTime()).toString());

                try
                {
                    dates = dateFormatter.parse(overtime_date.getText().toString());
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(dates).toString());
                GetOvertimeByDate(mid, dateFormatter.format(newDate.getTime()).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
