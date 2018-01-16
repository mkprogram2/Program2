package com.example.admin.program2.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.common.IDs;
import com.example.admin.program2.service.CheckinService;
import com.example.admin.program2.service.SalaryService;
import com.example.admin.program2.service.WorkhourService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private CheckinService service2;
    private WorkhourService service3;
    public static String mid, mname, shift_workstart, shift_workend, interval_work, workstart, workstart_interval;
    public static double gross_salary;
    public static Integer mshiftid;
    private String persons;

    @BindView(R.id.employee_name)
    TextView employee_name;
    @BindView(R.id.kehadiran)
    TextView kehadiran;
    @BindView(R.id.calendar)
    Button calendar;
    @BindView(R.id.checkin)
    Button checkin;
    @BindView(R.id.workhours)
    Button workhours;
    @BindView(R.id.checkout)
    Button checkout;
    @BindView(R.id.salary)
    Button salary;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mid = getIntent().getExtras().getString("id");
        mname = getIntent().getExtras().getString("name");
        gross_salary = getIntent().getExtras().getDouble("salary");
        mshiftid = Integer.parseInt(getIntent().getExtras().getString("shiftid")) ;
        shift_workstart = getIntent().getExtras().getString("shift_workstart");
        shift_workend = getIntent().getExtras().getString("shift_workend");

        try
        {
            SimpleDateFormat date = new SimpleDateFormat("hh:mm");
            Date tglAwal = (Date) date.parse(shift_workstart);
            Date tglAkhir = (Date) date.parse(shift_workend);

            long bedaHari = Math.abs(tglAkhir.getTime() - tglAwal.getTime());
            interval_work = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toHours(bedaHari),
                    TimeUnit.MILLISECONDS.toMinutes(bedaHari) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(bedaHari)));

            employee_name.setText(mname);
        }
        catch(Exception e){}


        IDs.setIdUser(mid);
        IDs.setLoginUser(mname);

        Log.d("RESPONSE WEBID: ", mname);

        service2 = ClientService.createService().create(CheckinService.class);
        service3 = ClientService.createService().create(WorkhourService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        String strRequestBody = mid;
        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        checkin.setOnClickListener (new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkIn(persons, requestBody);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CalendarActivity.class));
            }
        });

        workhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workhour(persons, mid);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut(persons, requestBody);
            }
        });

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SalaryActivity.class));
            }
        });
    }

    private void checkIn(final String persons, RequestBody id)
    {
        Call<Integer> call = service2.postCheckin(persons, id);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer data = response.body();

                if (data == 1)
                {
                    workhour(persons, mid);
                }
                else if(data == 2)
                {
                    Toast.makeText(MainActivity.this,"Anda Telah CheckIn Hari Ini", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Holiday!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                kehadiran.setText(t.getMessage());
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkOut(final String persons, RequestBody id)
    {
        Call<HashMap<String, String>> call = service2.checkout(persons, id);
        call.enqueue(new Callback<HashMap<String, String>>()
        {
            private String responseCode;
            private String responseMessage;

            @Override
            public void onResponse(retrofit2.Call<HashMap<String, String>> call, Response<HashMap<String, String>> response)
            {
                final HashMap<String, String> data = response.body();

                Log.d("Data",data.keySet().toString());
            }

            @Override
            public void onFailure(retrofit2.Call<HashMap<String, String>> call, Throwable t)
            {
                kehadiran.setText(t.getMessage());
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void workhour (final String persons, String id)
    {
        Call<HashMap<String, String>> call = service3.getCheckworkhour(persons, id);
        call.enqueue(new Callback<HashMap<String, String>>()
        {
            private String responseCode, responseMessage;

            @Override
            public void onResponse(retrofit2.Call<HashMap<String, String>> call, Response<HashMap<String, String>> response)
            {
                final HashMap<String, String> data = response.body();

                Log.d("aaa",data.keySet().toString());

                Intent intent = new Intent(MainActivity.this, WorkhoursActivity.class);

                for (String resultKey : data.keySet())
                {
                    responseCode = resultKey;
                    responseMessage = data.get(resultKey);
                    if (responseCode != null)
                    {
                        if (responseMessage != null)
                        {
                            String[] parts = responseMessage.split(";");
                            Log.d("RESPONSE FROM LOGIN", responseMessage);
                            Log.d("Response Code", responseCode);
                            Log.d("walaaa", parts[0]);
                            if (responseCode.equals("workstart"))
                            {
                                intent.putExtra("workstart", parts[0]);
                                workstart = parts[0].toString();
                            }
                            else if(responseCode.equals("workstartinterval"))
                            {
                                workstart_interval = parts[0].toString();
                                intent.putExtra("workstartinterval", parts[0]);
                            }
                        }
                    }
                }
                startActivity(intent);
            }

            @Override
            public void onFailure(retrofit2.Call<HashMap<String, String>> call, Throwable t)
            {
                Toast.makeText(MainActivity.this,"Silahkan CheckIn Terlebih Dahulu", Toast.LENGTH_LONG).show();
            }

        });
    }
}
