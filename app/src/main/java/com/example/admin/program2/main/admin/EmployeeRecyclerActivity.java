package com.example.admin.program2.main.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.main.LoginActivity;
import com.example.admin.program2.main.MainActivity;
import com.example.admin.program2.main.admin.adapter.EmployeeAdapter;
import com.example.admin.program2.main.admin.adapter.ItemClickSupport;
import com.example.admin.program2.model.person;
import com.example.admin.program2.service.EmployeeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRecyclerActivity extends AppCompatActivity {

    private RecyclerView rvCategory;
    private List<person> list;
    private EmployeeService employeeService;
    private String persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_recycler);

        employeeService = ClientService.createService().create(EmployeeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        rvCategory = (RecyclerView)findViewById(R.id.rv_employee);
        rvCategory.setHasFixedSize(true);

        GetPerson(persons);
    }

    private void showRecyclerList(){
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
        Call<List<person>> call = employeeService.GetPerson(persons);
        call.enqueue(new Callback<List<person>>()
        {
            private String responseCode;
            private String responseMessage;

            @Override
            public void onResponse(retrofit2.Call<List<person>> call, Response<List<person>> response)
            {
                list = response.body();
                Log.d("Data",list.get(0).getName());
                Log.d("Size", String.valueOf(list.size()));
                showRecyclerList();
            }

            @Override
            public void onFailure(retrofit2.Call<List<person>> call, Throwable t)
            {
                Toast.makeText(EmployeeRecyclerActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSelectedPerson(person person){
        Toast.makeText(this, "Kamu memilih "+person.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EmployeeRecyclerActivity.this, RemunerationActivity.class);
        intent.putExtra("id", person.getId());
        intent.putExtra("name", person.getName());
        startActivity(intent);
    }
}
