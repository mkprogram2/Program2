package com.mk.admin.payroll.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.PayrollService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.LoginService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;

    private LoginService service;
    private String persons, mUsername, mPassword;
    private ProgressDialog progressDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        service = ClientService.createService().create(LoginService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this, "Persons", "");
        session = new Session(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                attempLogin();
            }
        });
    }

    private void postlogin(String persons, Person logins)
    {
        Call<Person> call = service.postLogin(persons, logins);
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    final Person data = response.body();

                    session.setId(data.id);
                    Log.d("Session", session.getId());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("id", data.id);
                    intent.putExtra("name", data.name);
                    intent.putExtra("role", data.Role.id.toString());
                    intent.putExtra("role_name", data.Role.name.toString());
                    intent.putExtra("shiftid", data.PersonDetail.Shift.id.toString());
                    intent.putExtra("shift_workstart",data.PersonDetail.Shift.workstart.toString());
                    intent.putExtra("shift_workend", data.PersonDetail.Shift.workend.toString());
                    startActivity(intent);
                    //setFocus();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Server Failed!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                //setFocus();
                Log.d("ERROR", t.getMessage());
                Toast.makeText(LoginActivity.this, "Username Atau Password Salah", Toast.LENGTH_LONG).show();
            }

        });
    }

    private Person setlogin()
    {
        Person person = new Person();
        person.name = username.getText().toString();
        person.apppassword = password.getText().toString();
        return person;
    }

    private void setFocus()
    {
        username.setText("");
        password.setText("");
    }

    private void attempLogin ()
    {
        username.setError(null);
        password.setError(null);

        mUsername = username.getText().toString();
        mPassword = password.getText().toString();

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

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            /*progressDialog.setMessage(this.getString(R.string.progress_message));
            progressDialog.setCancelable(false);
            progressDialog.show();*/

            Person personlogin = setlogin();
            postlogin(persons, personlogin);

        }
    }
}
