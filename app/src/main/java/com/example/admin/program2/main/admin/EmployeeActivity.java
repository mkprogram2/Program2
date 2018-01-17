package com.example.admin.program2.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.main.MainActivity;
import com.example.admin.program2.model.Role;
import com.example.admin.program2.model.Shift;
import com.example.admin.program2.model.person;
import com.example.admin.program2.service.EmployeeService;
import com.example.admin.program2.service.WorkhourService;

import java.util.ArrayList;
import java.util.HashMap;
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

    private EmployeeService employeeService;
    private String persons, shiftid, role;
    final List<String> list_shift = new ArrayList<String>();
    final List<String> list_role = new ArrayList<String>();
    private List<Shift> shifts;
    private List<Role> roles;

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

        shift_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(shift_spin.getSelectedItem().toString())) {
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
    }

    private void GetEmployee (String persons, String id)
    {
        Call<person> call = employeeService.GetEmployee(persons, id);
        call.enqueue(new Callback<person>()
        {
            @Override
            public void onResponse(retrofit2.Call<person> call, Response<person> response)
            {
                final person data = response.body();
                Log.d("Data", data.getName());
                shiftid = data.Shift.getId();
                role = data.Role.getName();
            }

            @Override
            public void onFailure(retrofit2.Call<person> call, Throwable t)
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
            if(list_role.get(i).equals(role)) {
                role_spin.setSelection(i);
            }
        }
    }
}
