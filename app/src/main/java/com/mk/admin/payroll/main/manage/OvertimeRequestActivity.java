package com.mk.admin.payroll.main.manage;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mk.admin.payroll.R;

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

    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_request);

        ButterKnife.bind(this);

        date_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(OvertimeRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formatTanggal = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        date_overtime.setText("Kamu memilih tanggal : " + sdf.format(myCalendar.getTime()));
                    }
                },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
