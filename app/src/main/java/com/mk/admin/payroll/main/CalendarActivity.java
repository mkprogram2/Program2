package com.mk.admin.payroll.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mk.admin.payroll.main.adapter.EventAdapter;
import com.mk.admin.payroll.main.admin.EmployeeActivity;
import com.mk.admin.payroll.main.admin.EmployeeRecyclerActivity;
import com.mk.admin.payroll.main.admin.RemunerationActivity;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.model.Holiday;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.service.CalendarService;
import com.mk.admin.payroll.service.EmployeeService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<Holiday> holidays = new ArrayList();
    private CalendarService calendarService;
    private String persons;
    private Integer currentDay, currentMonth, currentYear;
    private RecyclerView rvEvent;

    @BindView(R.id.no_event)
    TextView no_event;

    public Runnable calendarUpdater = new Runnable() {
        @SuppressLint("WrongConstant")
        public void run() {
            items.clear();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            for(int i = 0; i < 7; ++i) {
                df.format(itemmonth.getTime());
                itemmonth.add(5, 1);
            }
            GetHolidays(persons, currentMonth, currentYear);
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

        Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH) + 1;
        currentYear = instance.get(Calendar.YEAR);
        currentDay = instance.get(Calendar.DATE);

        GridView gridview = (GridView)this.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title_calendar);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        rvEvent = (RecyclerView)findViewById(R.id.rv_event);
        rvEvent.setHasFixedSize(true);

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

                int temp = 0;

                for (int i = 0; i < holidays.size(); i++)
                {
                    if (holidays.get(i).date.equals(selectedGridDate))
                    {
                        if (holidays.size() > 0)
                        {
                            rvEvent.setVisibility(View.VISIBLE);
                            no_event.setVisibility(View.GONE);
                            showRecyclerList();
                            temp = 1;
                        }
                        else
                        {
                            Toast.makeText(CalendarActivity.this, "Server Failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if (temp != 1)
                {
                    rvEvent.setVisibility(View.GONE);
                    no_event.setVisibility(View.VISIBLE);
                }
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
        currentMonth = Integer.parseInt(android.text.format.DateFormat.format("M", month).toString());
        currentYear = Integer.parseInt(android.text.format.DateFormat.format("yyyy", month).toString());
        adapter.notifyDataSetChanged();
        handler.post(this.calendarUpdater);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        Log.d("TEST MONTH", android.text.format.DateFormat.format("M yyyy", month).toString());
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
                for (int i = 0; i < holidays.size(); i++)
                {
                    items.add(holidays.get(i).date);
                }
                adapter.setItems(items);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Holiday>> call, Throwable t)
            {
                Toast.makeText(CalendarActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showRecyclerList(){
        rvEvent.setLayoutManager(new LinearLayoutManager(this));
        EventAdapter EventAdapter = new EventAdapter(this);
        EventAdapter.setHolidays(holidays);
        rvEvent.setAdapter(EventAdapter);

        ItemClickSupport.addTo(rvEvent).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(holidays.get(position));
            }
        });
    }

    private void showSelectedPerson(Holiday holiday){
        Toast.makeText(this, "Event :  "+ holiday.name, Toast.LENGTH_SHORT).show();
    }
}