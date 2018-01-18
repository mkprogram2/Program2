package com.mk.admin.payroll.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.model.Role;
import com.mk.admin.payroll.model.Shift;
import com.mk.admin.payroll.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends AppCompatActivity {

    @BindView(R.id.employee_id)
    EditText employee_id;
    @BindView(R.id.employee_name)
    EditText employee_name;
    @BindView(R.id.role_spin)
    Spinner role_spin;
    @BindView(R.id.shift_spin)
    Spinner shift_spin;
    @BindView(R.id.shift_in)
    TextView shift_in;
    @BindView(R.id.shift_out)
    TextView shift_out;
    @BindView(R.id.save_employee)
    Button save_employee;

    private EmployeeService employeeService;
    private String persons, shiftid, role;
    final List<String> list_shift = new ArrayList<String>();
    final List<String> list_role = new ArrayList<String>();
    private List<Shift> shifts;
    private List<Role> roles;
    private Person dataperson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        ButterKnife.bind(this);

        employee_id.setText(getIntent().getExtras().getString("id"));
        employee_name.setText(getIntent().getExtras().getString("name"));

        employeeService = ClientService.createService().create(EmployeeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        GetEmployee(persons, employee_id.getText().toString());
        GetShift();
        GetRole();

        shift_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(shift_spin.getSelectedItem().toString()))
                    {
                        shift_in.setText(shifts.get(i).getWorkstart());
                        shift_out.setText(shifts.get(i).getWorkend());
                        Toast.makeText(EmployeeActivity.this,shifts.get(i).getWorkstart(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataperson.setName(employee_name.getText().toString());
                SetRole();
                SetShift();
                PutEmployee(persons, dataperson);
            }
        });
    }

    private void GetEmployee (String persons, String id)
    {
        Call<Person> call = employeeService.GetEmployee(persons, id);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.getName());
                shiftid = dataperson.Shift.getId();
                role = dataperson.Role.getName();
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
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
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
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
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetShiftSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_shift);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shift_spin.setAdapter(adp1);

        for(int i = 0; i < list_shift.size (); i++)
        {
            if(list_shift.get(i).equals(shiftid)) {
                shift_spin.setSelection(i);
            }
        }
    }

    private void SetRoleSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_role);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spin.setAdapter(adp1);

        for(int i = 0; i < list_role.size (); i++)
        {
            if(list_role.get(i).equals(role))
            {
                role_spin.setSelection(i);
            }
        }
    }

    private void PutEmployee(String person, Person persons)
    {
        Call<Person> call = employeeService.PutEmployee(person, persons);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.getName());
                Toast.makeText(EmployeeActivity.this,"Saving Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetRole()
    {
        for (int i = 0; i < roles.size(); i++)
        {
            if (roles.get(i).getName().equals(role_spin.getSelectedItem()))
            {
                dataperson.Role.setId(roles.get(i).getId());
                dataperson.Role.setName(roles.get(i).getName());
                dataperson.Role.setMaxsalary(roles.get(i).getMaxsalary());
                dataperson.Role.setMinsalary(roles.get(i).getMinsalary());
            }
        }
    }

    private void SetShift()
    {
        for (int i = 0; i < shifts.size(); i++)
        {
            if (shifts.get(i).getId().equals(shift_spin.getSelectedItem()))
            {
                dataperson.Shift.setId(shifts.get(i).getId());
                dataperson.Shift.setWorkstart(shifts.get(i).getWorkstart());
                dataperson.Shift.setWorkend(shifts.get(i).getWorkend());
            }
        }
    }
}
