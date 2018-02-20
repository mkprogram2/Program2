package com.mk.admin.payroll.main.employee.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.EmployeeService;
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
    private Button cancel_overtime;

    private OvertimeService overtimeService;
    private Session session;
    private Overtime overtime;
    private ImageView cancel_approved;
    private ImageView approved_overtime;

    public MyOvertimeFragment ()
    {
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
        cancel_overtime = (Button)viewFrag1.findViewById(R.id.cancel_overtime);

        GetMyOvertime(session.getId());
        cancel_overtime.setEnabled(false);

        cancel_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(viewFrag1.getContext());
                dialog.setContentView(R.layout.approved_overtime_dialog);
                dialog.setTitle("Canceled Overtime");
                TextView aprroved_question = (TextView)dialog.findViewById(R.id.aprroved_question);
                cancel_approved = (ImageView)dialog.findViewById(R.id.cancel_approved);
                approved_overtime = (ImageView)dialog.findViewById(R.id.delete_approved);
                aprroved_question.setText("Canceled Overtime On " + overtime.date + " ?");
                dialog.show();

                cancel_approved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                approved_overtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetOvertime();
                        dialog.dismiss();
                    }
                });
            }
        });

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
                    if (overtime.start != null)
                        start_overtime.setText(overtime.start.toString());
                    if (overtime.stop != null)
                        end_overtime.setText(overtime.stop.toString());
                    cancel_overtime.setEnabled(true);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"You Have Not Overtime For Today", Toast.LENGTH_LONG).show();
                cancel_overtime.setEnabled(false);
                EmptyUI();
            }
        });
    }

    private void CancelOvertime (Overtime overtimes)
    {
        Call<Overtime> call = overtimeService.ApprovedOvertime(overtimes, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    Toast.makeText(viewFrag1.getContext(),"Overtime Canceled", Toast.LENGTH_SHORT).show();
                    GetMyOvertime(session.getId());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(viewFrag1.getContext(),"Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetOvertime ()
    {
        overtime.status = 3;
        CancelOvertime(overtime);
    }

    private void EmptyUI ()
    {
        start_overtime.setText("");
        end_overtime.setText("");
        duration_overtime.setText("");
        date_overtime.setText("");
        information_overtime.setText("");
    }
}
