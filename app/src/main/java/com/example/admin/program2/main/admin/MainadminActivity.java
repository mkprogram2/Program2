package com.example.admin.program2.main.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.admin.program2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainadminActivity extends AppCompatActivity {

    @BindView(R.id.remuneration)
    Button remuneration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainadmin);

        ButterKnife.bind(this);

        remuneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainadminActivity.this, EmployeeRecyclerActivity.class));
            }
        });
    }
}
