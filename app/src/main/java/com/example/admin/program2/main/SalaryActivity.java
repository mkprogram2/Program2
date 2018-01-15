package com.example.admin.program2.main;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.model.Shift;
import com.example.admin.program2.model.Workhour;
import com.example.admin.program2.service.SalaryService;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaryActivity extends AppCompatActivity {

    private SalaryService service4;
    private String persons, g_salary, mid, months, yearss;

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
    @BindView(R.id.salary)
    Button salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        ButterKnife.bind(this);

        service4 = ClientService.createService().create(SalaryService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        MainActivity main = new MainActivity();
        g_salary = setString(main.gross_salary);
        mid = main.mid;

        final RackMonthPicker rackMonthPicker = new RackMonthPicker(SalaryActivity.this)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                         months = String.valueOf(month);
                         yearss = String.valueOf(year);

                        getSalary(persons, months, yearss, mid);
                        getPresent(persons, months, yearss, mid);
                        GetWorkdays(persons, months, yearss);
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        
                    }
                });

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rackMonthPicker.show();
            }
        });
    }

    private void getSalary (final String persons, String month, String year, String id)
    {
        Call<Double> call = service4.getSalary(persons, month, year, id);
        call.enqueue(new Callback<Double>()
        {
            @Override
            public void onResponse(retrofit2.Call<Double> call, Response<Double> response)
            {
                final double data = response.body();
                net_salary.setText(setString(data));
                if (data == 0.0)
                {
                    gross_salary.setText("0");
                }
                else
                {
                    gross_salary.setText(g_salary);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Double> call, Throwable t)
            {
                Toast.makeText(SalaryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getPresent (final String persons, String month, String year, String id)
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

    private void GetWorkdays (final String persons, String month, String year)
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
}
