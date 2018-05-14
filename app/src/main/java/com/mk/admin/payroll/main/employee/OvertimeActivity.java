package com.mk.admin.payroll.main.employee;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.model.Overtime;
import com.mk.admin.payroll.service.OvertimeService;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OvertimeActivity extends AppCompatActivity
{

    /*@BindView(R.id.myover)
    ImageView myover;
    @BindView(R.id.listover)
    ImageView listover;*/
    @BindView(R.id.date_overtime)
    TextView date_overtime;
    @BindView(R.id.time_overtime)
    TextView time_overtime;
    @BindView(R.id.start_overtime)
    TextView start_overtime;
    @BindView(R.id.end_overtime)
    TextView end_overtime;
    @BindView(R.id.duration_overtime)
    TextView duration_overtime;
    @BindView(R.id.information_overtime)
    TextView information_overtime;
    @BindView(R.id.cancel_overtime)
    Button cancel_overtime;
    @BindView(R.id.main_content)
    LinearLayout main_content;
    @BindView(R.id.data_not_found)
    LinearLayout data_not_found;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.back_icon)
    ImageView back_icon;


    private OvertimeService overtimeService;
    private Session session;
    private Overtime overtime;
    private Button cancel_approved;
    private Button approved_overtime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);
        ButterKnife.bind(this);

        overtimeService = ClientService.createService().create(OvertimeService.class);
        session = new Session(this);

        GetMyOvertime(session.getId());
        cancel_overtime.setEnabled(false);


        cancel_overtime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(OvertimeActivity.this);
                dialog.setContentView(R.layout.approved_overtime_dialog);
                dialog.setTitle("Canceled Overtime");
                TextView aprroved_question = (TextView)dialog.findViewById(R.id.aprroved_question);
                cancel_approved = (Button)dialog.findViewById(R.id.cancel_approved);
                approved_overtime = (Button)dialog.findViewById(R.id.delete_approved);
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

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });

        data_not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                GetMyOvertime(session.getId());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void GetMyOvertime (String id)
    {
        progress_bar.setVisibility(View.VISIBLE);
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

                    progress_bar.setVisibility(View.GONE);
                    main_content.setVisibility(View.VISIBLE);
                    data_not_found.setVisibility(View.GONE);
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    data_not_found.setVisibility(View.VISIBLE);
                    main_content.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                progress_bar.setVisibility(View.GONE);
                data_not_found.setVisibility(View.VISIBLE);
                main_content.setVisibility(View.GONE);
                cancel_overtime.setEnabled(false);
                EmptyUI();
            }
        });
    }

    private void CancelOvertime (Overtime overtimes)
    {
        progress_bar.setVisibility(View.VISIBLE);
        Call<Overtime> call = overtimeService.ApprovedOvertime(overtimes, session.getAccesstoken());
        call.enqueue(new Callback<Overtime>()
        {
            @Override
            public void onResponse(retrofit2.Call<Overtime> call, Response<Overtime> response)
            {
                if (response.isSuccessful())
                {
                    overtime = response.body();
                    Toast.makeText(OvertimeActivity.this,"Overtime Canceled !", Toast.LENGTH_SHORT).show();
                    GetMyOvertime(session.getId());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Overtime> call, Throwable t)
            {
                Toast.makeText(OvertimeActivity.this,"Server Failed !", Toast.LENGTH_SHORT).show();
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
