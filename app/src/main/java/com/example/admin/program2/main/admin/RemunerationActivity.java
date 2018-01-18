package com.example.admin.program2.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.main.SalaryActivity;
import com.example.admin.program2.model.person;
import com.example.admin.program2.service.EmployeeService;
import com.example.admin.program2.service.SalaryService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemunerationActivity extends AppCompatActivity {

    @BindView(R.id.employee_id)
    EditText employee_id;
    @BindView(R.id.employee_name)
    EditText employee_name;
    @BindView(R.id.employee_role)
    EditText employee_role;
    @BindView(R.id.basic_salary)
    EditText basic_salary;
    @BindView(R.id.gross_salary)
    TextView gross_salary;
    @BindView(R.id.save_remuneration)
    Button save_remuneration;

    private EmployeeService employeeService;
    private SalaryService salaryService;
    private String persons, mid;
    private person dataperson = new person();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remuneration);

        ButterKnife.bind(this);

        employeeService = ClientService.createService().create(EmployeeService.class);
        salaryService = ClientService.createService().create(SalaryService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        mid = getIntent().getExtras().getString("id").toString();

        GetEmployee(persons, mid);

        save_remuneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPerson();
            }
        });
    }

    private void GetEmployee (String persons, String id)
    {
        Call<person> call = employeeService.GetEmployee(persons, id);
        call.enqueue(new Callback<person>()
        {
            @Override
            public void onResponse(retrofit2.Call<person> call, Response<person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.getName());
                employee_id.setText(dataperson.getId().toString());
                employee_name.setText(dataperson.getName().toString());
                employee_role.setText(dataperson.Role.getName().toString());
                basic_salary.setText(setString(dataperson.Role.getMinsalary()));

                gross_salary.setText(basic_salary.getText());
            }

            @Override
            public void onFailure(retrofit2.Call<person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(RemunerationActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void PutSalary (String person, person persons)
    {
        Call<person> call = salaryService.PutSalary(person, persons);
        call.enqueue(new Callback<person>()
        {
            @Override
            public void onResponse(retrofit2.Call<person> call, Response<person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.getName());
                Toast.makeText(RemunerationActivity.this,"Saving Remuneration Was Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(RemunerationActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String setString (double datas)
    {
        String data = String.valueOf(Integer.valueOf(((int) datas)));
        return data;
    }

    private void SetPerson ()
    {
        dataperson.setSalary(Double.parseDouble(gross_salary.getText().toString()));
        PutSalary(persons, dataperson);
    }
}
