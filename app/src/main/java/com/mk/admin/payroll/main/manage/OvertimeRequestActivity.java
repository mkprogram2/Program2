package com.mk.admin.payroll.main.manage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.IDs;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.MainActivity;
import com.mk.admin.payroll.main.admin.EmployeeActivity;
import com.mk.admin.payroll.main.admin.EmployeeRecyclerActivity;
import com.mk.admin.payroll.main.admin.RemunerationActivity;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.main.manage.adapter.OvertimeAdapter;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.OvertimeService;
import com.mk.admin.payroll.service.WorkhourService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OvertimeRequestActivity extends AppCompatActivity {

    private RecyclerView rvOvertime;
    private List<Overtime> overtimes;
    private OvertimeService overtimeService;
    private String persons;
    private Overtime overtime = new Overtime();
    private String mid, mname;

    @BindView(R.id.add_overtime)
    TextView add_overtime;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_overtime_request);

        ButterKnife.bind (this);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        rvOvertime = (RecyclerView)findViewById(R.id.rv_overtime);
        rvOvertime.setHasFixedSize(true);

        mid = IDs.getIdUser();
        mname = IDs.getLoginUser();

        String strRequestBody = mid;
        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        GetOvertime(persons, requestBody);

        add_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OvertimeRequestActivity.this, AddOvertimeActivity.class));
            }
        });
    }

    private void GetOvertime (String persons, RequestBody requestBody)
    {
        Call<List<Overtime>> call = overtimeService.GetOvertime(persons, requestBody);
        call.enqueue(new Callback<List<Overtime>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Overtime>> call, Response<List<Overtime>> response)
            {
                overtimes = response.body();
                showRecyclerList();
            }
            @Override
            public void onFailure(retrofit2.Call<List<Overtime>> call, Throwable t)
            {
                Toast.makeText(OvertimeRequestActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList(){
        rvOvertime.setLayoutManager(new LinearLayoutManager(this));
        OvertimeAdapter OvertimeAdapter = new OvertimeAdapter(this);
        OvertimeAdapter.setOvertimes(overtimes);
        rvOvertime.setAdapter(OvertimeAdapter);

        ItemClickSupport.addTo(rvOvertime).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(overtimes.get(position));
            }
        });
    }

    private void showSelectedPerson(Overtime overtime){
        Toast.makeText(this, "You Choose "+overtime.date.toString(), Toast.LENGTH_SHORT).show();
    }
}
