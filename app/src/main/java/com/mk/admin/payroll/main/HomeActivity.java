package com.mk.admin.payroll.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.IDs;
import com.mk.admin.payroll.common.PayrollService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.admin.AddEmployeeActivity;
import com.mk.admin.payroll.main.admin.EmployeeRecyclerActivity;
import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.service.CheckinService;
import com.mk.admin.payroll.service.WorkhourService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CheckinService service2;
    private WorkhourService service3;
    public String mid, mname, shift_workstart, shift_workend, interval_work, role_name, role_id;
    public double gross_salary;
    public Integer mshiftid;
    private String persons;
    private NavigationView navigationView;

    @BindView(R.id.employee_name)
    TextView employee_name;
    @BindView(R.id.employee_role)
    TextView employee_role;
    @BindView(R.id.calendar)
    Button calendar;
    @BindView(R.id.checkin)
    Button checkin;
    @BindView(R.id.workhours)
    Button workhours;
    @BindView(R.id.checkout)
    Button checkout;
    @BindView(R.id.salary)
    Button salary;
    @BindView(R.id.remuneration)
    Button remuneration;
    @BindView(R.id.employee)
    Button employee;
    @BindView(R.id.add_employee)
    Button add_employee;
    @BindView(R.id.overtime)
    Button overtime;
    @BindView(R.id.req_overtime)
    Button reqovertime;
    @BindView(R.id.human_resource)
    LinearLayout human_resource;
    @BindView(R.id.manage)
    LinearLayout manage;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mid = getIntent().getExtras().getString("id");
        mname = getIntent().getExtras().getString("name");
        role_name = getIntent().getExtras().getString("role_name");
        role_id = getIntent().getExtras().getString("role");
        gross_salary = getIntent().getExtras().getDouble("salary");
        mshiftid = Integer.parseInt(getIntent().getExtras().getString("shiftid")) ;
        shift_workstart = getIntent().getExtras().getString("shift_workstart");
        shift_workend = getIntent().getExtras().getString("shift_workend");


        startService(new Intent(HomeActivity.this, PayrollService.class));

        employee_name.setText(mname);
        employee_role.setText(role_name);
        IDs.setIdUser(mid);
        IDs.setLoginUser(mname);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView textViewUser = (TextView) headerLayout.findViewById(R.id.employee_name);
        textViewUser.setText(mname);
        TextView textViewCompany = (TextView) headerLayout.findViewById(R.id.employee_role);
        textViewCompany.setText(role_name);
        
        GetIntervalWork();

        service2 = ClientService.createService().create(CheckinService.class);
        service3 = ClientService.createService().create(WorkhourService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        String strRequestBody = mid;
        final RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),strRequestBody);

        if (role_id.equals("1"))
        {
            manage.setVisibility(View.GONE);
        }
        else if (role_id.equals("2"))
        {
            human_resource.setVisibility(View.GONE);
        }
        else
        {
            human_resource.setVisibility(View.GONE);
            manage.setVisibility(View.GONE);
        }

        checkin.setOnClickListener (new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkIn(persons, requestBody);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CalendarActivity.class));
            }
        });

        workhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workhour(persons, mid);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut(persons, requestBody);
            }
        });

        salary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, SalaryActivity.class);
                intent.putExtra("id", mid);
                intent.putExtra("name", mname);
                intent.putExtra("role_name", role_name);
                startActivity(intent);
            }
        });

        remuneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EmployeeRecyclerActivity.class);
                intent.putExtra("activity", "remuneration");
                startActivity(intent);
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EmployeeRecyclerActivity.class);
                intent.putExtra("activity", "employee");
                startActivity(intent);
            }
        });

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddEmployeeActivity.class));
            }
        });

        reqovertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EmployeeRecyclerActivity.class);
                intent.putExtra("activity", "reqovertime");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.menu_logout)
        {
            Logout();
        }
        else if ( id == R.id.workhours_menu)
        {
            workhour(persons, mid);
        }
        else if ( id == R.id.calendar_menu)
        {
            startActivity(new Intent(HomeActivity.this,CalendarActivity.class));
        }
        else if ( id == R.id.salary_menu)
        {
            Intent intent = new Intent(HomeActivity.this, SalaryActivity.class);
            intent.putExtra("id", mid);
            intent.putExtra("name", mname);
            intent.putExtra("role_name", role_name);
            startActivity(intent);
        }
        else if ( id == R.id.employee_menu)
        {
            Intent intent = new Intent(HomeActivity.this, EmployeeRecyclerActivity.class);
            intent.putExtra("activity", "employee");
            startActivity(intent);
        }
        else if ( id == R.id.add_employee_menu)
        {
            startActivity(new Intent(HomeActivity.this, AddEmployeeActivity.class));
        }
        else if ( id == R.id.req_overtime_menu)
        {
            Intent intent = new Intent(HomeActivity.this, EmployeeRecyclerActivity.class);
            intent.putExtra("activity", "reqovertime");
            startActivity(intent);
        }
        else if ( id == R.id.workhours_menu)
        {
            workhour(persons, mid);
        }
        else if (id == R.id.remuneration_menu)
        {
            Intent intent = new Intent(HomeActivity.this, EmployeeRecyclerActivity.class);
            intent.putExtra("activity", "remuneration");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private void Logout()
    {
        final AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this);
        confirmationDialog.setTitle(R.string.logout_question);
        confirmationDialog.setMessage(R.string.logout_question_message);
        confirmationDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
        {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(HomeActivity.this, getString(R.string.logout_message, mname),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        confirmationDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        confirmationDialog.show();
    }
    
    private void GetIntervalWork ()
    {
        try
        {
            SimpleDateFormat date = new SimpleDateFormat("hh:mm");
            Date tglAwal = (Date) date.parse(shift_workstart);
            Date tglAkhir = (Date) date.parse(shift_workend);

            long bedaHari = Math.abs(tglAkhir.getTime() - tglAwal.getTime());
            interval_work = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(bedaHari),
                    TimeUnit.MILLISECONDS.toMinutes(bedaHari) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(bedaHari)));
        }
        catch(Exception e)
        {}
    }

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
                else if(data == 2)
                {
                    Toast.makeText(HomeActivity.this,"Anda Telah CheckIn Hari Ini", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(HomeActivity.this,"Holiday!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(HomeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkOut(final String persons, RequestBody id)
    {
        Call<Integer> call = service2.checkout(persons, id);
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
                else if (data == 2)
                {
                    Toast.makeText(HomeActivity.this,"Please Check In", Toast.LENGTH_LONG).show();
                }
                else if (data == 3)
                {
                    Toast.makeText(HomeActivity.this,"You Have Been Checked Out", Toast.LENGTH_LONG).show();
                }
                else if (data == 4)
                {
                    Toast.makeText(HomeActivity.this,"Cannot Check Out in 30 Minutes After Checked In", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                Toast.makeText(HomeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void workhour (final String persons, String id)
    {
        Call<Workhour> call = service3.getCheckworkhour(persons, id);
        call.enqueue(new Callback<Workhour>()
        {
            private String responseCode, responseMessage;

            @Override
            public void onResponse(retrofit2.Call<Workhour> call, Response<Workhour> response)
            {
                final Workhour data = response.body();

                Intent intent = new Intent(HomeActivity.this, WorkhoursActivity.class);
                intent.putExtra("workstart", data.workstart.toString());
                intent.putExtra("workstartinterval", data.workstartinterval.toString());
                intent.putExtra("interval_work", interval_work);
                intent.putExtra("personid", data.personid);
                intent.putExtra("checkout_status", data.status.toString());
                if (data.workend == null)
                {
                    intent.putExtra("workend", "null_workend");
                }
                else
                {
                    intent.putExtra("workend", data.workend.toString());
                }
                startActivity(intent);
            }

            @Override
            public void onFailure(retrofit2.Call<Workhour> call, Throwable t)
            {
                Toast.makeText(HomeActivity.this,"Please Check In", Toast.LENGTH_LONG).show();
            }

        });
    }
}
