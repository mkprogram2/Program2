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

    @BindView(R.id.add_employee_name)
    EditText add_employee_name;
    @BindView(R.id.add_password)
    EditText add_password;
    @BindView(R.id.add_email)
    EditText add_email;
    @BindView(R.id.date_in)
    EditText date_in;
    @BindView(R.id.add_npwp)
    EditText add_npwp;
    @BindView(R.id.add_phone)
    EditText add_phone;
    @BindView(R.id.add_birth)
    TextView add_birth;
    @BindView(R.id.add_gender)
    Spinner add_gender;
    @BindView(R.id.add_role_spin)
    Spinner add_role_spin;
    @BindView(R.id.add_shift_spin)
    Spinner add_shift_spin;
    @BindView(R.id.add_shift_in)
    TextView add_shift_in;
    @BindView(R.id.add_shift_out)
    TextView add_shift_out;
    @BindView(R.id.calendar_in)
    Button calendar_in;
    /*@BindView(R.id.calendar_birth)
    Button calendar_birth;*/
    @BindView(R.id.save_employee)
    ImageView save_employee;
    @BindView(R.id.cancel_save)
    ImageView cancel_save;

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

        GetShift();
        GetRole();
        SetGenderSpinner();

        add_shift_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(add_shift_spin.getSelectedItem().toString()))
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

        calendar_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        save_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (add_employee_name.equals("") || add_password.equals("") || date_in.equals(""))
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

        add_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialogBirth();
            }
        });

        /*calendar_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialogBirth();
            }
        });*/
    }

    private void GetShift()
    {
        Call<List<Shift>> call = employeeService.GetShifts(session.getAccesstoken());
        call.enqueue(new Callback<List<Shift>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Shift>> call, Response<List<Shift>> response)
            {
                shifts = response.body();
                Log.d("Shift", shifts.get(0).workstart);
                for (int i = 0; i < shifts.size(); i++)
                {
                    list_shift.add(shifts.get(i).id);
                }
                SetShiftSpinner();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Shift>> call, Throwable t)
            {
                Toast.makeText(AddEmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
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
                roles = response.body();
                Log.d("Role", roles.get(0).name);
                for (int i = 0; i < roles.size(); i++)
                {
                    list_role.add(roles.get(i).name);
                }
                SetRoleSpinner();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Role>> call, Throwable t)
            {
                Toast.makeText(AddEmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetShiftSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_shift);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_shift_spin.setAdapter(adp1);
    }

    private void SetRoleSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_role);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_role_spin.setAdapter(adp1);
    }

    private void SetGenderSpinner ()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_gender.setAdapter(adapter);
    }

    private void PostEmployee(Person persons)
    {
        Call<Person> call = employeeService.PostEmployee(persons, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                final Person dataperson = response.body();
                Log.d("Data", dataperson.name);
                Toast.makeText(AddEmployeeActivity.this,"Saving Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddEmployeeActivity.this, EmployeeActivity.class));
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(AddEmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetPerson()
    {
        Person person = new Person();
        person.name = add_employee_name.getText().toString();
        person.email = add_email.getText().toString();
        person.apppassword = add_password.getText().toString();
        person.persondetail.Shift.id = add_shift_spin.getSelectedItem().toString();

        for (int i = 0; i < roles.size(); i++)
        {
            if (roles.get(i).name.equals(add_role_spin.getSelectedItem()))
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
        person.gender = add_gender.getSelectedItem().toString().charAt(0);

        if (!add_npwp.equals(""))
            person.npwp = add_npwp.getText().toString();
        if (!add_phone.equals(""))
            person.phone = add_phone.getText().toString();
        if (!add_birth.equals(""))
            person.birthdate = birthdate;

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
                date_in.setText(dateFormatter.format(newDate.getTime()).toString());

                try {
                    Date parse = dateFormatter.parse(date_in.getText().toString());
                    dates = new java.sql.Date(parse.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(dates).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showDateDialogBirth()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                add_birth.setText(dateFormatter.format(newDate.getTime()).toString());

                try {
                    Date parse = dateFormatter.parse(add_birth.getText().toString());
                    birthdate = new java.sql.Date(parse.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(birthdate).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void ClearUI ()
    {
        add_employee_name.setText("");
        add_email.setText("");
        add_password.setText("");
        date_in.setText("");
        add_npwp.setText("");
        add_phone.setText("");
        add_birth.setText("");
    }
}