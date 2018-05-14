package com.mk.admin.payroll.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mk.admin.payroll.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPermission extends AppCompatActivity {

    @BindView (R.id.back_icon)
    ImageView back_icon;
    @BindView(R.id.employee_name)
    EditText employee_name;
    @BindView(R.id.permission_date)
    EditText permission_date;
    @BindView(R.id.permission_information)
    EditText permission_information;
    @BindView(R.id.save_permission)
    Button save_permission;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_permission);
        ButterKnife.bind(this);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
