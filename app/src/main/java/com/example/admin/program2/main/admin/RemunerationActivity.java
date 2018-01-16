package com.example.admin.program2.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.admin.program2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemunerationActivity extends AppCompatActivity {

    @BindView(R.id.employee_id)
    EditText employee_id;
    @BindView(R.id.employee_name)
    EditText employee_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remuneration);

        ButterKnife.bind(this);

        employee_id.setText(getIntent().getExtras().getString("id"));
        employee_name.setText(getIntent().getExtras().getString("name"));
    }
}
