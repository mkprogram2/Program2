package com.mk.admin.payroll.main;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.SharedPreferenceEditor;
import com.mk.admin.payroll.main.adapter.CalendarAdapter;
import com.mk.admin.payroll.main.admin.EmployeeRecyclerActivity;
import com.mk.admin.payroll.model.Holiday;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.CalendarService;
import com.mk.admin.payroll.service.EmployeeService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarActivity extends AppCompatActivity {

    public GregorianCalendar month;
    public GregorianCalendar itemmonth;
    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items;
    private List<Holiday> holidays = new ArrayList<>();
    private CalendarService calendarService;
    private String persons;

    public Runnable calendarUpdater = new Runnable() {
        @SuppressLint("WrongConstant")
        public void run() {
            items.clear();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            for(int i = 0; i < 7; ++i) {
                df.format(itemmonth.getTime());
                itemmonth.add(5, 1);
                items.add("2018-01-12");
                items.add("2018-01-07");
                items.add("2018-01-15");
                items.add("2018-01-20");
                items.add("2018-02-01");
                items.add("2018-02-02");
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

    public CalendarActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Locale.setDefault(Locale.US);

        ButterKnife.bind(this);

        calendarService = ClientService.createService().create(CalendarService.class);
        persons = SharedPreferenceEditor.LoadPreferences(this,"Persons","");

        month = (GregorianCalendar)GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar)month.clone();
        items = new ArrayList();
        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView)this.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title_calendar);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout)findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                ((CalendarAdapter)parent.getAdapter()).setSelected(v);
                String selectedGridDate = (String)CalendarAdapter.dayString.get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if(gridvalue > 10 && position < 8) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if(gridvalue < 7 && position > 28) {
                    setNextMonth();
                    refreshCalendar();
                }

                ((CalendarAdapter)parent.getAdapter()).setSelected(v);
                Toast.makeText(CalendarActivity.this, selectedGridDate, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("WrongConstant")
    protected void setNextMonth() {
        if(month.get(2) == month.getActualMaximum(2)) {
            month.set(month.get(1) + 1, month.getActualMinimum(2), 1);
        } else {
            month.set(2, month.get(2) + 1);
        }
    }

    @SuppressLint("WrongConstant")
    protected void setPreviousMonth() {
        if(month.get(2) == month.getActualMinimum(2)) {
            month.set(month.get(1) - 1, month.getActualMaximum(2), 1);
        } else {
            month.set(2, month.get(2) - 1);
        }
    }

    public void refreshCalendar() {
        TextView title = (TextView)this.findViewById(R.id.title_calendar);
        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(this.calendarUpdater);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    private void GetHolidays (String persons, Integer month, Integer year)
    {
        Call<List<Holiday>> call = calendarService.getHoliday(persons, month, year);
        call.enqueue(new Callback<List<Holiday>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Holiday>> call, Response<List<Holiday>> response)
            {
                holidays = response.body();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Holiday>> call, Throwable t)
            {
                Toast.makeText(CalendarActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}