package com.mk.admin.payroll.main;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.PayrollReceiver;
import com.mk.admin.payroll.common.PayrollService;
import com.mk.admin.payroll.common.RestVariable;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.model.AuthUtil;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.LoginService;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loginmerchantcode)
    EditText loginmerchantcode;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.rememberme)
    CheckBox rememberme;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private LoginService service, serviceVerified;
    private String mUsername, mPassword, mloginmerchantcode;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        service = ClientService.createServiceLogin().create(LoginService.class);
        serviceVerified = ClientService.createService().create(LoginService.class);
        session = new Session(this);

        rememberMe();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                attempLogin();
            }
        });
    }

    private void getpostlogin(String authorization, String email, String serial_key)
    {
        Call<Person> call = serviceVerified.getPerson(authorization, email, serial_key);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    final Person data = response.body();
                    session.setId(data.id);
                    session.SetUsername(mUsername);
                    session.SetPassword(mPassword);
                    session.SetMerchantCode(loginmerchantcode.getText().toString());
                    if (rememberme.isChecked())
                        session.SetRememberMe(1);
                    else
                        session.SetRememberMe(0);

                    if (data.id != null)
                    {
                        /*if (PayrollService.status == false)
                        {
                            startService(new Intent(LoginActivity.this, PayrollService.class));
                        }*/
                        if (!isMyServiceRunning(PayrollService.class))
                        {
                            startService(new Intent(LoginActivity.this, PayrollService.class));
                        }
                    /*stopService(new Intent(LoginActivity.this, PayrollService.class));
                    startService(new Intent(LoginActivity.this, PayrollService.class));*/
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        progress_bar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "E-mail Or Password Are Wrong!", Toast.LENGTH_LONG).show();
                        progress_bar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Server Failed!", Toast.LENGTH_LONG).show();
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Toast.makeText(LoginActivity.this, "Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void postlogin (final String merchantCode, String granttype, String username, String password)
    {
        Call<AuthUtil> call = service.RequestToken(RestVariable.Authorization, merchantCode, granttype, username, password);
        call.enqueue(new Callback<AuthUtil>()
        {
            @Override
            public void onResponse(retrofit2.Call<AuthUtil> call, Response<AuthUtil> response)
            {
                if (response.isSuccessful())
                {
                    final AuthUtil data = response.body();

                    session.setAccesstoken(data.access_token);
                    String strRequestBody = mUsername;
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);
                    getpostlogin(data.access_token, mUsername, Build.SERIAL.toString());
                    //setFocus();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Username or Password are Wrong!", Toast.LENGTH_LONG).show();
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AuthUtil> call, Throwable t)
            {
                Toast.makeText(LoginActivity.this, "Server Failed", Toast.LENGTH_LONG).show();
                progress_bar.setVisibility(View.GONE);
            }
        });
    }

    private void attempLogin ()
    {
        username.setError(null);
        password.setError(null);
        loginmerchantcode.setError(null);

        mUsername = username.getText().toString();
        mPassword = password.getText().toString();
        mloginmerchantcode = loginmerchantcode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mUsername))
        {
            username.setError("This field is required");
            focusView = username;
            cancel = true;
        }

        if (TextUtils.isEmpty(mPassword))
        {
            password.setError("This field is required");
            focusView = password;
            cancel = true;
        }

        if (TextUtils.isEmpty(mloginmerchantcode))
        {
            loginmerchantcode.setError("This field is required");
            focusView = loginmerchantcode;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            postlogin(loginmerchantcode.getText().toString(), RestVariable.PASSWORD, mUsername, mPassword);
        }

        progress_bar.setVisibility(View.VISIBLE);
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
                    Log.d("Yaaaaaa", " Yaaaaaaaaaaa");
                }
                else {
                    Log.d("Tidaaaaak", "Tidaaaak");
                    }
                }
            }, 1000);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void rememberMe ()
    {
        if (session.GetRememberMe() == 1)
        {
            username.setText(session.GetUsername());
            password.setText(session.GetPassword());
            loginmerchantcode.setText(session.GetMerchantCode());
            rememberme.setChecked(true);
        }
    }
}
