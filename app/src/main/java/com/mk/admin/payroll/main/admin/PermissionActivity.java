package com.mk.admin.payroll.main.admin;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.main.admin.fragment.ListPermissionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionActivity extends AppCompatActivity implements ListPermissionFragment.OnFragmentInteractionListener
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

        /*search_permission.setOnClickListener(this);
        add_permission.setOnClickListener(this);*/
        initFragment();
    }

    /*@Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.search_permission:
                ListPermissionFragment listPermissionFragment = new ListPermissionFragment();
                FragmentManager FM2 = getSupportFragmentManager();
                FragmentTransaction FT2 = FM2.beginTransaction();
                FT2.replace(R.id.main_menu, listPermissionFragment);
                FT2.commit();
                break;
        }
    }*/

    private void initFragment ()
    {
        ListPermissionFragment listPermissionFragment = new ListPermissionFragment();
        FragmentManager FM2 = getSupportFragmentManager();
        FragmentTransaction FT2 = FM2.beginTransaction();
        FT2.replace(R.id.permission_overtime, listPermissionFragment);
        FT2.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
