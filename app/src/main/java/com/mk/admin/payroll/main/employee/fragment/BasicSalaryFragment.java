package com.mk.admin.payroll.main.employee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.model.Remuneration;
import com.mk.admin.payroll.service.SalaryService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2/15/2018.
 */

public class BasicSalaryFragment extends android.support.v4.app.Fragment
{
    private View basicsalaryFrag;
    private Session session;
    private SalaryService salaryService;
    private Remuneration remuneration = new Remuneration();
    private String[] monthinyear = new String[]{"January" , "February", "Maret", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private TextView gross_salary;
    private TextView basic_salary;
    private TextView trans_salary;
    private TextView meal_salary;
    private TextView com_salary;
    private TextView diligent_salary;
    private TextView health_salary;
    private TextView overtime_salary;
    private TextView pension_salary;
    private TextView commision_salary;
    private TextView period;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        basicsalaryFrag = inflater.inflate(R.layout.fragment_basic_salary, container, false);

        session = new Session(basicsalaryFrag.getContext());
        salaryService = ClientService.createService().create(SalaryService.class);

        gross_salary = (TextView)basicsalaryFrag.findViewById(R.id.gross_salary);
        basic_salary = (TextView)basicsalaryFrag.findViewById(R.id.basic_salary);
        trans_salary = (TextView)basicsalaryFrag.findViewById(R.id.trans_salary);
        meal_salary = (TextView)basicsalaryFrag.findViewById(R.id.meal_salary);
        com_salary = (TextView)basicsalaryFrag.findViewById(R.id.com_salary);
        diligent_salary = (TextView)basicsalaryFrag.findViewById(R.id.diligent_salary);
        health_salary = (TextView)basicsalaryFrag.findViewById(R.id.health_salary);
        overtime_salary = (TextView)basicsalaryFrag.findViewById(R.id.overtime_salary);
        pension_salary = (TextView)basicsalaryFrag.findViewById(R.id.pension_salary);
        commision_salary = (TextView)basicsalaryFrag.findViewById(R.id.commision_salary);
        period = (TextView)basicsalaryFrag.findViewById(R.id.period);

        Calendar instance = Calendar.getInstance();
        Integer currentMonth = instance.get(Calendar.MONTH);
        Integer currentYear = instance.get(Calendar.YEAR);

        getSalary(session.getId(), currentMonth + 1, currentYear);

        for (int i = 0; i < monthinyear.length; i++)
        {
            if (currentMonth == i)
            {
                period.setText(monthinyear[i].toString() + " " + currentYear.toString());
                break;
            }
        }

        return basicsalaryFrag;
    }

    private void getSalary (String id, Integer month, Integer year)
    {
        Call<Remuneration> call = salaryService.GetRemuneration(id, month, year, session.getAccesstoken());
        call.enqueue(new Callback<Remuneration>()
        {
            @Override
            public void onResponse(retrofit2.Call<Remuneration> call, Response<Remuneration> response)
            {
                if (response.isSuccessful())
                {
                    remuneration = response.body();
                    ShowSalary();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Remuneration> call, Throwable t)
            {
                Toast.makeText(basicsalaryFrag.getContext(),"Data Not Found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ShowSalary ()
    {
        basic_salary.setText("Rp." + setString(remuneration.salary));
        trans_salary.setText("Rp." + setString(remuneration.trans));
        meal_salary.setText("Rp." + setString(remuneration.meal));
        com_salary.setText("Rp." + setString(remuneration.communication));
        diligent_salary.setText("Rp." + setString(remuneration.diligent));
        health_salary.setText("Rp." + setString(remuneration.health));
        overtime_salary.setText("Rp." + setString(remuneration.overtime));
        pension_salary.setText("Rp." + setString(remuneration.pension));
        commision_salary.setText("Rp." + setString(remuneration.commision));
        gross_salary.setText("Rp. " + setString(remuneration.income));
    }

    private String setString (double datas)
    {
        String data = String.valueOf(Integer.valueOf(((int) datas)));
        return data;
    }
}
