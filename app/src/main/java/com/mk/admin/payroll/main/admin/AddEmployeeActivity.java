package com.mk.admin.payroll.main.admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.model.Role;
import com.mk.admin.payroll.model.Shift;
import com.mk.admin.payroll.service.EmployeeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeActivity extends AppCompatActivity {

    @BindView(R.id.add_employee_email)
    EditText add_employee_email;
    @BindView(R.id.add_employee_password)
    EditText add_employee_password;
    @BindView(R.id.add_employee_name)
    EditText add_employee_name;
    @BindView(R.id.add_employee_gender)
    Spinner add_employee_gender;
    @BindView(R.id.add_employee_datein)
    EditText add_employee_date_in;
    @BindView(R.id.add_shift_in)
    EditText add_shift_in;
    @BindView(R.id.add_shift_out)
    EditText add_shift_out;
    @BindView(R.id.add_employee_role)
    Spinner add_employee_role;
    @BindView(R.id.add_employee_shift)
    Spinner add_employee_shift;
    @BindView(R.id.save_employee)
    Button save_employee;
    @BindView(R.id.cancel_save)
    Button cancel_save;
    @BindView(R.id.back_icon)
    ImageView back_icon;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private EmployeeService employeeService;
    final List<String> list_shift = new ArrayList<String>();
    final List<String> list_role = new ArrayList<String>();
    private List<Shift> shifts;
    private List<Role> roles;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private java.sql.Date dates, birthdate;
    private Session session;
    private String[] genderSpinner = new String[] {"Male", "Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        session = new Session(this);
        ButterKnife.bind(this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        employeeService = ClientService.createService().create(EmployeeService.class);

        add_shift_in.setEnabled(false);
        add_shift_out.setEnabled(false);

        GetShift();
        SetGenderSpinner();

        add_employee_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(add_employee_shift.getSelectedItem().toString()))
                    {
                        add_shift_in.setText(shifts.get(i).workstart);
                        add_shift_out.setText(shifts.get(i).workend);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_employee_date_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        save_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (add_employee_name.equals("") || add_employee_password.equals("") || add_employee_date_in.equals(""))
                {
                    Toast.makeText(AddEmployeeActivity.this,"Wrong Entries", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SetPerson();
                }
            }
        });

        cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearUI();
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void GetShift()
    {
        progress_bar.setVisibility(View.VISIBLE);
        Call<List<Shift>> call = employeeService.GetShifts(session.getAccesstoken());
        call.enqueue(new Callback<List<Shift>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Shift>> call, Response<List<Shift>> response)
            {
                if (response.isSuccessful())
                {
                    shifts = response.body();
                    Log.d("Shift", shifts.get(0).workstart);
                    for (int i = 0; i < shifts.size(); i++)
                    {
                        list_shift.add(shifts.get(i).id);
                    }
                    SetShiftSpinner();
                    GetRole();
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(AddEmployeeActivity.this,"Data Not Found !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Shift>> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(AddEmployeeActivity.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetRole()
    {
        Call<List<Role>> call = employeeService.GetRoles(session.getAccesstoken());
        call.enqueue(new Callback<List<Role>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Role>> call, Response<List<Role>> response)
            {
                if (response.isSuccessful())
                {
                    roles = response.body();
                    for (int i = 0; i < roles.size(); i++)
                    {
                        list_role.add(roles.get(i).name);
                    }
                    SetRoleSpinner();
                    progress_bar.setVisibility(View.GONE);
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(AddEmployeeActivity.this,"Data Not Found !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Role>> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(AddEmployeeActivity.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetShiftSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_shift);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_employee_shift.setAdapter(adp1);
    }

    private void SetRoleSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_role);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_employee_role.setAdapter(adp1);
    }

    private void SetGenderSpinner ()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_employee_gender.setAdapter(adapter);
    }

    private void PostEmployee(Person persons)
    {
        progress_bar.setVisibility(View.VISIBLE);
        Call<Person> call = employeeService.PostEmployee(persons, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    final Person dataperson = response.body();
                    progress_bar.setVisibility(View.GONE);
                    startActivity(new Intent(AddEmployeeActivity.this, EmployeeActivity.class));
                    Toast.makeText(AddEmployeeActivity.this,"Saving Successfully !", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(AddEmployeeActivity.this,"Data Not Found !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(AddEmployeeActivity.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetPerson()
    {
        Person person = new Person();
        person.name = add_employee_name.getText().toString();
        person.email = add_employee_email.getText().toString();
        person.apppassword = add_employee_password.getText().toString();
        person.persondetail.Shift.id = add_employee_shift.getSelectedItem().toString();

        for (int i = 0; i < roles.size(); i++)
        {
            if (roles.get(i).name.equals(add_employee_role.getSelectedItem()))
            {
                person.Role.id = roles.get(i).id;
            }
        }

        String uniqueId = null;
        if(uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
        }
        person.id = uniqueId;
        person.persondetail.assignwork = dateFormatter.format(dates);
        person.gender = add_employee_gender.getSelectedItem().toString().charAt(0);

        PostEmployee(person);
    }

    private void showDateDialog()
    {
        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                add_employee_date_in.setText(dateFormatter.format(newDate.getTime()).toString());

                try {
                    Date parse = dateFormatter.parse(add_employee_date_in.getText().toString());
                    dates = new java.sql.Date(parse.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(dates).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void ClearUI ()
    {
        add_employee_name.setText("");
        add_employee_email.setText("");
        add_employee_password.setText("");
        add_employee_date_in.setText("");
    }
}