package com.mk.admin.payroll.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.model.Holiday;

import java.util.List;

/**
 * Created by admin on 1/29/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CategoryViewHolder> {
    private Context context;
    private List<Holiday> holidays;

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public EventAdapter(Context context) {
        this.context = context;
    }

    @Override
    public EventAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter, parent, false);
        return new EventAdapter.CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(EventAdapter.CategoryViewHolder holder, int position) {
        holder.event_name.setText(getHolidays().get(position).name.toString());
        holder.event_date.setText(getHolidays().get(position).date);
    }

    @Override
    public int getItemCount()
    {
        return getHolidays().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView event_name;
        TextView event_date;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            event_name = (TextView)itemView.findViewById(R.id.event_name);
            event_date = (TextView)itemView.findViewById(R.id.event_date);
        }
    }
}
