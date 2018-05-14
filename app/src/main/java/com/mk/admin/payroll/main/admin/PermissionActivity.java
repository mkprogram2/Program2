package com.mk.admin.payroll.main.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mk.admin.payroll.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionActivity extends AppCompatActivity
{
    @BindView(R.id.back_icon)
    ImageView back_icon;
    @BindView(R.id.add_permission)
    ImageView add_permission;
    @BindView(R.id.search_toolbar_permission)
    ImageView search_toolbar_permission;
    @BindView(R.id.close_search_permission)
    ImageView close_search_permission;
    @BindView(R.id.search_employee_permission)
    EditText search_employee_permission;
    @BindView(R.id.main_toolbar)
    LinearLayout main_toolbar;
    @BindView(R.id.search_toolbar)
    LinearLayout search_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });

        search_toolbar_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                main_toolbar.setVisibility(View.GONE);
                search_toolbar.setVisibility(View.VISIBLE);
            }
        });

        close_search_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                search_toolbar.setVisibility(View.GONE);
                main_toolbar.setVisibility(View.VISIBLE);
            }
        });

        add_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(PermissionActivity.this, AddPermission.class));
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
