package com.example.surgicallogbook;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Procedure extends AppCompatActivity {

    private Spinner procedure, anaesthesia;
    private TextInputEditText procedureName, diagnosis, site, anaesthesiaOther,  prosthesisDetails, complicationsDetails , tissueDetails, tubesDetails, deepVeinDetails, postDiagnosis, position, antibiotic, incision, approach, intra, additional, closure;
    private EditText blood;
    private TextInputLayout procedureNameLayout, diagnosisLayout, prosthesisDetailsLayout, complicationsDetailsLayout,  anaesthesiaOtherLayout, tissueDetailsLayout, tubesDetailsLayout, deepVeinDetailsLayout;
    private RadioGroup side, prosthesis, complications, tissue, tubes, deepVein;
    private RadioButton left, right, prosYes, compYes, tissueYes, tubesYes, veinYes;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private long patients = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);
        getSupportActionBar().setTitle("Procedure");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        procedure = findViewById(R.id.procedure);
        anaesthesia = findViewById(R.id.anaesthesia);
        procedureName = findViewById(R.id.procedureName);
        site = findViewById(R.id.site);
        side = findViewById(R.id.sideradio);
        left = findViewById(R.id.buttonLeft);
        right = findViewById(R.id.buttonRight);
        anaesthesiaOther = findViewById(R.id.anaesthesiaOther);
        diagnosis = findViewById(R.id.preDiagnosis);
        postDiagnosis = findViewById(R.id.postDiagnosis);
        position = findViewById(R.id.position1);
        antibiotic = findViewById(R.id.antiPro);
        incision = findViewById(R.id.incision);
        approach = findViewById(R.id.approach);
        intra = findViewById(R.id.findings);
        additional = findViewById(R.id.additional);
        closure = findViewById(R.id.details);
        blood = findViewById(R.id.blood);
        procedureNameLayout = findViewById(R.id.procedureNameLayout);
        diagnosisLayout = findViewById(R.id.preDiagnosisLayout);
        prosthesis = findViewById(R.id.prosthesis);
        complications = findViewById(R.id.complications);
        tissue = findViewById(R.id.tissue);
        tubes = findViewById(R.id.tubes);
        deepVein = findViewById(R.id.deepVein);
        prosYes = findViewById(R.id.prosYes);
        compYes = findViewById(R.id.compYes);
        tissueYes = findViewById(R.id.tissueYes);
        tubesYes = findViewById(R.id.tubesYes);
        veinYes = findViewById(R.id.veinYes);
        tubesDetails = findViewById(R.id.tubesDetails);
        tissueDetails = findViewById(R.id.tissueDetails);
        deepVeinDetails = findViewById(R.id.deepVeinDetails);
        anaesthesiaOtherLayout = findViewById(R.id.anaesthesiaOtherLayout);
        prosthesisDetails = findViewById(R.id.prosthesisDetails);
        complicationsDetails = findViewById(R.id.complicationsDetails);
        prosthesisDetailsLayout = findViewById(R.id.prosthesisDetailsLayout);
        complicationsDetailsLayout = findViewById(R.id.complicationsDetailsLayout);
        tissueDetailsLayout = findViewById(R.id.tissueDetailsLayout);
        tubesDetailsLayout = findViewById(R.id.tubesDetailsLayout);
        deepVeinDetailsLayout = findViewById(R.id.deepVeinDetailsLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getUid()).child("Patients");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    patients = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String procedures[] = {"Elective", "Emergency"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, procedures);
        procedure.setAdapter(adapter);

        String anaesthesias[] = {"General", "Spinal", "Local", "Epidural", "Other"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, anaesthesias);
        anaesthesia.setAdapter(adapter1);


        anaesthesia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 4) {
                    anaesthesiaOtherLayout.setVisibility(View.VISIBLE);
                }
                else {
                    anaesthesiaOtherLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                anaesthesiaOtherLayout.setVisibility(View.GONE);
            }
        });

        procedureName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(procedureName.getText().toString().isEmpty()) {
                        procedureNameLayout.setError("This field cannot be empty");
                    }
                    else {
                        procedureNameLayout.setError(null);
                    }
                }
            }
        });

        diagnosis.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(diagnosis.getText().toString().isEmpty()) {
                        diagnosisLayout.setError("This field cannot be empty");
                    }
                    else {
                        diagnosisLayout.setError(null);
                    }
                }
            }
        });

        prosthesis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.prosYes) {
                    prosthesisDetailsLayout.setVisibility(View.VISIBLE);
                }
                else {
                    prosthesisDetailsLayout.setVisibility(View.GONE);
                }
            }
        });


        complications.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.compYes) {
                    complicationsDetailsLayout.setVisibility(View.VISIBLE);
                }
                else {
                    complicationsDetailsLayout.setVisibility(View.GONE);
                }
            }
        });

        tissue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.tissueYes) {
                    tissueDetailsLayout.setVisibility(View.VISIBLE);
                }
                else {
                    tissueDetailsLayout.setVisibility(View.GONE);
                }
            }
        });

        tubes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.tubesYes) {
                    tubesDetailsLayout.setVisibility(View.VISIBLE);
                }
                else {
                    tubesDetailsLayout.setVisibility(View.GONE);
                }
            }
        });

        deepVein.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.veinYes) {
                    deepVeinDetailsLayout.setVisibility(View.VISIBLE);
                }
                else {
                    deepVeinDetailsLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())  {
            case R.id.save:
                String name = getIntent().getStringExtra("Name");
                String age = getIntent().getStringExtra("Age");
                String phospRegNo = getIntent().getStringExtra("Hosp Reg No");
                String sex = getIntent().getStringExtra("Sex");
                String sdate = getIntent().getStringExtra("Surgery Date");
                String dateIn = getIntent().getStringExtra("Date In");
                String timeIn = getIntent().getStringExtra("Time In");
                String dateOut = getIntent().getStringExtra("Date Out");
                String timeOut = getIntent().getStringExtra("Time Out");
                String sposition = getIntent().getStringExtra("Position");

                String sprocedure = procedure.getSelectedItem().toString().trim();
                String sprocedureName = procedureName.getText().toString().trim();
                String ssite = site.getText().toString().trim();
                String sside = "";
                if(left.isChecked())
                    sside = "Left";
                else if(right.isChecked())
                    sside = "Right";

                String sanaesthesia = anaesthesia.getSelectedItem().toString().trim();
                if(sanaesthesia.equals("Other"))
                    sanaesthesia = anaesthesiaOther.getText().toString().trim();

                String sdiagnosis = diagnosis.getText().toString().trim();
                String spostDiagnosis = postDiagnosis.getText().toString().trim();
                String sposition1 = position.getText().toString().trim();
                String santibiotic = antibiotic.getText().toString().trim();
                String sincision = incision.getText().toString().trim();
                String sapproach = approach.getText().toString().trim();
                String sintra = intra.getText().toString().trim();
                String sadditional = additional.getText().toString().trim();
                String sclosure = closure.getText().toString().trim();
                String sblood = blood.getText().toString().trim();
                String sprosthesis = "", scomplications = "", stissue = "", stubes = "", svein = "";

                if(prosYes.isChecked())
                    sprosthesis = prosthesisDetails.getText().toString().trim();
                else
                    sprosthesis = "No";

                if(compYes.isChecked())
                    scomplications = complicationsDetails.getText().toString().trim();
                else
                    scomplications = "No";

                if(tissueYes.isChecked())
                    stissue = tissueDetails.getText().toString().trim();
                else
                    stissue = "No";

                if(tubesYes.isChecked())
                    stubes = tubesDetails.getText().toString().trim();
                else
                    stubes = "No";

                if(veinYes.isChecked())
                    svein = deepVeinDetails.getText().toString().trim();
                else
                    svein = "No";

                if(sprocedureName.isEmpty() || sanaesthesia.isEmpty() || sdiagnosis.isEmpty()) {
                    Snackbar.make(findViewById(R.id.procedureView),"Fill all necessary details", Snackbar.LENGTH_SHORT).show();
                }
                else {

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Saving Details..");
                    progressDialog.show();

                    DatabaseReference serial = myRef.child(String.valueOf(patients+1));
                    DatabaseReference patientRef = serial.child("Patient");
                    DatabaseReference operationRef = serial.child("Operation");
                    DatabaseReference procedureRef = serial.child("Procedure");

                    PatientProfile patientProfile = new PatientProfile(name,age,sex,phospRegNo);
                    OperationProfile operationProfile = new OperationProfile(sdate,dateIn,timeIn,dateOut,timeOut,sposition);
                    ProcedureProfile procedureProfile = new ProcedureProfile(sprocedure, sprocedureName, ssite, sside, sanaesthesia, sdiagnosis, spostDiagnosis, sposition1, santibiotic, sincision, sapproach, sintra, sadditional, sclosure, sblood, sprosthesis, scomplications, stissue, stubes, svein);

                    patientRef.setValue(patientProfile);
                    operationRef.setValue(operationProfile);
                    procedureRef.setValue(procedureProfile);

                    progressDialog.dismiss();

                    Toast.makeText(this, "Details saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Procedure.this, HomePage.class));
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
