package com.mk.admin.payroll.main.employee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.OvertimeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOvertimeFragment extends android.support.v4.app.Fragment {
    private View viewFrag1;
    private TextView start_overtime;
    private TextView end_overtime;
    private TextView duration_overtime;
    private TextView date_overtime;
    private TextView information_overtime;

    private OvertimeService overtimeService;
    private Session session;
    private Overtime overtime;

    public MyOvertimeFragment () {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewFrag1 = inflater.inflate(R.layout.activity_my_overtime_fragment, container, false);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        session = new Session(viewFrag1.getContext());

        start_overtime = (TextView)viewFrag1.findViewById(R.id.start_overtime);
        end_overtime = (TextView)viewFrag1.findViewById(R.id.end_overtime);
        duration_overtime = (TextView)viewFrag1.findViewById(R.id.duration_overtime);
        date_overtime = (TextView)viewFrag1.findViewById(R.id.date_overtime);
        information_overtime = (TextView)viewFrag1.findViewById(R.id.information_overtime);

        GetMyOvertime(session.getId());

        return viewFrag1;
    }

    private void GetMyOvertime (String id)
    {
        Call<Overtime> call = overtimeService.GetMyOvertime(id, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    Integer duration_over = overtime.duration / 3600;
                    duration_overtime.setText(duration_over.toString() + " Hours");
                    date_overtime.setText(overtime.date.toString());
                    information_overtime.setText(overtime.information.toString());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"You Have Not Overtime For Today", Toast.LENGTH_LONG).show();
            }
        });
    }
}
