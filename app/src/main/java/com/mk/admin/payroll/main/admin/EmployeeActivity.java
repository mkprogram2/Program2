package com.mk.admin.payroll.main.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.admin.payroll.R;
import com.mk.admin.payroll.common.ClientService;
import com.mk.admin.payroll.common.Session;
import com.mk.admin.payroll.main.admin.adapter.EmployeeAdapter;
import com.mk.admin.payroll.main.admin.adapter.ItemClickSupport;
import com.mk.admin.payroll.model.Person;
import com.mk.admin.payroll.model.Role;
import com.mk.admin.payroll.model.Shift;
import com.mk.admin.payroll.service.EmployeeService;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends AppCompatActivity
{
    @BindView(R.id.employee_id)
    EditText employee_id;
    @BindView(R.id.employee_name)
    EditText employee_name;
    @BindView(R.id.employee_email)
    EditText employee_email;
    @BindView(R.id.employee_datein)
    EditText employee_datein;
    @BindView(R.id.employee_npwp)
    EditText employee_npwp;
    @BindView(R.id.employee_phone)
    EditText employee_phone;
    @BindView(R.id.employee_birth)
    EditText employee_birth;
    @BindView(R.id.employee_gender)
    Spinner employee_gender;
    @BindView(R.id.role_spin)
    Spinner role_spin;
    @BindView(R.id.shift_spin)
    Spinner shift_spin;
    @BindView(R.id.shift_in)
    TextView shift_in;
    @BindView(R.id.shift_out)
    TextView shift_out;
    @BindView(R.id.save_employee)
    ImageView save_employee;
    @BindView(R.id.edit_employee)
    ImageView edit_employee;
    @BindView(R.id.cancel_edit)
    ImageView cancel_edit;
    @BindView(R.id.employee_photo)
    ImageView employee_photo;
    @BindView(R.id.select_employee)
    Button select_employee;
    @BindView(R.id.add_employee)
    Button add_employee;
    /*@BindView(R.id.calendar_birth)
    Button calendar_birth;*/

    private EmployeeService employeeService;
    private String shiftid, role, mid, mname;
    final List<String> list_shift = new ArrayList<String>();
    final List<String> list_role = new ArrayList<String>();
    private List<Shift> shifts = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Person dataperson;
    private RecyclerView rvCategory;
    private List<Person> list;
    private Session session;
    private String[] genderSpinner = new String[] {"Male", "Female"};
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private Date dates;
    private static int RESULT_LOAD_IMAGE = 1;
    private String birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        session = new Session(this);
        ButterKnife.bind(this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DisableUI();

        employeeService = ClientService.createService().create(EmployeeService.class);

        shift_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                for(int i = 0; i < list_shift.size (); i++)
                {
                    if(list_shift.get(i).equals(shift_spin.getSelectedItem().toString()))
                    {
                        shift_in.setText(shifts.get(i).workstart);
                        shift_out.setText(shifts.get(i).workend);
                        Toast.makeText(EmployeeActivity.this,shifts.get(i).workstart, Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SetGenderSpinner();

        save_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mid == null)
                {
                    select_employee.setError("Please Select Employee");
                }
                else
                {
                    SetPerson();
                    SetRole();
                    SetShift();
                    PutEmployee(dataperson);
                }
            }
        });

        edit_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableUI();
            }
        });

        cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisableUI();
            }
        });

        select_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPerson();
            }
        });

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeActivity.this, AddEmployeeActivity.class));
            }
        });

        employee_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        employee_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        /*calendar_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            employee_photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bitmap = ((BitmapDrawable) employee_photo.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataperson.image = baos.toByteArray();
        }
    }

    private void GetEmployee (String id)
    {
        Call<Person> call = employeeService.GetEmployee(id, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    dataperson = response.body();
                    Log.d("Data", dataperson.name);
                    shiftid = dataperson.persondetail.Shift.id;
                    role = dataperson.Role.name;
                    GetShift();
                    GetRole();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetPerson ()
    {
        dataperson.name = employee_name.getText().toString();
        dataperson.email = employee_email.getText().toString();
        dataperson.npwp = employee_npwp.getText().toString();
        dataperson.phone = employee_phone.getText().toString();
        dataperson.gender = employee_gender.getSelectedItem().toString().charAt(0);
        try
        {
            Date parse = dateFormatter.parse(employee_birth.getText().toString());
            dataperson.birthdate = new java.sql.Date (parse.getTime());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void GetShift()
    {
        Call<List<Shift>> call = employeeService.GetShifts(session.getAccesstoken());
        call.enqueue(new Callback<List<Shift>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Shift>> call, Response<List<Shift>> response)
            {
                shifts.clear();
                list_shift.clear();
                shifts = response.body();
                Log.d("Shift", shifts.get(0).workstart);
                for (int i = 0; i < shifts.size(); i++)
                {
                    list_shift.add(shifts.get(i).id);
                }
                SetShiftSpinner();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Shift>> call, Throwable t)
            {
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetRole()
    {
        Call<List<Role>> call = employeeService.GetRoles(session.getAccesstoken());
        call.enqueue(new Callback<List<Role>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Role>> call, Response<List<Role>> response)
            {
                roles.clear();
                list_role.clear();
                roles = response.body();
                Log.d("Role", roles.get(0).name);
                for (int i = 0; i < roles.size(); i++)
                {
                    list_role.add(roles.get(i).name);
                }
                SetRoleSpinner();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Role>> call, Throwable t)
            {
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetShiftSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_shift);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shift_spin.setAdapter(adp1);

        for(int i = 0; i < list_shift.size (); i++)
        {
            if(list_shift.get(i).equals(shiftid)) {
                shift_spin.setSelection(i);
            }
        }
    }

    private void SetRoleSpinner()
    {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_role);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spin.setAdapter(adp1);

        for(int i = 0; i < list_role.size (); i++)
        {
            if(list_role.get(i).equals(role))
            {
                role_spin.setSelection(i);
            }
        }
    }

    private void PutEmployee(Person persons)
    {
        Call<Person> call = employeeService.PutEmployee(persons, session.getAccesstoken());
        call.enqueue(new Callback<Person>()
        {
            @Override
            public void onResponse(retrofit2.Call<Person> call, Response<Person> response)
            {
                if (response.isSuccessful())
                {
                    dataperson = response.body();
                    Log.d("Data", dataperson.name);
                    Toast.makeText(EmployeeActivity.this,"Saving Success", Toast.LENGTH_LONG).show();
                    DisableUI();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Person> call, Throwable t)
            {
                Log.d("Error here", t.getMessage());
                Toast.makeText(EmployeeActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetRole()
    {
        for (int i = 0; i < roles.size(); i++)
        {
            if (roles.get(i).name.equals(role_spin.getSelectedItem()))
            {
                dataperson.Role.id = roles.get(i).id;
                dataperson.Role.name = roles.get(i).name;
                dataperson.Role.RoleDetail.maxsalary = roles.get(i).RoleDetail.maxsalary;
                dataperson.Role.RoleDetail.minsalary = roles.get(i).RoleDetail.minsalary;
            }
        }
    }

    private void SetShift()
    {
        for (int i = 0; i < shifts.size(); i++)
        {
            if (shifts.get(i).id.equals(shift_spin.getSelectedItem()))
            {
                dataperson.persondetail.Shift.id = shifts.get(i).id;
                dataperson.persondetail.Shift.workstart = shifts.get(i).workstart;
                dataperson.persondetail.Shift.workend = shifts.get(i).workend;
            }
        }
    }

    private void GetPerson ()
    {
        Call<List<Person>> call = employeeService.GetPerson(session.getAccesstoken());
        call.enqueue(new Callback<List<Person>>()
        {
            @Override
            public void onResponse(retrofit2.Call<List<Person>> call, Response<List<Person>> response)
            {
                if (response.isSuccessful())
                {
                    list = response.body();
                    Log.d("Data",list.get(0).name);
                    Log.d("Size", String.valueOf(list.size()));
                    showRecyclerList();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Person>> call, Throwable t)
            {
                Toast.makeText(EmployeeActivity.this,"Server Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList()
    {
        // custom dialog
        final Dialog dialog = new Dialog(EmployeeActivity.this);
        dialog.setContentView(R.layout.employee_list_dialog);
        dialog.setTitle("Employee");

        dialog.show();

        rvCategory = (RecyclerView)dialog.findViewById(R.id.rv_employees);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(EmployeeActivity.this));
        EmployeeAdapter EmployeeAdapter = new EmployeeAdapter(EmployeeActivity.this);
        EmployeeAdapter.setListPerson(list);
        rvCategory.setAdapter(EmployeeAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedPerson(list.get(position));
                dialog.dismiss();
            }
        });
    }

    private void showSelectedPerson(Person person)
    {
        mid = person.id;
        mname = person.name;

        employee_id.setText(mid);
        employee_name.setText(mname);
        employee_email.setText(person.email);
        employee_npwp.setText(person.npwp);
        employee_phone.setText(person.phone);
        employee_birth.setText(String.valueOf(person.birthdate));
        employee_datein.setText(person.persondetail.assignwork);

        if (person.image != null)
        {
            byte[] data = person.image;
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            employee_photo.setImageBitmap(bmp);
        }

        for (int i =0; i < genderSpinner.length; i++)
        {
            if (genderSpinner[i].equals(person.gender))
            {
                employee_gender.setSelection(i);
            }
        }

        GetEmployee(mid);
    }

    private void DisableUI ()
    {
        employee_id.setEnabled(false);
        employee_name.setEnabled(false);
        employee_photo.setEnabled(false);
        role_spin.setEnabled(false);
        shift_spin.setEnabled(false);
        employee_email.setEnabled(false);
        employee_datein.setEnabled(false);
        employee_npwp.setEnabled(false);
        employee_phone.setEnabled(false);
        employee_birth.setEnabled(false);
        employee_gender.setEnabled(false);
        //calendar_birth.setEnabled(false);
        save_employee.setVisibility(View.GONE);
        cancel_edit.setVisibility(View.GONE);
        edit_employee.setVisibility(View.VISIBLE);
    }

    private void EnableUI ()
    {
        edit_employee.setVisibility(View.GONE);
        save_employee.setVisibility(View.VISIBLE);
        cancel_edit.setVisibility(View.VISIBLE);
        employee_name.setEnabled(true);
        employee_email.setEnabled(true);
        employee_photo.setEnabled(true);
        employee_npwp.setEnabled(true);
        employee_phone.setEnabled(true);
        employee_birth.setEnabled(true);
        employee_gender.setEnabled(true);
        //calendar_birth.setEnabled(true);
    }

    private void SetGenderSpinner ()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employee_gender.setAdapter(adapter);
    }

    private void showDateDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                employee_birth.setText(dateFormatter.format(newDate.getTime()).toString());

                try {
                    dates = dateFormatter.parse(employee_birth.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("date", dateFormatter.format(dates).toString());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
