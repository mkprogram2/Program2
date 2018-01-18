package com.mk.admin.payroll.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.admin.MainadminActivity;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    private EditText username;
    private EditText password;
    private Button login;

    private LoginService service;
    private String persons;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        service = ClientService.createService().create(LoginService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this, "Persons", "");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Person personlogin = setlogin();
                postlogin(persons, personlogin);
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
                final Person data = response.body();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("name", data.getName());
                intent.putExtra("role", data.Role.getId());
                intent.putExtra("salary", data.getSalary());
                intent.putExtra("shiftid", data.Shift.getId());
                intent.putExtra("shift_workstart",data.Shift.getWorkstart());
                intent.putExtra("shift_workend", data.Shift.getWorkend());

                if (data.Role.getId() != 1)
                {
                    startActivity(intent);
                }
                else if (data.Role.getId() == 1)
                {
                    startActivity(new Intent(LoginActivity.this, MainadminActivity.class));
                }

                Log.d("data",data.toString());
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                //setFocus();
                Toast.makeText(LoginActivity.this, "Username Atau Password Salah", Toast.LENGTH_LONG).show();
            }

        });
    }

    private Person setlogin()
    {
        Person person = new Person();
        person.setName(username.getText().toString());
        person.setPassword(password.getText().toString());
        return person;
    }

    private void setFocus()
    {
        username.setText("");
        password.setText("");
    }
}
