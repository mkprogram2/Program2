package com.mk.admin.payroll.main.manage.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.admin.RemunerationActivity;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.OvertimeService;
import com.mk.admin.payroll.service.SalaryService;

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

/**
 * Created by admin on 2/8/2018.
 */

public class AddEmployeeOvertime extends android.support.v4.app.Fragment
{
    private Button select_employee;
    private Button overtime_calendar;
    private ImageView save_overtime;
    private ImageView cancel_overtime;
    private TextView employee_name;
    private TextView employee_role;
    private EditText date_overtime;
    private EditText duration;
    private EditText information;

    private View viewFrag1;
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

    public AddEmployeeOvertime ()
    {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewFrag1 = inflater.inflate(R.layout.fragment_add_employee_overtime, container, false);
        session = new Session(viewFrag1.getContext());

        select_employee = (Button)viewFrag1.findViewById(R.id.select_employee);
        overtime_calendar = (Button)viewFrag1.findViewById(R.id.overtime_calendar);
        save_overtime = (ImageView)viewFrag1.findViewById(R.id.save_overtime);
        cancel_overtime = (ImageView)viewFrag1.findViewById(R.id.cancel_overtime);
        employee_name = (TextView)viewFrag1.findViewById(R.id.employee_name);
        employee_role = (TextView)viewFrag1.findViewById(R.id.employee_role);
        date_overtime = (EditText)viewFrag1.findViewById(R.id.date_overtime);
        duration = (EditText)viewFrag1.findViewById(R.id.duration);
        information = (EditText)viewFrag1.findViewById(R.id.information);

        save_overtime.setVisibility(View.GONE);
        cancel_overtime.setVisibility(View.GONE);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        employeeService = ClientService.createService().create(EmployeeService.class);
        overtimeService = ClientService.createService().create(OvertimeService.class);

        select_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPerson();
            }
        });

        save_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(employee_name.getText()))
                {
                    select_employee.setError("Select Employee");
                    Toast.makeText(viewFrag1.getContext(), "Please Select Employee", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(date_overtime.getText()))
                {
                    select_employee.setError(null);
                    overtime_calendar.setError("Select Calendar");
                    Toast.makeText(viewFrag1.getContext(), "Please Select Calendar", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(duration.getText()) || duration.getText().equals("0"))
                {
                    duration.setError("Fill Duration");
                }
                else if (TextUtils.isEmpty(information.getText()))
                {
                    information.setError("Fill Information");
                }
                else
                {
                    Integer durations = Integer.parseInt(duration.getText().toString());
                    if (durations > 0 && durations <= 3)
                        setOvertime();
                    else
                    {
                        Toast.makeText(viewFrag1.getContext(), "Duration Should Have 1 - 3 Hours Only!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancel_overtime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clearUI();
            }
        });

        overtime_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employee_name.length() == 0)
                {
                    select_employee.setError("");
                    Toast.makeText(viewFrag1.getContext(), "Please Select Employee", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showDateDialog();
                }
            }
        });
        return viewFrag1;
    }

    private void GetPerson ()
    {
        Call<List<Person>> call = employeeService.GetPerson(session.getAccesstoken());
        call.enqueue(new Callback<List<Person>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Person>> call, Response<List<Person>> response)
            {
                if (response.isSuccessful())
                {
                    list = response.body();
                    Log.d("Data",list.get(0).name);
                    Log.d("Size", String.valueOf(list.size()));
                    showRecyclerList();
                }
                else
                    Toast.makeText(viewFrag1.getContext(),"Server Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Person>> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList()
    {
        // custom dialog
        final Dialog dialog = new Dialog(viewFrag1.getContext());
        dialog.setContentView(R.layout.employee_list_dialog);
        dialog.setTitle("Employee");

        dialog.show();

        rvCategory = (RecyclerView)dialog.findViewById(R.id.rv_employees);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(viewFrag1.getContext()));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(viewFrag1.getContext());
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

        //GetEmployee(persons, mid);

        for (int i =0; i < list.size(); i++)
        {
            if (list.get(i).id == mid)
            {
                employee_name.setText(list.get(i).name);
                employee_role.setText(list.get(i).Role.name);

                save_overtime.setVisibility(View.VISIBLE);
                cancel_overtime.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    /*private void GetEmployee (String persons, String id)
    {
        Call<Person> call = employeeService.GetEmployee(persons, id, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                dataperson = response.body();
                employee_name.setText(dataperson.name.toString());
                employee_role.setText(dataperson.Role.name.toString());

                save_overtime.setVisibility(View.VISIBLE);
                cancel_overtime.setVisibility(View.VISIBLE);
                //EdittextChange();
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(viewFrag1.getContext(),"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }*/

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
                    date_overtime.setText(date);
                    Integer duration_over = overtime.duration / 3600;
                    duration.setText(duration_over.toString());
                    information.setText(overtime.information.toString());
                }
                else
                {
                    Toast.makeText(viewFrag1.getContext(), "New Overtime", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"Server Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearUI ()
    {
        employee_name.setText("");
        employee_role.setText("");
        date_overtime.setText("");
        duration.setText("0");
        information.setText("");
        select_employee.setError(null);
        date_overtime.setError(null);
    }

    private void setOvertime ()
    {
        overtime.person.id = mid;
        overtime.date = date_overtime.getText().toString();
        overtime.duration = Integer.parseInt(duration.getText().toString()) * 3600;
        overtime.information = information.getText().toString();
        overtime.status = 0;
        PostOvertime(overtime);
    }

    private void PostOvertime (Overtime overtimes)
    {
        Call<Overtime> call = overtimeService.PostOvertime(overtimes, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    Toast.makeText(viewFrag1.getContext(), "Overtime Saved", Toast.LENGTH_SHORT).show();
                    clearUI();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"Server Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(viewFrag1.getContext(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_overtime.setText(dateFormatter.format(newDate.getTime()).toString());

                try
                {
                    dates = dateFormatter.parse(date_overtime.getText().toString());
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