package com.mk.admin.payroll.main.employee.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.model.Remuneration;
import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.service.SalaryService;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2/15/2018.
 */

public class MonthlySalaryFragment extends android.support.v4.app.Fragment
{

    private View monthsalaryFrag;
    private SalaryService service4;
    private Integer present_days, work_days, months, years, month_now;
    private Remuneration remuneration = new Remuneration();
    private RackMonthPicker rackMonthPicker;
    private Session session;
    private String[] monthinyear = new String[]{"January" , "February", "Maret", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private TextView gross_salary;
    private TextView present_day;
    private TextView absent_day;
    private TextView net_salary;
    private TextView workdays;
    private TextView basic_salary;
    private TextView trans_salary;
    private TextView meal_salary;
    private TextView com_salary;
    private TextView diligent_salary;
    private TextView health_salary;
    private TextView overtime_salary;
    private TextView pension_salary;
    private TextView commision_salary;
    private TextView paid_month;
    private TextView basic_deduction;
    private TextView trans_deduction;
    private TextView meal_deduction;
    private TextView diligent_deduction;
    private TextView deduction;
    private TextView period;
    private Button salary;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        monthsalaryFrag = inflater.inflate(R.layout.fragment_monthly_salary, container, false);

        session = new Session(monthsalaryFrag.getContext());
        service4 = ClientService.createService().create(SalaryService.class);

        gross_salary = (TextView)monthsalaryFrag.findViewById(R.id.gross_salary);
        present_day = (TextView)monthsalaryFrag.findViewById(R.id.present_day);
        absent_day = (TextView)monthsalaryFrag.findViewById(R.id.absent_day);
        net_salary = (TextView)monthsalaryFrag.findViewById(R.id.net_salary);
        workdays = (TextView)monthsalaryFrag.findViewById(R.id.workdays);
        basic_salary = (TextView)monthsalaryFrag.findViewById(R.id.basic_salary);
        trans_salary = (TextView)monthsalaryFrag.findViewById(R.id.trans_salary);
        meal_salary = (TextView)monthsalaryFrag.findViewById(R.id.meal_salary);
        com_salary = (TextView)monthsalaryFrag.findViewById(R.id.com_salary);
        diligent_salary = (TextView)monthsalaryFrag.findViewById(R.id.diligent_salary);
        health_salary = (TextView)monthsalaryFrag.findViewById(R.id.health_salary);
        overtime_salary = (TextView)monthsalaryFrag.findViewById(R.id.overtime_salary);
        pension_salary = (TextView)monthsalaryFrag.findViewById(R.id.pension_salary);
        commision_salary = (TextView)monthsalaryFrag.findViewById(R.id.commision_salary);
        paid_month = (TextView)monthsalaryFrag.findViewById(R.id.paid_month);
        basic_deduction = (TextView)monthsalaryFrag.findViewById(R.id.basic_deduction);
        trans_deduction = (TextView)monthsalaryFrag.findViewById(R.id.trans_deduction);
        meal_deduction = (TextView)monthsalaryFrag.findViewById(R.id.meal_deduction);
        diligent_deduction = (TextView)monthsalaryFrag.findViewById(R.id.diligent_deduction);
        deduction = (TextView)monthsalaryFrag.findViewById(R.id.deduction);
        period = (TextView)monthsalaryFrag.findViewById(R.id.period);
        salary = (Button)monthsalaryFrag.findViewById(R.id.salary);

        EmptyUI();
        RackMonth();

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rackMonthPicker.show();
            }
        });

        return monthsalaryFrag;
    }

    private void getSalary (String id, Integer month, Integer year)
    {
        Call<Remuneration> call = service4.GetRemuneration(id, month, year, session.getAccesstoken());
        call.enqueue(new Callback<Remuneration>()
        {
            @Override
            public void onResponse(retrofit2.Call<Remuneration> call, Response<Remuneration> response)
            {
                remuneration = response.body();
                ShowSalary();
            }

            @Override
            public void onFailure(retrofit2.Call<Remuneration> call, Throwable t)
            {
                EmptyUI();
                Toast.makeText(monthsalaryFrag.getContext(),"Data Not Found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPresent (Integer month, Integer year, String id)
    {
        Call<List<Workhour>> call = service4.getDay(month, year, id, session.getAccesstoken());
        call.enqueue(new Callback<List<Workhour>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Workhour>> call, Response<List<Workhour>> response)
            {
                final List<Workhour> workhours = response.body();
                //present_days = workhours.size();
                Integer temp = 0;
                for (int i = 0; i < workhours.size(); i++)
                {
                    if (workhours.get(i).workend != null)
                    {
                        temp++;
                    }
                }
                present_days = temp;
                Log.d("PRESENT", present_days.toString());
                present_day.setText(String.valueOf(present_days));
                if (months == month_now)
                {
                    GetWorkdaysToday(months, years);
                }
                else
                {
                    GetWorkdays(months, years);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Workhour>> call, Throwable t)
            {
                Toast.makeText(monthsalaryFrag.getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void GetWorkdays (Integer month, Integer year)
    {
        Call<Integer> call = service4.GetWorkdays(month, year, session.getAccesstoken());
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer workhours = response.body();
                work_days = workhours;
                if (present_days == null)
                {
                    present_days = 0;
                }
                absent_day.setText(String.valueOf(work_days - present_days));
                workdays.setText(workhours.toString());
                getSalary(session.getId(), months, years);
            }

            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(monthsalaryFrag.getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void GetWorkdaysToday (Integer month, Integer year)
    {
        Call<Integer> call = service4.GetWorkdaysToday(month, year, session.getAccesstoken());
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer workhours = response.body();
                work_days = workhours;
                if (present_days == null)
                {
                    present_days = 0;
                }
                absent_day.setText(String.valueOf(work_days - present_days));
                workdays.setText(workhours.toString());
                getSalary(session.getId(), months, years);
            }

            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(monthsalaryFrag.getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void ShowSalary ()
    {
        basic_salary.setText("Rp." + HitungSalary(remuneration.salary).toString());
        trans_salary.setText("Rp." + HitungSalary(remuneration.trans).toString());
        meal_salary.setText("Rp." + HitungSalary(remuneration.meal).toString());
        com_salary.setText("Rp." + HitungSalary(remuneration.communication).toString());
        diligent_salary.setText("Rp." + HitungSalary(remuneration.diligent).toString());
        health_salary.setText("Rp." + HitungSalary(remuneration.health).toString());
        overtime_salary.setText("Rp." + HitungSalary(remuneration.overtime).toString());
        pension_salary.setText("Rp." + HitungSalary(remuneration.pension).toString());
        commision_salary.setText("Rp." + HitungSalary(remuneration.commision).toString());
        net_salary.setText("Rp." + HitungSalary(remuneration.netsalary).toString());
        basic_deduction.setText("Rp." + HitungSalary(remuneration.minsalary).toString());
        trans_deduction.setText("Rp." + HitungSalary(remuneration.mintrans).toString());
        meal_deduction.setText("Rp." + HitungSalary(remuneration.minmeal).toString());
        diligent_deduction.setText("Rp." + HitungSalary(remuneration.mindiligent).toString());
        gross_salary.setText("Rp. " + HitungSalary(remuneration.income).toString());
        deduction.setText("Rp. " + HitungSalary(remuneration.deduction).toString());
    }

    private void RackMonth ()
    {
        rackMonthPicker = new RackMonthPicker(monthsalaryFrag.getContext())
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {

                        Calendar instance = Calendar.getInstance();
                        Integer currentMonth = instance.get(Calendar.MONTH);
                        Integer currentYear = instance.get(Calendar.YEAR);
                        Integer currentDay = instance.get(Calendar.DATE);

                        for (int i = 0; i < monthinyear.length; i++)
                        {
                            if ((i + 1) == month)
                            {
                                paid_month.setText(monthinyear[i] + " , " + year);
                                if (currentDay == 1)
                                    period.setText("-");
                                else
                                    period.setText("1 " + " - " + (currentDay - 1) + " " + monthinyear[i] + " " + currentYear);
                            }
                        }

                        months = month;
                        years = year;
                        month_now = currentMonth + 1;

                        getPresent(month, year, session.getId());

                        //getSalary(session.getId(), month, year);
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener()
                {
                    @Override
                    public void onCancel(AlertDialog dialog)
                    {
                        rackMonthPicker.dismiss();
                    }
                });
    }

    private void EmptyUI ()
    {
        net_salary.setText("-");
        basic_salary.setText("-");
        trans_salary.setText("-");
        meal_salary.setText("-");
        com_salary.setText("-");
        diligent_salary.setText("-");
        health_salary.setText("-");
        overtime_salary.setText("-");
        pension_salary.setText("-");
        commision_salary.setText("-");
        gross_salary.setText("-");
        basic_deduction.setText("-");
        trans_deduction.setText("-");
        meal_deduction.setText("-");
        diligent_deduction.setText("-");
        deduction.setText("-");
    }

    private Integer HitungSalary (Double salary)
    {
        Double hitungsalary = (Double.valueOf(present_days) / Double.valueOf(work_days)) * salary;
        Integer hasil = hitungsalary.intValue();
        return hasil;
    }
}
