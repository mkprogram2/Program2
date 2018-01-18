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
import com.example.admin.program2.model.Hr;
import com.example.admin.program2.model.person;
import com.example.admin.program2.service.HrService;
import com.example.admin.program2.service.CheckinService;
import com.example.admin.program2.service.WorkhourService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private HrService service;
    private CheckinService service2;
    private WorkhourService service3;
    private person person;
    private List<Hr> hr;
    public static String mid, mname, shift_workstart, shift_workend, interval_work, workstart, workstart_interval;
    public static Integer mshiftid;
    private String employee_id, persons, tgl;
    private Timestamp timestamp;
    //private Long time_stamp_server = new Long(1515573272749L);
    private Long time_stamp_checkin;
    private Integer status_checkin = 0;

    //TextView employee_name;
    //Button checkin;
    /*private List<LoginActivity> login;
    private postHr posthr;*/
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mid = getIntent().getExtras().getString("id");
        mname = getIntent().getExtras().getString("name");
        mshiftid = Integer.parseInt(getIntent().getExtras().getString("shiftid")) ;
        shift_workstart = getIntent().getExtras().getString("shift_workstart");
        shift_workend = getIntent().getExtras().getString("shift_workend");

        try{
            SimpleDateFormat date = new SimpleDateFormat("hh:mm");
            Date tglAwal = (Date) date.parse(shift_workstart);
            Date tglAkhir = (Date) date.parse(shift_workend);

            long bedaHari = Math.abs(tglAkhir.getTime() - tglAwal.getTime());
            interval_work = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toHours(bedaHari),
                    TimeUnit.MILLISECONDS.toMinutes(bedaHari) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(bedaHari)));

            employee_name.setText(mname);

        }catch(Exception e){}


        IDs.setIdUser(mid);
        IDs.setLoginUser(mname);

        Log.d("RESPONSE WEBID: ", mname);

        service2 = ClientService.createService().create(CheckinService.class);
        service3 = ClientService.createService().create(WorkhourService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        String strRequestBody = mid;
        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        //employee_name = (TextView) findViewById(R.id.employee_name);
        /*service = ClientService.createService().create(HrService.class);

        employee_id = SharedPreferenceEditor.LoadPreferences(this, "Employee Id", "");

        getHr(employee_id);*/

        //checkin.setVisibility(View.INVISIBLE);

        checkin.setOnClickListener (new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkIn(persons, requestBody);
                /*Hr tot = sethr();

                postHr(employee_id, tot);

                Hr uphr =  updatedataHr();

                updateHr(employee_id, uphr);

                //deleteHr(employee_id,"4");

                Toast.makeText(MainActivity.this, uphr.getName(),
                        Toast.LENGTH_LONG).show();*/
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
                //startActivity(new Intent(MainActivity.this, WorkhoursActivity.class));
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut(persons, requestBody);
            }
        });
    }

    private void checkIn(final String persons, RequestBody id)
    {
        Call<HashMap<String, String>> call = service2.postCheckin(persons, id);
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
                            if (responseCode.equals("personid"))
                            {
                                status_checkin = 1;
                            }
                            else if (responseCode.equals("workstart"))
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
                        else
                        {
                            if (responseCode.equals("personid"))
                            {
                                status_checkin = 0;
                            }
                        }
                    }
                }
                if (status_checkin == 0)
                {
                    Toast.makeText(MainActivity.this, "Anda Telah CheckIn Hari Ini", Toast.LENGTH_LONG).show();
                }
                else
                {
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<HashMap<String, String>> call, Throwable t)
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
                            if (responseCode.equals("personid"))
                            {
                                status_checkin = 1;
                            }
                            else if (responseCode.equals("workstart"))
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
                        else
                        {
                            if (responseCode.equals("personid"))
                            {
                                status_checkin = 0;
                            }
                        }
                    }
                }
                if (status_checkin == 0)
                {
                    Toast.makeText(MainActivity.this, "Silahkan CheckIn Terlebih Dahulu", Toast.LENGTH_LONG).show();
                }
                else
                {
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<HashMap<String, String>> call, Throwable t)
            {
                Toast.makeText(MainActivity.this,"Silahkan CheckIn Terlebih Dahulu", Toast.LENGTH_LONG).show();
            }

        });

    }

    private String getDate(long time_stamp_server) {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        return formatter.format(time_stamp_server);
    }

    private Date setdate(String workstartinterval)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        Date a = null;
        try {
            a =  simpleDateFormat.parse(workstartinterval);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return a;
    }

    private void getHr(final String employee_id)
    {
        Call<List<Hr>> call = service.getHr(employee_id);
        call.enqueue(new Callback<List<Hr>>()
        {
            @Override
            public void onResponse(Call<List<Hr>> call, Response<List<Hr>> response)
            {
                    hr = response.body();
                    employee_name.setText(hr.get(0).getName());
            }

            @Override
            public void onFailure(Call<List<Hr>> call, Throwable t)
            {
                employee_name.setText(t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(),
                Toast.LENGTH_LONG).show();
            }
        });
    }

    private void postHr(String employee_id, Hr hrr)
    {
        Call<HashMap<Integer, String>> call = service.postHr(employee_id, hrr);
        call.enqueue(new Callback<HashMap<Integer, String>>()
        {
            @Override
            public void onResponse(Call<HashMap<Integer, String>> call, Response<HashMap<Integer, String>> response)
            {
                //final HashMap<Integer, String> data = response.body();
                Toast.makeText(MainActivity.this, "Tesssssssssssss", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<HashMap<Integer, String>> call, Throwable t)
            {
                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void updateHr(String employee_id, Hr uphr)
    {
        //Hr hr2;
        Call<List<Hr>> call = service.updateHr(employee_id, uphr);
        call.enqueue(new Callback<List<Hr>>()
        {
            private int responseCode;
            private String responseMessage;

            @Override
            public void onResponse(Call<List<Hr>> call, Response<List<Hr>> response)
            {
                //final HashMap<Integer, String> data = response.body();
                Toast.makeText(MainActivity.this, "Bisa", Toast.LENGTH_LONG).show();
                /*for (int resultKey : data.keySet())
                {
                    responseCode = resultKey;
                    responseMessage = data.get(resultKey);
                    Log.d("RESPONSE WEBSERVICE: ", String.valueOf(responseCode) + responseMessage);

                    Toast.makeText(MainActivity.this, responseMessage, Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<List<Hr>> call, Throwable t)
            {
                Log.d(t.getMessage(), "onFailure: ");
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void deleteHr(String employee_id, String id_employee)
    {
        Call<HashMap<Integer, String>> call = service.deleteHr(employee_id, id_employee);
        call.enqueue(new Callback<HashMap<Integer, String>>()
        {
            @Override
            public void onResponse(Call<HashMap<Integer, String>> call, Response<HashMap<Integer, String>> response)
            {

            }

            @Override
            public void onFailure(Call<HashMap<Integer, String>> call, Throwable t)
            {

            }

        });
    }

    private Hr sethr()
    {
        Hr hrr = new Hr();
        hrr.setName("ani anak baik");
        hrr.setId("4");
        return hrr;
    }

    private Hr updatedataHr()
    {
        Hr uphr = new Hr();
        uphr.setId("4");
        uphr.setName("Ivan jangan Nakal");
        return  uphr;
    }
}
