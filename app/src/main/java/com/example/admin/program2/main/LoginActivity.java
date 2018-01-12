package com.example.admin.program2.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.model.Login;
import com.example.admin.program2.model.person;
import com.example.admin.program2.service.LoginService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private List<Login> listlogin;

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
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                person personlogin = setlogin();
                postlogin(persons, personlogin);
            }
        });
    }

    private void postlogin(String persons, person logins)
    {
        Call<person> call = service.postLogin(persons, logins);
        call.enqueue(new Callback<person>()
        {
            private String responseCode, responseMessage;

            @Override
            public void onResponse(retrofit2.Call<person> call, Response<person> response)
            {
                final person data = response.body();
                //Toast.makeText(LoginActivity.this,data.getSalary().toString(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("name", data.getName());
                intent.putExtra("role", data.getRole());
                intent.putExtra("salary", data.getSalary());
                intent.putExtra("shiftid", data.Shift.getId());
                intent.putExtra("shift_workstart",data.Shift.getWorkstart());
                intent.putExtra("shift_workend", data.Shift.getWorkend());

                if (data.getRole() == 1)
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Admin Cuy", Toast.LENGTH_LONG).show();
                }

                Log.d("data",data.toString());
            }

            @Override
            public void onFailure(retrofit2.Call<person> call, Throwable t)
            {
                //setFocus();
                Toast.makeText(LoginActivity.this, "Username Atau Password Salah", Toast.LENGTH_LONG).show();
            }

        });
    }

    private person setlogin()
    {
        person person = new person();
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
