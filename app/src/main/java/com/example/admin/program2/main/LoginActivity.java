package com.example.admin.program2.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.program2.R;
import com.example.admin.program2.common.ClientService;
import com.example.admin.program2.common.SharedPreferenceEditor;
import com.example.admin.program2.model.Login;
import com.example.admin.program2.service.LoginService;

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
    private List<LoginActivity> listlogin;

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
                Login plog = setlogin();
                postlogin(persons, plog);
            }
        });
    }

    private void postlogin(String persons, Login logins)
    {
        Call<HashMap<Integer, String>> call = service.postLogin(persons, logins);
        call.enqueue(new Callback<HashMap<Integer, String>>()
        {
            @Override
            public void onResponse(retrofit2.Call<HashMap<Integer, String>> call, Response<HashMap<Integer, String>> response)
            {
                //final HashMap<Integer, String> data = response.body();
                Toast.makeText(LoginActivity.this, "Tesssssssssssss", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<HashMap<Integer, String>> call, Throwable t)
            {
                Toast.makeText(LoginActivity.this,"Salah Pake Ini", Toast.LENGTH_LONG).show();
            }

        });
    }

    private Login setlogin()
    {
        Login login = new Login();
        login.setId(password.getText().toString());
        login.setName(username.getText().toString());
        return login;
    }
}
