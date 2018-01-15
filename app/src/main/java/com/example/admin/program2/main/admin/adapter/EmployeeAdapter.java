package com.example.admin.program2.main.admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.program2.R;
import com.example.admin.program2.model.person;

import java.util.ArrayList;

/**
 * Created by admin on 1/15/2018.
 */

public class EmployeeAdapter  extends RecyclerView.Adapter<EmployeeAdapter.CategoryViewHolder>{
    private Context context;

    public ArrayList<person> getListPerson() {
        return listPerson;
    }
    public void setListPerson(ArrayList<person> listPresident) {
        this.listPerson = listPresident;
    }
    private ArrayList<person>listPerson;

    public EmployeeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_adapter, parent, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        holder.tvName.setText("test");
        holder.tvRemarks.setText("test");

        /*Glide.with(context)
                .load(getListPresident().get(position).getPhoto())
                .override(55, 55)
                .crossFade()
                .into(holder.imgPhoto);*/
    }

    @Override
    public int getItemCount() {
        return getListPerson().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvRemarks;
        ImageView imgPhoto;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tv_item_name);
            tvRemarks = (TextView)itemView.findViewById(R.id.tv_item_remarks);
            imgPhoto = (ImageView)itemView.findViewById(R.id.img_item_photo);
        }
    }
}
