package com.mk.admin.payroll.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.mk.admin.payroll.common.PayrollService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.main.admin.EmployeeActivity;
import com.mk.admin.payroll.main.admin.PermissionActivity;
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

import java.security.acl.Group;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.today)
    TextView today;
    @BindView(R.id.datenow)
    TextView datenow;
    @BindView(R.id.masuk)
    TextView masuk;
    @BindView(R.id.keluar)
    TextView keluar;
    @BindView(R.id.shift_in)
    TextView shift_in;
    @BindView(R.id.shift_out)
    TextView shift_out;
    @BindView(R.id.break_time)
    TextView break_time;
    @BindView(R.id.kehadiran)
    TextView kehadiran;
    @BindView(R.id.checkout)
    TextView checkout;
    @BindView(R.id.timeout)
    TextView timeout;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.home_content)
    ScrollView home_content;
    @BindView(R.id.data_not_found)
    LinearLayout data_not_found;

    private EmployeeService employeeService;
    private WorkhourService service3;
    private String mid, mname, shift_workstart, shift_workend, interval_work, role_name, workstart, workstartinterval, workend, result, result2, shifttime, EVENT_DATE_TIME;
    public Integer role_id, jam_pulang_shift, menit_pulang_shift, jam_pulang, menit_pulang, telat_jam, telat_menit, check;
    private Long workstartinterval_time;
    private String[] waktu_shift, waktu_kerja, telat_s;
    private NavigationView navigationView;
    private Session session;
    private Handler handler = new Handler();
    private Runnable runnable;
    private RequestBody requestBody;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        today.setText(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        datenow.setText(df.format(c));

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

        role_id = session.GetRole();
        CheckRole();

        data_not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                GetEmployee(session.getId());
            }
        });
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.logout)
        {
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

        if ( id == R.id.profile_menu)
        {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }
        /*else if ( id == R.id.calendar_menu)
        {
            startActivity(new Intent(HomeActivity.this,CalendarActivity.class));
        }*/
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
        else if (id == R.id.permission_menu)
        {
            startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
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
                    menuItem.setVisible(true);
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
            @Override
            public void onResponse(retrofit2.Call<Workhour> call, Response<Workhour> response)
            {
                if (response.isSuccessful())
                {
                    final Workhour data = response.body();

                    workstart = data.workstart.toString();
                    workstartinterval = data.workstartinterval.toString();
                    if (data.workend == null)
                    {
                        workend = "null_workend";
                    }
                    else
                    {
                        workend = data.workend.toString();
                    }
                    setTime();
                    progress_bar.setVisibility(View.GONE);
                    home_content.setVisibility(View.VISIBLE);
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    data_not_found.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Workhour> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                data_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    private void GetEmployee (String id)
    {
        progress_bar.setVisibility(View.VISIBLE);
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
                    shift_workstart = dataperson.persondetail.Shift.workstart;
                    shift_workend = dataperson.persondetail.Shift.workend;
                    shift_in.setText(SetShiftTime(dataperson.persondetail.Shift.workstart));
                    shift_out.setText(SetShiftTime(dataperson.persondetail.Shift.workend));
                    break_time.setText(SetShiftTime(dataperson.persondetail.Shift.breakstart) + " - " + SetShiftTime(dataperson.persondetail.Shift.breakend));

                    View headerLayout = navigationView.getHeaderView(0);
                    TextView textViewUser = (TextView) headerLayout.findViewById(R.id.employee_name);
                    textViewUser.setText(mname);
                    TextView textViewCompany = (TextView) headerLayout.findViewById(R.id.employee_role);
                    textViewCompany.setText(role_name);
                    ImageView employee_photo = (ImageView) headerLayout.findViewById(R.id.employee_photo);
                    if (dataperson.image != null)
                    {
                        byte[] data = dataperson.image;
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        employee_photo.setImageBitmap(bmp);
                    }
                    GetIntervalWork();

                    requestBody = RequestBody.create(MediaType.parse("text/plain"),session.getId());
                    Behavior(requestBody);
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    data_not_found.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                data_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setTime ()
    {
        if (workstart == null || workstartinterval == null)
        {
            kehadiran.setText("Please Check In");
        }
        else
        {
            workstartinterval_time = Long.parseLong(workstartinterval);
            Log.d("workinterval", workstartinterval_time.toString());

            GetWorkstart(workstart);

            masuk.setText(result2.toString());

            waktu_shift = interval_work.split(":");
            jam_pulang_shift = Integer.parseInt(waktu_shift[0]);
            menit_pulang_shift = Integer.parseInt((waktu_shift[1]));

            waktu_kerja = result2.split(":");
            jam_pulang = Integer.parseInt(waktu_kerja[0]) + jam_pulang_shift;
            menit_pulang = Integer.parseInt(waktu_kerja[1]) + menit_pulang_shift;

            telat_s = workstartinterval.split("-");
            telat_jam = Integer.parseInt(workstartinterval) / 3600;
            telat_menit = (Integer.parseInt(workstartinterval) % 3600) / 60;
            check = Integer.parseInt(workstartinterval);

            if (workstartinterval_time < 0)
            {
                if (telat_jam < 0)
                {
                    String[] hour_late = telat_jam.toString().split("-");
                    if (telat_menit < 0)
                    {
                        String[] minute_late = telat_menit.toString().split("-");
                        kehadiran.setText("Late " + hour_late[1] + " Hours : " + minute_late[1] + " Minutes");
                    }
                    else
                    {
                        kehadiran.setText("Late " + hour_late[1] + " Hours : " + telat_menit + " Minutes");
                    }
                }
                else if (telat_menit < 0)
                {
                    String[] minute_late = telat_menit.toString().split("-");
                    if (telat_jam < 0)
                    {
                        String[] hour_late = telat_jam.toString().split("-");
                        kehadiran.setText("Late " + hour_late[1] + " Hours : " + minute_late[1] + " Minutes");
                    }
                    else
                    {
                        kehadiran.setText("Late " + telat_jam + " Hours : " + minute_late[1] + " Minutes");
                    }
                }
                else
                {
                    kehadiran.setText("Late " + telat_jam + " Hours : " + telat_menit + " Minutes");
                }
            }
            else
            {
                kehadiran.setText("On Time");
            }

            if (jam_pulang > 23)
            {
                timeout.setText("23:59");
                EVENT_DATE_TIME = "0" + jam_pulang + ":" + menit_pulang + ":00";
            }
            else
            {
                if (jam_pulang <10)
                {
                    timeout.setText("0" +jam_pulang + ":" + menit_pulang);
                    EVENT_DATE_TIME = "0" + jam_pulang + ":" + menit_pulang + ":00";
                }
                else if(menit_pulang < 10)
                {
                    timeout.setText(jam_pulang + ":0" + menit_pulang);
                    EVENT_DATE_TIME =jam_pulang + ":0" + menit_pulang + ":00";
                }
                else
                {
                    timeout.setText(jam_pulang + ":" + menit_pulang);
                    EVENT_DATE_TIME = jam_pulang + ":" + menit_pulang + ":00";
                }
            }
        }

        if (workend.equals("null_workend"))
        {
            keluar.setText("");
            checkout.setText("Checked Out");
        }
        else
        {
            GetWorkend(workend);
            keluar.setText(result);
            checkout.setText("Checked In");
        }

        countDownStart();
    }

    private void GetWorkstart (String worktime)
    {
        SimpleDateFormat formatterr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        try {
            Date dates = formatterr.parse(worktime);
            formatterr.applyPattern("HH:mm");
            Log.d("Date", dates.toString());
            result2 = formatterr.format(dates);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void GetWorkend (String worktime)
    {
        SimpleDateFormat formatterr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        try {
            Date dates = formatterr.parse(worktime);
            formatterr.applyPattern("HH:mm");
            result = formatterr.format(dates);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void countDownStart()
    {
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = dateFormat.parse(dateFormat.format(new Date()));
                    long diff = event_date.getTime() - current_date.getTime();
                    long Days = diff / (24 * 60 * 60 * 1000);
                    long Hours = diff / (60 * 60 * 1000) % 24;
                    long Minutes = diff / (60 * 1000) % 60;
                    long Seconds = diff / 1000 % 60;

                    //tv_hour.setText(String.format("%02d", Hours) + " : " + String.format("%02d", Minutes) + " : " + String.format("%02d", Seconds));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private String SetShiftTime (String time)
    {
        SimpleDateFormat formatterr = new SimpleDateFormat("HH:mm:ss");

        try {
            Date dates = formatterr.parse(time);
            formatterr.applyPattern("HH:mm");
            shifttime = formatterr.format(dates);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return shifttime;
    }


    private void Behavior (RequestBody id)
    {
        Call<Integer> call = service3.postBehavior(id, session.getAccesstoken());
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                if (response.isSuccessful())
                {
                    workhour(session.getId());
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    data_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(HomeActivity.this,"Data Not Found !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                data_not_found.setVisibility(View.VISIBLE);
                Toast.makeText(HomeActivity.this,"Server Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }
}
