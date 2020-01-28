package com.example.surgicallogbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Operation extends AppCompatActivity {

    private EditText operationDate, operationDateIn, operationDateOut, operationTimeIn, operationTimeOut, surgeon;
    private Spinner position;
    private String timeIn, timeOut;
    private boolean validTime = false;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        getSupportActionBar().setTitle("Operation");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        calendar = Calendar.getInstance();

        operationDate = findViewById(R.id.operationDate);
        operationDateIn = findViewById(R.id.operationDateIn);
        operationDateOut = findViewById(R.id.operationDateOut);
        operationTimeIn = findViewById(R.id.operationTimeIn);
        operationTimeOut = findViewById(R.id.operationTimeOut);
        position = findViewById(R.id.position);
        surgeon = findViewById(R.id.surgeon);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        operationDate.setText(sdf.format(new Date()));
        operationDateIn.setText(sdf.format(new Date()));
        operationDateOut.setText(sdf.format(new Date()));

        final DateFormat df = new SimpleDateFormat("HH:mm");
        operationTimeIn.setText(df.format(calendar.getTime()));
        operationTimeOut.setText(df.format(calendar.getTime()));

        final DatePickerDialog date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                operationDate.setText(updateLabel());
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        final DatePickerDialog date1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                operationDateIn.setText(updateLabel());
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        final DatePickerDialog date2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                operationDateOut.setText(updateLabel());
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        operationDateIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                Date d = null;
                try {
                    d = sdf.parse(s.toString());
                    operationDateOut.setText(s);
                    date2.getDatePicker().setMinDate(d.getTime());
                    date2.getDatePicker().setMaxDate(d.getTime() + 86400000 * 2);
                } catch (ParseException e) {
                    e.printStackTrace();

                }
                if (timeIn != null && timeOut != null) {
                    if (timeOut.compareTo(timeIn) < 0  && operationDateIn.getText().toString().equals(operationDateOut.getText().toString())) {
                        operationTimeOut.setError("Wrong Time");
                        validTime = false;

                    }
                    else {
                        operationTimeOut.setError(null);
                        validTime = true;
                    }
                }
            }
        });



        operationTimeIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Operation.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        timeIn = formatter.format(selectedHour) + ":" + formatter.format(selectedMinute);
                        operationTimeIn.setText(timeIn);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });

        operationTimeIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (timeOut != null) {
                    if (s.toString().compareTo(timeOut) > 0  && operationDateIn.getText().toString().equals(operationDateOut.getText().toString())) {
                        operationTimeOut.setError("Wrong Time");
                        validTime = false;

                    }
                    else {
                        operationTimeOut.setError(null);
                        validTime = true;
                    }
                }
            }
        });

        operationTimeOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (timeIn != null) {
                    if (s.toString().compareTo(timeIn) < 0  && operationDateIn.getText().toString().equals(operationDateOut.getText().toString())) {
                        operationTimeOut.setError("Wrong Time");
                        validTime = false;

                    }
                    else {
                        operationTimeOut.setError(null);
                        validTime = true;
                    }
                }

            }
        });

        operationTimeOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Operation.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        timeOut = formatter.format(selectedHour) + ":" + formatter.format(selectedMinute);
                        operationTimeOut.setText(timeOut);
                    }
                }, hour, minute, false);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        operationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show();
            }
        });

        operationDateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date1.show();
            }
        });

        operationDateOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date2.show();
            }
        });

        String positions[] = {"Primary Surgeon", "Assistant", "Observer", "Anaesthetist"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Operation.this, R.layout.support_simple_spinner_dropdown_item, positions);
        position.setAdapter(adapter);

        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                surgeon.setVisibility(View.VISIBLE);
                surgeon.setHint(position.getSelectedItem().toString()+" Name*:");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                surgeon.setVisibility(View.GONE);
            }
        });

        surgeon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(surgeon.getText().toString().isEmpty()) {
                        surgeon.setError("This field cannot be empty");
                    }
                    else {
                        surgeon.setError(null);
                    }
                }
            }
        });

        String username = getIntent().getStringExtra("Username");
        surgeon.setText("Dr. "+username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())  {
            case R.id.next:
                if(validTime) {
                    String name = getIntent().getStringExtra("Name");
                    String age = getIntent().getStringExtra("Age");
                    String phospRegNo = getIntent().getStringExtra("Hosp Reg No");
                    String sex = getIntent().getStringExtra("Sex");
                    String sdate = operationDate.getText().toString().trim();
                    String dateIn = operationDateIn.getText().toString().trim();
                    String timeIn = operationTimeIn.getText().toString().trim();
                    String dateOut = operationDateOut.getText().toString().trim();
                    String timeOut = operationTimeOut.getText().toString().trim();
                    String sposition = position.getSelectedItem().toString().trim();

                    Intent intent = new Intent(Operation.this, Procedure.class);
                    intent.putExtra("Name", name);
                    intent.putExtra("Age", age);
                    intent.putExtra("Hosp Reg No", phospRegNo);
                    intent.putExtra("Sex", sex);
                    intent.putExtra("Surgery Date", sdate);
                    intent.putExtra("Date In", dateIn);
                    intent.putExtra("Time In", timeIn);
                    intent.putExtra("Date Out", dateOut);
                    intent.putExtra("Time Out", timeOut);
                    intent.putExtra("Date Out", dateOut);
                    intent.putExtra("Position", sposition);
                    startActivity(intent);
                }
                else {
                    Snackbar.make(findViewById(R.id.operationView),"Enter valid time",Snackbar.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private String updateLabel() {
        String format = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(calendar.getTime());

    }
}
