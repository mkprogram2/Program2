package com.mk.admin.payroll.main.employee;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.main.admin.EmployeeActivity;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.employee_id)
    EditText employee_id;
    @BindView(R.id.employee_name)
    EditText employee_name;
    @BindView(R.id.employee_email)
    EditText employee_email;
    @BindView(R.id.employee_datein)
    EditText employee_datein;
    @BindView(R.id.employee_npwp)
    EditText employee_npwp;
    @BindView(R.id.employee_phone)
    EditText employee_phone;
    @BindView(R.id.employee_birth)
    EditText employee_birth;
    @BindView(R.id.employee_role)
    EditText employee_role;
    @BindView(R.id.employee_gender)
    Spinner employee_gender;
    @BindView(R.id.shift_in)
    TextView shift_in;
    @BindView(R.id.shift_out)
    TextView shift_out;
    @BindView(R.id.save_employee)
    ImageView save_employee;
    @BindView(R.id.edit_employee)
    ImageView edit_employee;
    @BindView(R.id.cancel_edit)
    ImageView cancel_edit;
    @BindView(R.id.calendar_birth)
    Button calendar_birth;

    private EmployeeService employeeService;
    private Session session;
    private Person person = new Person();
    private String[] genderSpinner = new String[] {"Male", "Female"};
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private Date dates;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        session = new Session(this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        employeeService = ClientService.createService().create(EmployeeService.class);
        DisableUI();
        SetGenderSpinner();
        GetEmployee(session.getId());

        edit_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableUI();
            }
        });

        cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisableUI();
            }
        });

        save_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPerson();
            }
        });

        calendar_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void GetEmployee (String id)
    {
        Call<Person> call = employeeService.GetEmployee(id, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    person = response.body();
                    SetUI();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Toast.makeText(ProfileActivity.this,"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetUI ()
    {
        employee_id.setText(person.id);
        employee_name.setText(person.name);
        employee_email.setText(person.email);
        employee_datein.setText(person.persondetail.assignwork);
        employee_role.setText(person.Role.name);
        employee_npwp.setText(person.npwp);
        employee_phone.setText(person.phone);
        employee_birth.setText(person.birthdate.toString());
        shift_in.setText(person.persondetail.Shift.workstart);
        shift_out.setText(person.persondetail.Shift.workend);

        for (int i =0; i < genderSpinner.length; i++)
        {
            if (genderSpinner[i].equals(person.gender))
            {
                employee_gender.setSelection(i);
            }
        }
    }

    private void SetGenderSpinner ()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_gender.setAdapter(adapter);
    }

    private void DisableUI ()
    {
        employee_id.setEnabled(false);
        employee_name.setEnabled(false);
        employee_email.setEnabled(false);
        employee_datein.setEnabled(false);
        employee_role.setEnabled(false);
        employee_npwp.setEnabled(false);
        employee_phone.setEnabled(false);
        employee_birth.setEnabled(false);
        employee_gender.setEnabled(false);
        calendar_birth.setEnabled(false);
        save_employee.setVisibility(View.GONE);
        cancel_edit.setVisibility(View.GONE);
        edit_employee.setVisibility(View.VISIBLE);
    }

    private void EnableUI ()
    {
        employee_name.setEnabled(true);
        employee_email.setEnabled(true);
        employee_npwp.setEnabled(true);
        employee_phone.setEnabled(true);
        employee_birth.setEnabled(true);
        employee_gender.setEnabled(true);
        calendar_birth.setEnabled(true);
        edit_employee.setVisibility(View.GONE);
        save_employee.setVisibility(View.VISIBLE);
        cancel_edit.setVisibility(View.VISIBLE);
    }

    private void SetPerson ()
    {
        person.name = employee_name.getText().toString();
        person.email = employee_email.getText().toString();
        person.npwp = employee_npwp.getText().toString();
        person.phone = employee_phone.getText().toString();
        try
        {
            person.birthdate = dateFormatter.parse(employee_birth.getText().toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        PutEmployee(person);
    }

    private void PutEmployee(Person persons)
    {
        Call<Person> call = employeeService.PutEmployee(persons, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                person = response.body();
                Toast.makeText(ProfileActivity.this,"Saving Success", Toast.LENGTH_LONG).show();
                DisableUI();
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Toast.makeText(ProfileActivity.this,"Server Failed", Toast.LENGTH_LONG).show();
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
                employee_birth.setText(dateFormatter.format(newDate.getTime()).toString());

                try {
                    dates = dateFormatter.parse(employee_birth.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(dates).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
