package com.example.admin.program2.main;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaryActivity extends AppCompatActivity {

    private SalaryService service4;
    private String persons, g_salary, mid;

    @BindView(R.id.gross_salary)
    TextView gross_salary;
    @BindView(R.id.present_day)
    TextView present_day;
    @BindView(R.id.absent_day)
    TextView absent_day;
    @BindView(R.id.net_salary)
    TextView net_salary;
    @BindView(R.id.salary)
    Button salary;
    @BindView(R.id.year_spin)
    Spinner year_spin;
    @BindView(R.id.month_spin)
    Spinner month_spin;

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

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        year_spin.setAdapter(adapter);

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gross_salary.setText(g_salary);
                getSalary(persons, "1", "2018", mid);
                getPresent(persons, "1", "2018", mid);
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

    private String setString (double datas)
    {
        String data = String.valueOf(Integer.valueOf(((int) datas)));
        return data;
    }
}
