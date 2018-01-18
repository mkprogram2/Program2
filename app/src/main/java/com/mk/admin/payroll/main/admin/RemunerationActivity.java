package com.mk.admin.payroll.main.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.SalaryService;

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
    private Person dataperson = new Person();

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
        Call<Person> call = employeeService.GetEmployee(persons, id);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
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
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(RemunerationActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void PutSalary (String person, Person persons)
    {
        Call<Person> call = salaryService.PutSalary(person, persons);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.getName());
                Toast.makeText(RemunerationActivity.this,"Saving Remuneration Was Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
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
