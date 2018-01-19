package com.mk.admin.payroll.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mk.admin.payroll.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkhoursActivity extends AppCompatActivity
{

    private String workstart, workstartinterval, interval_work, workend, result;
    private Long workstart_time, workstartinterval_time;
    private String[] waktu_shift, waktu_kerja, waktu_terlambat, telat_s;
    private Integer jam_pulang_shift, menit_pulang_shift, jam_pulang, menit_pulang, telat_jam, telat_menit, check;

    @BindView(R.id.masuk)
    Button masuk;
    @BindView(R.id.keluar)
    Button keluar;
    @BindView(R.id.kehadiran)
    TextView kehadiran;
    @BindView(R.id.checkout)
    TextView checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhours);

        ButterKnife.bind(this);

        workstartinterval = getIntent().getExtras().getString("workstartinterval");
        workstart = getIntent().getExtras().getString("workstart");
        interval_work = getIntent().getExtras().getString("interval_work");
        workend = getIntent().getExtras().getString("workend");

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
                    keluar.setText("0" +jam_pulang + ":" + menit_pulang);
                }
                else if(menit_pulang < 10)
                {
                    keluar.setText(jam_pulang + ":0" + menit_pulang);
                }
                else
                {
                    keluar.setText(jam_pulang + ":" + menit_pulang);
                }
            }
            else
            {
                kehadiran.setText("On Time");
            }
        }

        if (workend.equals("null_workend"))
        {
            checkout.setText("");
        }
        else
        {
            GetWorkend(workend);
            checkout.setText("You Have Been Check Out On " + result);
        }
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

    private String getDate(long time_stamp_server)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(time_stamp_server);
    }

    private String workend (String workstart, String shift_workend)
    {
        String worktime = workstart + shift_workend;
        return worktime;
    }
}