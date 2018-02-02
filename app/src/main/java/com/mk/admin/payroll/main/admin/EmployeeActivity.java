package com.mk.admin.payroll.main.admin;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.HomeActivity;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.main.manage.OvertimeRequestActivity;
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
    ImageView save_employee;
    @BindView(R.id.edit_employee)
    ImageView edit_employee;
    @BindView(R.id.cancel_edit)
    ImageView cancel_edit;
    @BindView(R.id.select_employee)
    Button select_employee;
    @BindView(R.id.add_employee)
    Button add_employee;

    private EmployeeService employeeService;
    private String persons, shiftid, role, mid, mname;
    final List<String> list_shift = new ArrayList<String>();
    final List<String> list_role = new ArrayList<String>();
    private List<Shift> shifts = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Person dataperson;
    private RecyclerView rvCategory;
    private List<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        ButterKnife.bind(this);

        DisableUI();

        employeeService = ClientService.createService().create(EmployeeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        shift_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(shift_spin.getSelectedItem().toString()))
                    {
                        shift_in.setText(shifts.get(i).workstart);
                        shift_out.setText(shifts.get(i).workend);
                        Toast.makeText(EmployeeActivity.this,shifts.get(i).workstart, Toast.LENGTH_LONG).show();
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
                if (mid == null)
                {
                    select_employee.setError("Please Select Employee");
                }
                else
                {
                    dataperson.name = employee_name.getText().toString();
                    SetRole();
                    SetShift();
                    PutEmployee(persons, dataperson);
                }
            }
        });

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

        select_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPerson(persons);
            }
        });

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeActivity.this, AddEmployeeActivity.class));
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
                Log.d("Data", dataperson.name);
                shiftid = dataperson.PersonDetail.Shift.id;
                role = dataperson.Role.name;
                GetShift();
                GetRole();
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
                shifts.clear();
                list_shift.clear();
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
                roles.clear();
                list_role.clear();
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
                Log.d("Data", dataperson.name);
                Toast.makeText(EmployeeActivity.this,"Saving Success", Toast.LENGTH_LONG).show();
                DisableUI();
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
            if (roles.get(i).name.equals(role_spin.getSelectedItem()))
            {
                dataperson.Role.id = roles.get(i).id;
                dataperson.Role.name = roles.get(i).name;
                dataperson.Role.RoleDetail.maxsalary = roles.get(i).RoleDetail.maxsalary;
                dataperson.Role.RoleDetail.minsalary = roles.get(i).RoleDetail.minsalary;
            }
        }
    }

    private void SetShift()
    {
        for (int i = 0; i < shifts.size(); i++)
        {
            if (shifts.get(i).id.equals(shift_spin.getSelectedItem()))
            {
                dataperson.PersonDetail.Shift.id = shifts.get(i).id;
                dataperson.PersonDetail.Shift.workstart = shifts.get(i).workstart;
                dataperson.PersonDetail.Shift.workend = shifts.get(i).workend;
            }
        }
    }

    private void GetPerson (String persons)
    {
        Call<List<Person>> call = employeeService.GetPerson(persons);
        call.enqueue(new Callback<List<Person>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Person>> call, Response<List<Person>> response)
            {
                list = response.body();
                Log.d("Data",list.get(0).name);
                Log.d("Size", String.valueOf(list.size()));
                showRecyclerList();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Person>> call, Throwable t)
            {
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList()
    {
        // custom dialog
        final Dialog dialog = new Dialog(EmployeeActivity.this);
        dialog.setContentView(R.layout.employee_list_dialog);
        dialog.setTitle("Employee");

        dialog.show();

        rvCategory = (RecyclerView)dialog.findViewById(R.id.rv_employees);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(EmployeeActivity.this));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(EmployeeActivity.this);
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
        mname = person.name;

        employee_id.setText(mid);
        employee_name.setText(mname);

        GetEmployee(persons, mid);
    }

    private void DisableUI ()
    {
        employee_id.setEnabled(false);
        employee_name.setEnabled(false);
        role_spin.setEnabled(false);
        shift_spin.setEnabled(false);
        save_employee.setVisibility(View.GONE);
        cancel_edit.setVisibility(View.GONE);
        edit_employee.setVisibility(View.VISIBLE);
    }

    private void EnableUI ()
    {
        edit_employee.setVisibility(View.GONE);
        save_employee.setVisibility(View.VISIBLE);
        cancel_edit.setVisibility(View.VISIBLE);
        role_spin.setEnabled(true);
        shift_spin.setEnabled(true);
    }
}
