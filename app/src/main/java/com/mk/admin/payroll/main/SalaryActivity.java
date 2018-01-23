package com.mk.admin.payroll.main;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.model.Remuneration;
import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.service.SalaryService;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaryActivity extends AppCompatActivity {

    private SalaryService service4;
    private String persons, mid;
    private Remuneration remuneration = new Remuneration();
    private RackMonthPicker rackMonthPicker;
    private String[] monthinyear = new String[]{"January" , "February", "Maret", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @BindView(R.id.gross_salary)
    TextView gross_salary;
    @BindView(R.id.present_day)
    TextView present_day;
    @BindView(R.id.absent_day)
    TextView absent_day;
    @BindView(R.id.net_salary)
    TextView net_salary;
    @BindView(R.id.workdays)
    TextView workdays;
    @BindView(R.id.basic_salary)
    TextView basic_salary;
    @BindView(R.id.trans_salary)
    TextView trans_salary;
    @BindView(R.id.meal_salary)
    TextView meal_salary;
    @BindView(R.id.com_salary)
    TextView com_salary;
    @BindView(R.id.diligent_salary)
    TextView diligent_salary;
    @BindView(R.id.health_salary)
    TextView health_salary;
    @BindView(R.id.overtime_salary)
    TextView overtime_salary;
    @BindView(R.id.pension_salary)
    TextView pension_salary;
    @BindView(R.id.commision_salary)
    TextView commision_salary;
    @BindView(R.id.paid_month)
    TextView paid_month;
    @BindView(R.id.basic_deduction)
    TextView basic_deduction;
    @BindView(R.id.trans_deduction)
    TextView trans_deduction;
    @BindView(R.id.meal_deduction)
    TextView meal_deduction;
    @BindView(R.id.diligent_deduction)
    TextView diligent_deduction;
    @BindView(R.id.deduction)
    TextView deduction;
    @BindView(R.id.salary)
    Button salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        ButterKnife.bind(this);

        service4 = ClientService.createService().create(SalaryService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        mid = getIntent().getExtras().getString("id");

        EmptyUI();
        RackMonth();

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rackMonthPicker.show();
            }
        });
    }

    private void getSalary (final String persons, String id, Integer month, Integer year)
    {
        Call<Remuneration> call = service4.GetRemuneration(persons, id, month, year);
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
                Toast.makeText(SalaryActivity.this,"Data Not Found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPresent (final String persons, Integer month, Integer year, String id)
    {
        Call<List<Workhour>> call = service4.getDay(persons, month, year, id);
        call.enqueue(new Callback<List<Workhour>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Workhour>> call, Response<List<Workhour>> response)
            {
                final List<Workhour> workhours = response.body();
                present_day.setText(String.valueOf(workhours.size()));
            }

            @Override
            public void onFailure(retrofit2.Call<List<Workhour>> call, Throwable t)
            {
                Toast.makeText(SalaryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void GetWorkdays (final String persons, Integer month, Integer year)
    {
        Call<Integer> call = service4.GetWorkdays(persons, month, year);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer workhours = response.body();
                workdays.setText(workhours.toString());
            }

            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(SalaryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private String setString (double datas)
    {
        String data = String.valueOf(Integer.valueOf(((int) datas)));
        return data;
    }

    private void ShowSalary ()
    {
        if (remuneration.salary != null)
            basic_salary.setText("Rp." + setString(remuneration.salary));
        else
            basic_salary.setText("-");
        if (remuneration.trans != null)
            trans_salary.setText("Rp." + setString(remuneration.trans));
        else
            trans_salary.setText("-");
        if (remuneration.meal != null)
            meal_salary.setText("Rp." + setString(remuneration.meal));
        else
            meal_salary.setText("-");
        if (remuneration.communication != null)
            com_salary.setText("Rp." + setString(remuneration.communication));
        else
            commision_salary.setText("-");
        if (remuneration.diligent != null)
            diligent_salary.setText("Rp." + setString(remuneration.diligent));
        else
            diligent_salary.setText("-");
        if (remuneration.health != null)
            health_salary.setText("Rp." + setString(remuneration.health));
        else
            health_salary.setText("-");
        if (remuneration.overtime != null)
            overtime_salary.setText("Rp." + setString(remuneration.overtime));
        else
            overtime_salary.setText("-");
        if (remuneration.pension != null)
            pension_salary.setText("Rp." + setString(remuneration.pension));
        else
            pension_salary.setText("-");
        if (remuneration.commision != null)
            commision_salary.setText("Rp." + setString(remuneration.commision));
        else
            commision_salary.setText("-");
        if (remuneration.netsalary != null)
            net_salary.setText("Rp." + setString(remuneration.netsalary));
        else
            net_salary.setText("-");
        if (remuneration.minsalary != null)
            basic_deduction.setText("Rp." + setString(remuneration.salary));
        else
            basic_deduction.setText("-");
        if (remuneration.mintrans != null)
            trans_deduction.setText("Rp." + setString(remuneration.mintrans));
        else
            trans_deduction.setText("-");
        if (remuneration.minmeal != null)
            meal_deduction.setText("Rp." + setString(remuneration.minmeal));
        else
            meal_deduction.setText("-");
        if (remuneration.mindiligent != null)
            diligent_deduction.setText("Rp." + setString(remuneration.mindiligent));
        else
            diligent_deduction.setText("-");
    }

    private void RackMonth ()
    {
        rackMonthPicker = new RackMonthPicker(SalaryActivity.this)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        getSalary(persons, mid, month, year);
                        getPresent(persons, month, year, mid);
                        GetWorkdays(persons, month, year);

                        for (int i = 0; i < monthinyear.length; i++)
                        {
                            if ((i + 1) == month)
                            {
                                paid_month.setText(monthinyear[i] + " , " + year);
                            }
                        }
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener()
                {
                    @Override
                    public void onCancel(AlertDialog dialog)
                    {
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
}