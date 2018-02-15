package com.mk.admin.payroll.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.main.admin.EmployeeActivity;
import com.mk.admin.payroll.main.admin.RemunerationActivity;
import com.mk.admin.payroll.main.employee.CalendarActivity;
import com.mk.admin.payroll.main.employee.OvertimeActivity;
import com.mk.admin.payroll.main.employee.ProfileActivity;
import com.mk.admin.payroll.main.employee.SalaryActivity;
import com.mk.admin.payroll.main.employee.WorkhoursActivity;
import com.mk.admin.payroll.main.manage.EmployeeOvertimeActivity;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.model.Workhour;
import com.mk.admin.payroll.service.EmployeeService;
import com.mk.admin.payroll.service.WorkhourService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EmployeeService employeeService;
    private WorkhourService service3;
    private String mid, mname, shift_workstart, shift_workend, interval_work, role_name;
    public Integer role_id;
    private NavigationView navigationView;
    private Session session;

    @BindView(R.id.employee_name)
    TextView employee_name;
    @BindView(R.id.employee_role)
    TextView employee_role;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        //startService(new Intent(HomeActivity.this, PayrollService.class));

        employeeService = ClientService.createService().create(EmployeeService.class);
        service3 = ClientService.createService().create(WorkhourService.class);
        session = new Session(this);

        GetEmployee(session.getId());

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if ( id == R.id.workhours_menu)
        {
            workhour(mid);
        }
        else if ( id == R.id.profile_menu)
        {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }
        else if ( id == R.id.calendar_menu)
        {
            startActivity(new Intent(HomeActivity.this,CalendarActivity.class));
        }
        else if ( id == R.id.salary_menu)
        {
            startActivity(new Intent(HomeActivity.this, SalaryActivity.class));
        }
        else if (id == R.id.overtime)
        {
            startActivity(new Intent(HomeActivity.this, OvertimeActivity.class));
        }
        else if ( id == R.id.employee_menu)
        {
            startActivity(new Intent(HomeActivity.this, EmployeeActivity.class));
        }
        else if ( id == R.id.req_overtime_menu)
        {
            startActivity(new Intent(HomeActivity.this, EmployeeOvertimeActivity.class));
        }
        else if (id == R.id.remuneration_menu)
        {
            startActivity(new Intent(HomeActivity.this, RemunerationActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void CheckRole ()
    {
        Menu menu = navigationView.getMenu();
        if (role_id == 1)
        {
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++)
            {
                MenuItem menuItem= menu.getItem(menuItemIndex);

                if(menuItem.getItemId() == R.id.item_manage)
                {
                    menuItem.setVisible(false);
                }
            }
        }
        else if (role_id == 2)
        {
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++)
            {
                MenuItem menuItem= menu.getItem(menuItemIndex);

                if(menuItem.getItemId() == R.id.item_human_resource)
                {
                    menuItem.setVisible(false);
                }
            }
        }
        else
        {
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++)
            {
                MenuItem menuItem = menu.getItem(menuItemIndex);

                if(menuItem.getItemId() == R.id.item_manage)
                {
                    menuItem.setVisible(false);
                    Log.d("human resource", "human");

                }
                if (menuItem.getItemId() == R.id.item_human_resource);
                {
                    menuItem.setVisible(false);
                    Log.d("manage", "manage");
                    //break;
                }
                if (menuItem.getItemId() == R.id.item_working)
                {
                    menuItem.setVisible(true);
                }
            }
        }
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

    private void workhour (String id)
    {
        Call<Workhour> call = service3.getCheckworkhour(id, session.getAccesstoken());
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

    private void GetEmployee (String id)
    {
        Call<Person> call = employeeService.GetEmployee(id, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    final Person dataperson = response.body();
                    Log.d("Data", dataperson.name);
                    mid = dataperson.id;
                    mname = dataperson.name;
                    role_name = dataperson.Role.name;
                    role_id = dataperson.Role.id;
                    shift_workstart = dataperson.persondetail.Shift.workstart;
                    shift_workend = dataperson.persondetail.Shift.workend;

                    employee_name.setText(mname);
                    employee_role.setText(role_name);

                    View headerLayout = navigationView.getHeaderView(0);
                    TextView textViewUser = (TextView) headerLayout.findViewById(R.id.employee_name);
                    textViewUser.setText(mname);
                    TextView textViewCompany = (TextView) headerLayout.findViewById(R.id.employee_role);
                    textViewCompany.setText(role_name);

                    CheckRole();

                    GetIntervalWork();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Toast.makeText(HomeActivity.this,"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
