package com.example.admin.program2.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.admin.program2.R;
import com.example.admin.program2.main.admin.adapter.EmployeeAdapter;
import com.example.admin.program2.model.person;

import java.util.ArrayList;

public class EmployeeRecyclerActivity extends AppCompatActivity {

    private RecyclerView rvCategory;
    private ArrayList<person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_recycler);

        rvCategory = (RecyclerView)findViewById(R.id.rv_employee);
        rvCategory.setHasFixedSize(true);

        /*list = new ArrayList<>();
        list.addAll(PresidentData.getListData());*/

        showRecyclerList();
    }

    private void showRecyclerList(){
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(this);
        EmployeeAdapter.setListPerson(list);
        rvCategory.setAdapter(EmployeeAdapter);
    }
}
