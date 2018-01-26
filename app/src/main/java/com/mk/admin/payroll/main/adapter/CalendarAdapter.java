package com.mk.admin.payroll.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mk.admin.payroll.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by admin on 1/26/2018.
 */

public class CalendarAdapter extends BaseAdapter {

    private Context mContext;
    private Calendar month;
    public GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue;
    String curentDateString;
    DateFormat df;
    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    @SuppressLint("WrongConstant")
    public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
        dayString = new ArrayList();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar)monthCalendar.clone();
        mContext = c;
        month.set(5, 1);
        items = new ArrayList();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public void setItems(ArrayList<String> items) {
        for(int i = 0; i != items.size(); ++i) {
            if(((String)items.get(i)).length() == 1) {
                items.set(i, "0" + (String)items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0L;
    }

    @SuppressLint("ResourceType")
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null) {
            @SuppressLint("WrongConstant") LayoutInflater vi = (LayoutInflater)mContext.getSystemService("layout_inflater");
            v = vi.inflate(R.layout.calendar_item, (ViewGroup)null);
        }

        TextView dayView = (TextView)v.findViewById(R.id.date);
        String[] separatedTime = ((String)dayString.get(position)).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if(Integer.parseInt(gridvalue) > 1 && position < firstDay) {
            dayView.setTextColor(-1);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if(Integer.parseInt(gridvalue) < 7 && position > 28) {
            dayView.setTextColor(-1);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            dayView.setTextColor(Color.BLACK);
        }

        if(((String)dayString.get(position)).equals(this.curentDateString)) {
            setSelected(v);
            previousView = v;
        } else {
            v.setBackgroundResource(R.drawable.list_item_background);
        }

        dayView.setText(gridvalue);
        String date = (String)dayString.get(position);
        if(date.length() == 1) {
            date = "0" + date;
        }

        String monthStr = "" + (month.get(2) + 1);
        if(monthStr.length() == 1) {
            (new StringBuilder("0")).append(monthStr).toString();
        }

        ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        if(date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(0);
        } else {
            iw.setVisibility(4);
        }

        return v;
    }

    public View setSelected(View view) {
        if(previousView != null) {
            previousView.setBackgroundResource(R.drawable.list_item_background);
        }

        this.previousView = view;
        view.setBackgroundResource(R.drawable.calendar_cel_selectl);
        return view;
    }

    @SuppressLint("WrongConstant")
    public void refreshDays() {
        items.clear();
        dayString.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar)month.clone();
        firstDay = month.get(7);
        maxWeeknumber = month.getActualMaximum(4);
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP();
        calMaxP = maxP - (firstDay - 1);
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        pmonthmaxset.set(5, calMaxP + 1);

        for(int n = 0; n < mnthlength; ++n) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(5, 1);
            dayString.add(itemvalue);
        }

    }

    @SuppressLint("WrongConstant")
    private int getMaxP() {
        if(month.get(2) == month.getActualMinimum(2)) {
            pmonth.set(month.get(1) - 1, month.getActualMaximum(2), 1);
        } else {
            pmonth.set(2, month.get(2) - 1);
        }

        int maxP = pmonth.getActualMaximum(5);
        return maxP;
    }
}
