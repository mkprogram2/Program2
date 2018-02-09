package com.mk.admin.payroll.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.service.CheckinService;
import com.mk.admin.payroll.service.WorkhourService;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayrollService extends Service {
    private CheckinService service2;
    private WorkhourService service3;
    private String persons;
    private String mid;
    private Session session;
    private RequestBody requestBody;
    private Boolean temp = false;
    public static Boolean status = false;

    public PayrollService()
    {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        service2 = ClientService.createService().create(CheckinService.class);
        service3 = ClientService.createService().create(WorkhourService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        session = new Session(PayrollService.this);
        mid = session.getId();
        String strRequestBody = mid;
        requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        super.registerReceiver(receiver, filters);
        //return super.onStartCommand(intent, flags, startId);
        //EveryTime();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Payroll Stopped Working !", Toast.LENGTH_SHORT).show();
        /*super.onDestroy();
        startService(new Intent(this, PayrollService.class));*/ // add this line
    }

    /*@Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }*/


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (temp == false)
            {
                temp = true;
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo;
                wifiInfo = wifiManager.getConnectionInfo();

                if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))
                {
                    Intent pushIntent = new Intent(context, PayrollService.class);
                    context.startService(pushIntent);
                }
                else
                {
                    if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        String ssid = wifiInfo.getSSID();
                        String macaddress = wifiInfo.getMacAddress();
                        Log.d("SSID", ssid);
                        Log.d("MAC ADDRESS", macaddress);
                        if (ssid.equals("\"B1Z_2017\""))
                        {
                            status = true;
                            EveryTime();
                        /*session = new Session(PayrollService.this);
                        mid = session.getId();
                        String strRequestBody = mid;
                        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);
                        checkIn(persons, requestBody);*/
                        }
                        else
                        {
                            if (status == true)
                                Toast.makeText(PayrollService.this, "Out Of Range Within Workplace !", Toast.LENGTH_SHORT).show();
                            else
                                status = false;
                        }
                    }
                }
            }
            else
            {
                Test();
            }
        }
    };

    /*private void checkIn(RequestBody id, String access_token)
    {
        Call<Integer> call = service2.postCheckin(id, access_token);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                final Integer data = response.body();

                if (response.isSuccessful())
                {
                    if (data == 1)
                    {
                        workhour(mid);
                    }
                    else if (data == 2)
                    {
                        Behavior(requestBody);
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(PayrollService.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void workhour (String id)
    {
        Call<Workhour> call = service3.getCheckworkhour(id, session.getAccesstoken());
        call.enqueue(new Callback<Workhour>()
        {
            @Override
            public void onResponse(retrofit2.Call<Workhour> call, Response<Workhour> response)
            {
                final Workhour data = response.body();
                Toast.makeText(PayrollService.this, "You Have Checked In !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(retrofit2.Call<Workhour> call, Throwable t)
            {
            }

        });
    }

    private void Behavior (RequestBody id)
    {
        Call<Integer> call = service3.postBehavior(id, session.getAccesstoken());
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                if (response.isSuccessful())
                {
                    final Integer data = response.body();
                }
                else
                {
                    onDestroy();
                }
                /*if (data == 1)
                {
                    workhour(mid);
                }*/
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(PayrollService.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void EveryTime ()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean condition = false;
                if(condition != true) {
                    handler.postDelayed(this, 60000);
                    Log.d("YAAA", " YAAA");
                    Toast.makeText(PayrollService.this, "Service", Toast.LENGTH_SHORT).show();

                    //checkIn(requestBody, session.getAccesstoken());
                    Behavior(requestBody);
                }
                else {
                    Log.d("NOPEEE", "NOPEEE");
                }
            }
        }, 1000);
    }

    private void Test ()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean condition = false;
                if(condition != true) {
                    handler.postDelayed(this, 50000);
                    temp = false;
                }
                else
                {
                }
            }
        }, 1000);
    }
}
