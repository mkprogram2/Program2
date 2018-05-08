package com.mk.admin.payroll.main.admin;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.HomeActivity;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.model.Remuneration;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.SalaryService;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.trans)
    EditText trans;
    @BindView(R.id.meal)
    EditText meal;
    @BindView(R.id.communication)
    EditText communication;
    @BindView(R.id.diligent)
    EditText diligent;
    @BindView(R.id.health)
    EditText health;
    @BindView(R.id.overtime)
    EditText overtime;
    @BindView(R.id.pension)
    EditText pension;
    @BindView(R.id.commision)
    EditText commision;
    @BindView(R.id.month_salary)
    TextView month_salary;
    @BindView(R.id.gross_salary)
    TextView gross_salary;
    @BindView(R.id.range_salary)
    TextView range_salary;
    @BindView(R.id.save_remuneration)
    ImageView save_remuneration;
    @BindView(R.id.cancel_remuneration)
    ImageView cancel_remuneration;
    @BindView(R.id.set_date)
    LinearLayout set_date;
    @BindView(R.id.trans_daily)
    RadioButton trans_daily;
    @BindView(R.id.trans_fixed)
    RadioButton trans_fixed;
    @BindView(R.id.meal_daily)
    RadioButton meal_daily;
    @BindView(R.id.meal_fixed)
    RadioButton meal_fixed;
    @BindView(R.id.select_employee)
    LinearLayout select_employee;
    @BindView(R.id.back_icon)
    ImageView back_icon;

    private EmployeeService employeeService;
    private SalaryService salaryService;
    private String mid, mname;
    private Integer months, yearss;
    private Person dataperson = new Person();
    private Remuneration remuneration = new Remuneration();
    private RackMonthPicker rackMonthPicker;
    private String[] monthinyear = new String[]{"January" , "February", "Maret", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private RecyclerView rvCategory;
    private List<Person> list;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remuneration);
        session = new Session(this);
        ButterKnife.bind(this);

        employeeService = ClientService.createService().create(EmployeeService.class);
        salaryService = ClientService.createService().create(SalaryService.class);

        employee_id.setEnabled(false);
        employee_name.setEnabled(false);
        employee_role.setEnabled(false);
        save_remuneration.setVisibility(View.GONE);
        cancel_remuneration.setVisibility(View.GONE);

        EmptyUI();
        CheckRadio();

        save_remuneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(month_salary.getText()))
                {
                    month_salary.setError("Please Select Date");
                    Toast.makeText(RemunerationActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SetRemuneration();
                }
            }
        });

        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDate();
                rackMonthPicker.show();
            }
        });

        select_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPerson();
            }
        });

        cancel_remuneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_remuneration.setVisibility(View.GONE);
                cancel_remuneration.setVisibility(View.GONE);
                select_employee.setVisibility(View.VISIBLE);
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void GetEmployee (String id)
    {
        Call<Person> call = employeeService.GetEmployee(id, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.name);
                employee_id.setText(dataperson.id.toString());
                employee_name.setText(dataperson.name.toString());
                employee_role.setText(dataperson.Role.name.toString());
                range_salary.setText("Rp." + setString(dataperson.Role.RoleDetail.minsalary) + " - Rp." + setString(dataperson.Role.RoleDetail.maxsalary));
                gross_salary.setText(basic_salary.getText());

                EdittextChange();
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

    private void SetDate ()
    {
        rackMonthPicker = new RackMonthPicker(RemunerationActivity.this)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        months = month;
                        yearss = year;

                        GetRemuneration(mid, month, year);

                        for (int i = 0; i < monthinyear.length; i++)
                        {
                            if ((i + 1) == month)
                            {
                                month_salary.setText(monthinyear[i] + " , " + yearss);
                            }
                        }
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {

                    }
                });
    }

    private void GetRemuneration (String id, Integer month, Integer year)
    {
        Call<Remuneration> call = salaryService.GetRemuneration(id, month, year, session.getAccesstoken());
        call.enqueue(new Callback<Remuneration>()
        {
            @Override
            public void onResponse(retrofit2.Call<Remuneration> call, Response<Remuneration> response)
            {
                remuneration = response.body();
                Log.d("tesss", remuneration.id.toString());

                employee_id.setText(remuneration.person.id.toString());
                employee_name.setText(remuneration.person.name.toString());
                employee_role.setText(remuneration.person.Role.name.toString());
                range_salary.setText("Rp." + setString(remuneration.person.Role.RoleDetail.minsalary) + " - Rp." + setString(remuneration.person.Role.RoleDetail.maxsalary));

                if (remuneration.salary != null)
                    basic_salary.setText(setString(remuneration.salary));
                if (remuneration.trans != null)
                    trans.setText(setString(remuneration.trans));
                if (remuneration.meal != null)
                    meal.setText(setString(remuneration.meal));
                if (remuneration.communication != null)
                    communication.setText(setString(remuneration.communication));
                if (remuneration.diligent != null)
                    diligent.setText(setString(remuneration.diligent));
                if (remuneration.health != null)
                    health.setText(setString(remuneration.health));
                if (remuneration.overtime != null)
                    overtime.setText(setString(remuneration.overtime));
                if (remuneration.pension != null)
                    pension.setText(setString(remuneration.pension));
                if (remuneration.commision != null)
                    commision.setText(setString(remuneration.commision));
                if (remuneration.income != null)
                    gross_salary.setText(setString(remuneration.income));
            }

            @Override
            public void onFailure(retrofit2.Call<Remuneration> call, Throwable t)
            {
                EmptyUI();
                remuneration.id = null;
                remuneration.income = 0.0;
                remuneration.minsalary = 0.0;
                remuneration.mintrans = 0.0;
                remuneration.minmeal = 0.0;
                remuneration.mindiligent = 0.0;
                remuneration.deduction = 0.0;
                remuneration.netsalary = 0.0;
                remuneration.person.id = mid;
                Toast.makeText(RemunerationActivity.this,"Make New Remuneration!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetRemuneration ()
    {
        remuneration.salary = Double.parseDouble(basic_salary.getText().toString());
        remuneration.trans = Double.parseDouble(trans.getText().toString());
        remuneration.meal = Double.parseDouble(meal.getText().toString());
        remuneration.communication = Double.parseDouble(communication.getText().toString());
        remuneration.diligent = Double.parseDouble(diligent.getText().toString());
        remuneration.health = Double.parseDouble(health.getText().toString());
        remuneration.overtime = Double.parseDouble(overtime.getText().toString());
        remuneration.pension = Double.parseDouble(pension.getText().toString());
        remuneration.commision = Double.parseDouble(commision.getText().toString());
        remuneration.income = Double.parseDouble(gross_salary.getText().toString());
        remuneration.month = months;
        remuneration.year = yearss;
        PostRemuneration(remuneration);
    }

    private void PostRemuneration (Remuneration remunerations)
    {
        Call<Remuneration> call = salaryService.PostRemuneration(remunerations, session.getAccesstoken());
        call.enqueue(new Callback<Remuneration>()
        {
            @Override
            public void onResponse(retrofit2.Call<Remuneration> call, Response<Remuneration> response)
            {
                remuneration = response.body();
                Toast.makeText(RemunerationActivity.this,"Remuneration Successfully Saved", Toast.LENGTH_LONG).show();
                save_remuneration.setVisibility(View.GONE);
                cancel_remuneration.setVisibility(View.GONE);
                select_employee.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(retrofit2.Call<Remuneration> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(RemunerationActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void EmptyUI ()
    {
            basic_salary.setText("0");
            trans.setText("0");
            meal.setText("0");
            communication.setText("0");
            diligent.setText("0");
            health.setText("0");
            overtime.setText("0");
            pension.setText("0");
            commision.setText("0");
            gross_salary.setText("0");
    }

    private Integer GrossSalary ()
    {
        Integer grosssalary = Integer.parseInt(basic_salary.getText().toString()) +
                            Integer.parseInt(trans.getText().toString()) +
                            Integer.parseInt(meal.getText().toString()) +
                            Integer.parseInt(communication.getText().toString()) +
                            Integer.parseInt(diligent.getText().toString()) +
                            Integer.parseInt(health.getText().toString()) +
                            Integer.parseInt(overtime.getText().toString()) +
                            Integer.parseInt(pension.getText().toString()) +
                            Integer.parseInt(commision.getText().toString());
        return grosssalary;
    }

    private void EdittextChange ()
    {
        basic_salary.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (basic_salary.getText().length() == 0)
                    basic_salary.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        trans.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (trans.getText().length() == 0)
                    trans.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        meal.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (meal.getText().length() == 0)
                    meal.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        communication.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (communication.getText().length() == 0)
                    communication.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        diligent.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (diligent.getText().length() == 0)
                    diligent.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        health.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (health.getText().length() == 0)
                    health.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        overtime.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (overtime.getText().length() == 0)
                    overtime.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        pension.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (pension.getText().length() == 0)
                    pension.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

        commision.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (commision.getText().length() == 0)
                    commision.setText("0");
                Integer grosssalary = GrossSalary();
                gross_salary.setText(grosssalary.toString());
            }
        });

    }

    private void CheckRadio ()
    {
        trans_fixed.setChecked(true);
        trans_fixed.setSelected(true);
        meal_fixed.setChecked(true);
        meal_fixed.setSelected(true);

        trans_daily.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((trans_daily.isSelected())){
                    trans_daily.setChecked(false);
                    trans_daily.setSelected(false);
                    trans_fixed.setChecked(true);
                    trans_fixed.setSelected(true);
                } else {
                    trans_daily.setChecked(true);
                    trans_daily.setSelected(true);
                    trans_fixed.setChecked(false);
                    trans_fixed.setSelected(false);
                }
            }
        });

        trans_fixed.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((trans_fixed.isSelected())){
                    trans_daily.setChecked(true);
                    trans_daily.setSelected(true);
                    trans_fixed.setChecked(false);
                    trans_fixed.setSelected(false);
                } else {
                    trans_daily.setChecked(false);
                    trans_daily.setSelected(false);
                    trans_fixed.setChecked(true);
                    trans_fixed.setSelected(true);
                }
            }
        });

        meal_daily.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((meal_daily.isSelected())){
                    meal_daily.setChecked(false);
                    meal_daily.setSelected(false);
                    meal_fixed.setChecked(true);
                    meal_fixed.setSelected(true);
                } else {
                    meal_daily.setChecked(true);
                    meal_daily.setSelected(true);
                    meal_fixed.setChecked(false);
                    meal_fixed.setSelected(false);
                }
            }
        });

        meal_fixed.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((meal_fixed.isSelected())){
                    meal_daily.setChecked(true);
                    meal_daily.setSelected(true);
                    meal_fixed.setChecked(false);
                    meal_fixed.setSelected(false);
                } else {
                    meal_daily.setChecked(false);
                    meal_daily.setSelected(false);
                    meal_fixed.setChecked(true);
                    meal_fixed.setSelected(true);
                }
            }
        });
    }

    private void GetPerson ()
    {
        Call<List<Person>> call = employeeService.GetPerson(session.getAccesstoken());
        call.enqueue(new Callback<List<Person>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Person>> call, Response<List<Person>> response)
            {
                list = response.body();
                Log.d("Data",list.get(0).name);
                Log.d("Size", String.valueOf(list.size()));
                showRecyclerList();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Person>> call, Throwable t)
            {
                Toast.makeText(RemunerationActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList()
    {
        // custom dialog
        final Dialog dialog = new Dialog(RemunerationActivity.this);
        dialog.setContentView(R.layout.employee_list_dialog);
        dialog.setTitle("Employee");

        dialog.show();

        rvCategory = (RecyclerView)dialog.findViewById(R.id.rv_employees);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(RemunerationActivity.this));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(RemunerationActivity.this);
        EmployeeAdapter.setListPerson(list);
        rvCategory.setAdapter(EmployeeAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(list.get(position));
                dialog.dismiss();
            }
        });
    }

    private void showSelectedPerson(Person person)
    {
        mid = person.id;
        mname = person.name;

        employee_id.setText(mid);
        employee_name.setText(mname);
        save_remuneration.setVisibility(View.VISIBLE);
        cancel_remuneration.setVisibility(View.VISIBLE);
        select_employee.setVisibility(View.GONE);

        GetEmployee(mid);
    }
}