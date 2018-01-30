package com.mk.admin.payroll.main.manage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OvertimeRequestActivity extends AppCompatActivity {

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

    DatePickerDialog datePickerDialog;
    private String mid, mname;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_overtime_request);

        ButterKnife.bind (this);

        mid = getIntent().getExtras().getString("id");
        mname = getIntent().getExtras().getString("name");

        employee_name.setText(mname);
        employee_role.setText(getIntent().getExtras().getString("role"));

        MakeDatePicker();

        overtime_calendar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });

    }

    private void MakeDatePicker ()
    {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(OvertimeRequestActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener()
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
}
