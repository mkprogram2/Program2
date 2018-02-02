package com.mk.admin.payroll.main.manage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.IDs;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.admin.EmployeeRecyclerActivity;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.OvertimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOvertimeActivity extends AppCompatActivity {


    @BindView(R.id.employee_name)
    TextView employee_name;
    @BindView(R.id.employee_role)
    TextView employee_role;
    @BindView(R.id.date_overtime)
    EditText date_overtime;
    @BindView(R.id.duration)
    EditText duration;
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.overtime_calendar)
    Button overtime_calendar;
    @BindView(R.id.save_overtime)
    Button save_overtime;

    DatePickerDialog datePickerDialog;
    private String mid, mname, persons;
    private OvertimeService overtimeService;
    private Overtime overtime = new Overtime();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_overtime);

        ButterKnife.bind (this);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        mid = getIntent().getExtras().getString("id");
        mname = getIntent().getExtras().getString("name");

        employee_name.setText(mname);

        MakeDatePicker();

        overtime_calendar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });

        save_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duration.getText().length() == 0)
                {
                    Toast.makeText(AddOvertimeActivity.this,"Overtime Limit Max 3 Hours!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SetOvertime();
                }
            }
        });
    }

    private void MakeDatePicker ()
    {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(AddOvertimeActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(newDate.getTime());
                date_overtime.setText(date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void PostOvertime (String persons, Overtime overtimes)
    {
        Call<Overtime> call = overtimeService.PostOvertime(persons, overtimes);
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                overtime = response.body();
                Intent intent = new Intent(AddOvertimeActivity.this, EmployeeRecyclerActivity.class);
                intent.putExtra("activity", "reqovertime");
                startActivity(intent);
                Toast.makeText(AddOvertimeActivity.this,"Saving Success!", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(AddOvertimeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetOvertime ()
    {
        overtime.date = date_overtime.getText().toString();
        overtime.duration = Integer.parseInt(duration.getText().toString()) * 3600;
        overtime.information = information.getText().toString();
        overtime.personid = mid;
        overtime.status = 0;

        PostOvertime(persons, overtime);
    }
}
