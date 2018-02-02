package com.mk.admin.payroll.main.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
//import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.main.manage.OvertimeRequestActivity;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRecyclerActivity extends AppCompatActivity {

    private RecyclerView rvCategory;
    private List<Person> list;
    private EmployeeService employeeService;
    private String persons, activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_recycler);

        employeeService = ClientService.createService().create(EmployeeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        rvCategory = (RecyclerView)findViewById(R.id.rv_employee);
        rvCategory.setHasFixedSize(true);

        GetPerson(persons);
    }

    private void showRecyclerList()
    {
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(this);
        EmployeeAdapter.setListPerson(list);
        rvCategory.setAdapter(EmployeeAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(list.get(position));
            }
        });
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
                Toast.makeText(EmployeeRecyclerActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSelectedPerson(Person person)
    {
        Toast.makeText(this, "You Choose "+person.name, Toast.LENGTH_SHORT).show();

        activity = getIntent().getExtras().getString("activity");

        if (activity.equals("employee"))
        {
            Intent intent = new Intent(EmployeeRecyclerActivity.this, EmployeeActivity.class);
            intent.putExtra("id", person.id);
            intent.putExtra("name", person.name);
            startActivity(intent);
        }
        else if(activity.equals("remuneration"))
        {
            Intent intent = new Intent(EmployeeRecyclerActivity.this, RemunerationActivity.class);
            intent.putExtra("id", person.id);
            intent.putExtra("name", person.name);
            startActivity(intent);
        }
        else if (activity.equals("reqovertime"))
        {
            Intent intent = new Intent(EmployeeRecyclerActivity.this, OvertimeRequestActivity.class);
            intent.putExtra("id", person.id);
            intent.putExtra("name", person.name);
            intent.putExtra("role", person.Role.name);
            startActivity(intent);
        }
    }
}