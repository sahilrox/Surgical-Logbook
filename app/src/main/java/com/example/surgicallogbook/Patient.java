package com.example.surgicallogbook;

import android.content.Intent;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Patient extends AppCompatActivity {

    private TextInputEditText patientName, years, months, hospRegNo;
    private RadioButton male, female;
    private TextInputLayout pnamelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        getSupportActionBar().setTitle("Patient");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        patientName = findViewById(R.id.patientName);
        pnamelayout = findViewById(R.id.patientNameLayout);
        years = findViewById(R.id.patientYears);
        months = findViewById(R.id.patientMonths);
        hospRegNo = findViewById(R.id.hospitalRegNo);
        male = findViewById(R.id.buttonMale);
        female = findViewById(R.id.buttonFemale);

        months.setFilters(new InputFilter[]{ new InputFilterMinMax("1","12")});

        patientName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(patientName.getText().toString().isEmpty()) {
                        pnamelayout.setError("This field cannot be empty");
                    }
                    else {
                        pnamelayout.setError(null);
                    }
                }
            }
        });
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

                String name = patientName.getText().toString().trim();
                String age = years.getText().toString().trim() + " years " + months.getText().toString().trim() + " months";
                String phospRegNo = hospRegNo.getText().toString().trim();
                String sex = "";
                String username = getIntent().getStringExtra("Username");
                if (male.isChecked()) {
                    sex = "Male";
                }
                else if(female.isChecked()) {
                    sex = "Female";
                }

                if(name.isEmpty() || years.getText().toString().isEmpty() || months.getText().toString().isEmpty() || (!male.isChecked() && !female.isChecked())) {
                    Snackbar.make(findViewById(R.id.patientView),"Fill all necessary details",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Patient.this,Operation.class);
                    intent.putExtra("Name",name);
                    intent.putExtra("Age",age);
                    intent.putExtra("Hosp Reg No",phospRegNo);
                    intent.putExtra("Sex",sex);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

class InputFilterMinMax implements InputFilter {

    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }


    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString()+source.toString());
            if (isInRange(min,max,input))
                return null;
        }catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
