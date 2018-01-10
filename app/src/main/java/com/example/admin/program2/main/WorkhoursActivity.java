package com.example.admin.program2.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.program2.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkhoursActivity extends AppCompatActivity {

    private String workstart, workstartinterval;
    private Long workstart_time, workstartinterval_time;

    @BindView(R.id.masuk)
    Button masuk;
    @BindView(R.id.keluar)
    Button keluar;
    @BindView(R.id.kehadiran)
    TextView kehadiran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhours);

        ButterKnife.bind(this);

        workstart = getIntent().getExtras().getString("workstart");
        workstartinterval = getIntent().getExtras().getString("workstartinterval");

        if (workstart == null || workstartinterval == null)
        {

        }
        else
        {
            workstart_time = Long.parseLong(workstart);
            workstartinterval_time = Long.parseLong(workstartinterval);

            masuk.setText(getDate(workstart_time));
            keluar.setText(getDate(workstartinterval_time));

            if (workstartinterval_time < 0)
            {
                kehadiran.setText("Terlambat " + getDate(workstartinterval_time));
            }
            else
            {
                kehadiran.setText("Tepat Waktu");
            }
        }
    }

    private String getDate(long time_stamp_server) {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        return formatter.format(time_stamp_server);
    }
}
