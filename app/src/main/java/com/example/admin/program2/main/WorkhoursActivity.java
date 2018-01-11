package com.example.admin.program2.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.program2.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkhoursActivity extends AppCompatActivity
{

    private String workstart, workstartinterval, workend, interval_work;
    private Long workstart_time, workstartinterval_time;
    private String[] waktu_shift, waktu_kerja;
    private Integer jam_pulang_shift, menit_pulang_shift, jam_pulang, menit_pulang, selisih_jam, selisih_menit;

    @BindView(R.id.masuk)
    Button masuk;
    @BindView(R.id.keluar)
    Button keluar;
    @BindView(R.id.kehadiran)
    TextView kehadiran;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhours);

        ButterKnife.bind(this);
/*
        workstart = getIntent().getExtras().getString("workstart");
        workstartinterval = getIntent().getExtras().getString("workstartinterval");*/
        MainActivity main = new MainActivity();
        workstart = main.workstart;
        workstartinterval = main.workstart_interval;
        interval_work = main.interval_work;


        if (workstart == null || workstartinterval == null)
        {
            kehadiran.setText("Anda Belum Melakukan CheckIn");
        }
        else
        {
            workstart_time = Long.parseLong(workstart);
            workstartinterval_time = Long.parseLong(workstartinterval);

            masuk.setText(getDate(workstart_time));

            waktu_shift = interval_work.split(":");
            jam_pulang_shift = Integer.parseInt(waktu_shift[0]);
            menit_pulang_shift = Integer.parseInt((waktu_shift[1]));

            waktu_kerja = getDate(workstart_time).split(":");
            jam_pulang = Integer.parseInt(waktu_kerja[0]) + jam_pulang_shift;
            menit_pulang = Integer.parseInt(waktu_kerja[1]) + menit_pulang_shift;


            if (workstartinterval_time < 0)
            {
                /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                simpleDateFormat.parse(workstartinterval);*/
                Toast.makeText(WorkhoursActivity.this, workstartinterval, Toast.LENGTH_LONG).show();
                keluar.setText(jam_pulang + ":" + menit_pulang);
                kehadiran.setText("Terlambat " + getDate(workstartinterval_time));
            }
            else
            {
                kehadiran.setText("Tepat Waktu");
            }
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
