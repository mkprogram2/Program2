package com.mk.admin.payroll.main.employee;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.main.employee.fragment.ListOvertimeFragment;
import com.mk.admin.payroll.main.employee.fragment.MyOvertimeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OvertimeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.myover)
    ImageView myover;
    @BindView(R.id.listover)
    ImageView listover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

        ButterKnife.bind(this);

        myover.setOnClickListener(this);
        listover.setOnClickListener(this);
        initFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.listover:
                myover.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
                listover.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
                ListOvertimeFragment listOvertimeFragment = new ListOvertimeFragment();
                FragmentManager FM2 = getSupportFragmentManager();
                FragmentTransaction FT2 = FM2.beginTransaction();
                FT2.replace(R.id.main_menu, listOvertimeFragment);
                FT2.commit();
                break;
            case R.id.myover:
                myover.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
                listover.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
                MyOvertimeFragment myOvertimeFragment = new MyOvertimeFragment();
                FragmentManager FM = getSupportFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                FT.replace(R.id.main_menu, myOvertimeFragment);
                FT.commit();
                break;
        }
    }

    private void initFragment ()
    {
        myover.setColorFilter(getBaseContext().getResources().getColor(R.color.blue));
        listover.setColorFilter(getBaseContext().getResources().getColor(R.color.black_de));
        MyOvertimeFragment myOvertimeFragment = new MyOvertimeFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.main_menu, myOvertimeFragment);
        FT.commit();
    }
}
