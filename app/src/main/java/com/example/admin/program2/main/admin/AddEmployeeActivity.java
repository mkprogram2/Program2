package com.example.admin.program2.main.admin;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.model.Role;
import com.example.admin.program2.model.Shift;
import com.example.admin.program2.model.person;
import com.example.admin.program2.service.EmployeeService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @BindView(R.id.date_in)
    EditText date_in;
    @BindView(R.id.add_role_spin)
    Spinner add_role_spin;
    @BindView(R.id.add_shift_spin)
    Spinner add_shift_spin;
    @BindView(R.id.add_shift_in)
    TextView add_shift_in;
    @BindView(R.id.add_shift_out)
    TextView add_shift_out;
    @BindView(R.id.add_employee)
    Button add_employee;
    @BindView(R.id.calendar_in)
    Button calendar_in;

    private EmployeeService employeeService;
    private String persons;
    final List<String> list_shift = new ArrayList<String>();
    final List<String> list_role = new ArrayList<String>();
    private List<Shift> shifts;
    private List<Role> roles; private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    public Date dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        ButterKnife.bind(this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        employeeService = ClientService.createService().create(EmployeeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        GetShift();
        GetRole();

        add_shift_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(add_shift_spin.getSelectedItem().toString()))
                    {
                        add_shift_in.setText(shifts.get(i).getWorkstart());
                        add_shift_out.setText(shifts.get(i).getWorkend());
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

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    private void GetShift()
    {
        Call<List<Shift>> call = employeeService.GetShifts();
        call.enqueue(new Callback<List<Shift>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Shift>> call, Response<List<Shift>> response)
            {
                shifts = response.body();
                Log.d("Shift", shifts.get(0).getWorkstart());
                for (int i = 0; i < shifts.size(); i++)
                {
                    list_shift.add(shifts.get(i).getId());
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
        Call<List<Role>> call = employeeService.GetRoles();
        call.enqueue(new Callback<List<Role>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Role>> call, Response<List<Role>> response)
            {
                roles = response.body();
                Log.d("Role", roles.get(0).getName());
                for (int i = 0; i < roles.size(); i++)
                {
                    list_role.add(roles.get(i).getName());
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

    private void PostEmployee(String person, person persons)
    {
        Call<person> call = employeeService.PostEmployee(person, persons);
        call.enqueue(new Callback<person>()
        {
            @Override
            public void onResponse(retrofit2.Call<person> call, Response<person> response)
            {
                final person dataperson = response.body();
                Log.d("Data", dataperson.getName());
                Toast.makeText(AddEmployeeActivity.this,"Saving Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(AddEmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetPerson()
    {
        person person = new person();
        person.setName(add_employee_name.getText().toString());
        person.setPassword(add_password.getText().toString());
        person.Shift.setId(add_shift_spin.getSelectedItem().toString());
        for (int i = 0; i < roles.size(); i++)
        {
            if (roles.get(i).getName().equals(add_role_spin.getSelectedItem()))
            {
                person.Role.setId(roles.get(i).getId());
            }
        }
        String uniqueId = null;
        if(uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
        }
        person.setId(uniqueId);
        person.assignwork = dateFormatter.format(dates);
        //person.assignwork = dates;
        PostEmployee(persons, person);
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_in.setText(dateFormatter.format(newDate.getTime()).toString());

                try {
                    dates = dateFormatter.parse(date_in.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(dates).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
