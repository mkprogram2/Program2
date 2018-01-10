package com.example.admin.program2.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.example.admin.program2.model.Login;
import com.example.admin.program2.model.postHr;
import com.example.admin.program2.service.HrService;
import com.example.admin.program2.service.CheckinService;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private List<Hr> hr;
    private String employee_id, mid, mname, persons, tgl;
    private Integer mshift;
    private Timestamp timestamp;
    //private Long time_stamp_server = new Long(1515573272749L);
    private Long time_stamp_checkin;

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
        mshift = Integer.parseInt(getIntent().getExtras().getString("shiftid")) ;

        IDs.setIdUser(mid);
        IDs.setLoginUser(mname);

        Log.d("RESPONSE WEBID: ", mid);

        final person person = new person();
        person.setId(mid);
        person.setName(mname);
        person.setShiftid(mshift);

        employee_name.setText(IDs.getLoginUser());

        service2 = ClientService.createService().create(CheckinService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        String strRequestBody = mid;
        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        //employee_name = (TextView) findViewById(R.id.employee_name);
        /*service = ClientService.createService().create(HrService.class);

        employee_id = SharedPreferenceEditor.LoadPreferences(this, "Employee Id", "");

        getHr(employee_id);*/

        //checkin.setVisibility(View.INVISIBLE);

        //kehadiran.setText(getDate(time_stamp_server));

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
                startActivity(new Intent(MainActivity.this, WorkhoursActivity.class));
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut(persons, requestBody);
            }
        });
    }

    public void checkIn(final String persons, RequestBody id)
    {
        Call<HashMap<String, String>> call = service2.postCheckin(persons, id);
        call.enqueue(new Callback<HashMap<String, String>>()
        {
            private String responseCode;
            private String responseMessage;

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
                    if (responseCode == null || responseMessage == null)
                    {
                        Log.d("teh", "haaa");
                    }
                    else
                    {
                        String[] parts = responseMessage.split(";");
                        Log.d("RESPONSE FROM LOGIN", responseMessage);
                        Log.d("Response Code", responseCode);
                        Log.d("walaaa", parts[0]);
                        if (responseCode.equals("workstart"))
                        {
                            intent.putExtra("workstart", parts[0]);
                            /*time_stamp_checkin = Long.parseLong(parts[0]);
                            kehadiran.setText(getDate(time_stamp_checkin));*/
                        }
                        else if(responseCode.equals("workstartinterval"))
                        {
                            /*time_stamp_checkin = Long.parseLong(parts[0]);
                            kehadiran.setText(parts[0]);*/
                            intent.putExtra("workstartinterval", parts[0]);
                        }
                    }
                }
                if (responseCode == null || responseMessage == null)
                {

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

    private String getDate(long time_stamp_server) {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        return formatter.format(time_stamp_server);
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
