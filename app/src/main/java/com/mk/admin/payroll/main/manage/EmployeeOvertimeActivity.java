package com.mk.admin.payroll.main.manage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.main.employee.fragment.MyOvertimeFragment;
import com.mk.admin.payroll.main.manage.fragment.AddEmployeeOvertime;
import com.mk.admin.payroll.main.manage.fragment.ListEmployeeOvertime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeOvertimeActivity extends AppCompatActivity implements View.OnClickListener
{

    @BindView(R.id.list_employee_over)
    ImageView list_employee_over;
    @BindView(R.id.add_employee_over)
    ImageView add_employee_over;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_overtime);

        ButterKnife.bind(this);
        list_employee_over.setOnClickListener(this);
        add_employee_over.setOnClickListener(this);
        initFragment();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.list_employee_over:
                list_employee_over.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
                add_employee_over.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
                ListEmployeeOvertime listEmployeeOvertime = new ListEmployeeOvertime();
                FragmentManager FM = getSupportFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                FT.replace(R.id.employee_overtime_item, listEmployeeOvertime);
                FT.commit();
                break;
            case R.id.add_employee_over:
                list_employee_over.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
                add_employee_over.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
                AddEmployeeOvertime addEmployeeOvertime = new AddEmployeeOvertime();
                FragmentManager FM2 = getSupportFragmentManager();
                FragmentTransaction FT2 = FM2.beginTransaction();
                FT2.replace(R.id.employee_overtime_item, addEmployeeOvertime);
                FT2.commit();
                break;
        }

    }

    private void initFragment ()
    {
        list_employee_over.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
        add_employee_over.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
        ListEmployeeOvertime listEmployeeOvertime = new ListEmployeeOvertime();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.employee_overtime_item, listEmployeeOvertime);
        FT.commit();
    }
}
