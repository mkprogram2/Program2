package com.mk.admin.payroll.main.manage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.model.Overtime;

import java.util.List;

/**
 * Created by admin on 2/8/2018.
 */

public class EmployeeOvertimeAdapter extends RecyclerView.Adapter<EmployeeOvertimeAdapter.CategoryViewHolder> {

    private Context context;
    private List<Overtime> overtimes;

    public List<Overtime> getOvertimes() {
        return overtimes;
    }

    public void setOvertimes(List<Overtime> overtimes) {
        this.overtimes = overtimes;
    }

    public EmployeeOvertimeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public EmployeeOvertimeAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.overtime_adapter, parent, false);
        return new EmployeeOvertimeAdapter.CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(EmployeeOvertimeAdapter.CategoryViewHolder holder, int position) {

        holder.overtime_date.setText(getOvertimes().get(position).date.toString());
        holder.overtime_duration.setText(getOvertimes().get(position).duration.toString());
        holder.overtime_status.setText(getOvertimes().get(position).status.toString());
    }

    @Override
    public int getItemCount()
    {
        return getOvertimes().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView overtime_date;
        TextView overtime_duration;
        TextView overtime_status;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            overtime_date = (TextView)itemView.findViewById(R.id.overtime_date);
            overtime_duration = (TextView)itemView.findViewById(R.id.overtime_duration);
            overtime_status = (TextView)itemView.findViewById(R.id.overtime_status);
        }
    }
}
