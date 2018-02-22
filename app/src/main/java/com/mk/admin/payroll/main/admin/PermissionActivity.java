package com.mk.admin.payroll.main.admin;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.main.admin.fragment.AddPermissionFragment;
import com.mk.admin.payroll.main.admin.fragment.ListPermissionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener
{
    @BindView(R.id.search_permission)
    ImageView search_permission;
    @BindView(R.id.add_permission)
    ImageView add_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);

        initFragment();
        search_permission.setOnClickListener(this);
        add_permission.setOnClickListener(this);
    }

    private void initFragment ()
    {
        ListPermissionFragment listPermissionFragment = new ListPermissionFragment();
        FragmentManager FM2 = getSupportFragmentManager();
        FragmentTransaction FT2 = FM2.beginTransaction();
        FT2.replace(R.id.permission_overtime, listPermissionFragment);
        FT2.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.search_permission:
                search_permission.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
                add_permission.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
                ListPermissionFragment listPermissionFragment = new ListPermissionFragment();
                FragmentManager FM2 = getSupportFragmentManager();
                FragmentTransaction FT2 = FM2.beginTransaction();
                FT2.replace(R.id.permission_overtime, listPermissionFragment);
                FT2.commit();
                break;
            case R.id.add_permission:
                add_permission.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
                search_permission.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
                AddPermissionFragment addPermissionFragment = new AddPermissionFragment();
                FragmentManager FM = getSupportFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                FT.replace(R.id.permission_overtime, addPermissionFragment);
                FT.commit();
                break;

        }
    }
}
