package com.mk.admin.payroll.main;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.admin.EmployeeActivity;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.WorkhourService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkhoursActivity extends AppCompatActivity
{

    private String workstart, workstartinterval, interval_work, workend, result, persons, personid, shifttime;
    private Long workstartinterval_time;
    private String[] waktu_shift, waktu_kerja, telat_s;
    private Integer jam_pulang_shift, menit_pulang_shift, jam_pulang, menit_pulang, telat_jam, telat_menit, check;
    private EmployeeService employeeService;
    private Person dataperson = new Person();
    private Handler handler = new Handler();
    private Runnable runnable;
    private String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @BindView(R.id.masuk)
    Button masuk;
    @BindView(R.id.keluar)
    Button keluar;
    @BindView(R.id.shift_in)
    Button shift_in;
    @BindView(R.id.shift_out)
    Button shift_out;
    @BindView(R.id.kehadiran)
    TextView kehadiran;
    @BindView(R.id.checkout)
    TextView checkout;
    @BindView(R.id.timeout)
    TextView timeout;
    @BindView(R.id.tv_second)
    TextView tv_second;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhours);

        ButterKnife.bind(this);

        employeeService = ClientService.createService().create(EmployeeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        workstartinterval = getIntent().getExtras().getString("workstartinterval");
        workstart = getIntent().getExtras().getString("workstart");
        interval_work = getIntent().getExtras().getString("interval_work");
        workend = getIntent().getExtras().getString("workend");
        personid = getIntent().getExtras().getString("personid");

        GetPerson(persons, personid);

        if (workstart == null || workstartinterval == null)
        {
            kehadiran.setText("Please Check In");
        }
        else
        {
            workstartinterval_time = Long.parseLong(workstartinterval);

            GetWorkend(workstart);

            masuk.setText(result.toString());

            waktu_shift = interval_work.split(":");
            jam_pulang_shift = Integer.parseInt(waktu_shift[0]);
            menit_pulang_shift = Integer.parseInt((waktu_shift[1]));

            waktu_kerja = result.split(":");
            jam_pulang = Integer.parseInt(waktu_kerja[0]) + jam_pulang_shift;
            menit_pulang = Integer.parseInt(waktu_kerja[1]) + menit_pulang_shift;

            telat_s = workstartinterval.split("-");
            telat_jam = Integer.parseInt(telat_s[1]) / 3600;
            telat_menit = (Integer.parseInt(telat_s[1]) % 3600) / 60;
            check = Integer.parseInt(telat_s[1]);

            if (workstartinterval_time < 0)
            {
                kehadiran.setText("Late " + telat_jam + " Hours : " + telat_menit + " Minutes");
                if (jam_pulang <10)
                {
                    timeout.setText("0" +jam_pulang + ":" + menit_pulang);
                }
                else if(menit_pulang < 10)
                {
                    timeout.setText(jam_pulang + ":0" + menit_pulang);
                }
                else
                {
                    timeout.setText(jam_pulang + ":" + menit_pulang);
                }
            }
            else
            {
                kehadiran.setText("On Time");
            }
        }

        if (workend.equals("null_workend"))
        {
            keluar.setText("");
            checkout.setText("");
        }
        else
        {
            GetWorkend(workend);
            keluar.setText(result);
            checkout.setText("You Have Been Checked Out");
        }

        countDownStart();
    }

    private void GetPerson (String persons, String id)
    {
        Call<Person> call = employeeService.GetEmployee(persons, id);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                dataperson = response.body();
                Log.d("Data", dataperson.getName());
                shift_in.setText(SetShiftTime(dataperson.Shift.getWorkstart()));
                shift_out.setText(SetShiftTime(dataperson.Shift.getWorkend()));
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(WorkhoursActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetWorkend (String worktime)
    {
        SimpleDateFormat formatterr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");

        try {
            Date dates = formatterr.parse(worktime);
            formatterr.applyPattern("HH:mm");
            result = formatterr.format(dates);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String SetShiftTime (String time)
    {
        SimpleDateFormat formatterr = new SimpleDateFormat("HH:mm:ss");

        try {
            Date dates = formatterr.parse(time);
            formatterr.applyPattern("HH:mm");
            shifttime = formatterr.format(dates);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return shifttime;
    }


    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;

                        tv_second.setText(String.format("%02d", Seconds));
                        //
                        /*tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));*/

                    } else {
                        /*linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);*/
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private String getDate(long time_stamp_server)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(time_stamp_server);
    }
}