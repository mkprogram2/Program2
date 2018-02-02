package com.mk.admin.payroll.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.mk.admin.payroll.main.HomeActivity;
import com.mk.admin.payroll.main.WorkhoursActivity;
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

        IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        super.registerReceiver(receiver, filters);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Payroll Stopped Working !", Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo;

            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                String ssid = wifiInfo.getSSID();
                Log.d("SSID", ssid);
                Log.d("TEST", "TEST");
                if (ssid.equals("\"B1Z_2017\""))
                {
                    session = new Session(PayrollService.this);
                    mid = session.getId();
                    String strRequestBody = mid;
                    final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);
                    checkIn(persons, requestBody);
                }
                else
                {
                    Toast.makeText(PayrollService.this, "Out Of Range Within Workplace !", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(PayrollService.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void workhour (final String persons, String id)
    {
        Call<Workhour> call = service3.getCheckworkhour(persons, id);
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
}
