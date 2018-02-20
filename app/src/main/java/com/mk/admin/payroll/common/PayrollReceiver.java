package com.mk.admin.payroll.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.mk.admin.payroll.service.EmployeeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayrollReceiver extends BroadcastReceiver {

    private final static String TAG = PayrollReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        Log.d("SUKSES","Test");
        Toast.makeText(context, "Payroll Service", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, PayrollService.class));

        /*int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
                && WifiManager.WIFI_STATE_ENABLED == wifiState)
        {
            if (Log.isLoggable(TAG, Log.VERBOSE))
            {
                Log.v(TAG, "Wifi is now enabled");
            }
            Log.d("1","1");
            context.startService(new Intent(context, PayrollService.class));
        }
        else
        {
            Log.d("4","4");
        }*/

    }
}
