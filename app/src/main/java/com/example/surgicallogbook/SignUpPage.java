package com.example.surgicallogbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SignUpPage extends AppCompatActivity {

    String TAG = "tag";
    private static final int PICK_IMAGE = 123;
    private TextInputEditText name, email, pass, regno, confpass, mobile, state, city, hospital, specify;
    private TextInputLayout emailLayout, mobileLayout, passLayout, confpassLayout;
    private Button signup;
    private ImageView profilePic;
    private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6,checkBox7,checkBox8,checkBox9,checkBox10;
    private Spinner country, designation;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private String sname, semail, spass, sregno, sconfpass, smobile, scountry, sstate, scity, shospital, sdegree, sdesignation, countryCode = "";
    Uri imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!= null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Cannot set profile pic", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        getSupportActionBar().setTitle("Sign Up");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        regno = findViewById(R.id.regno);
        pass = findViewById(R.id.pass);
        confpass = findViewById(R.id.confpass);
        signup = findViewById(R.id.sign_up);
        mobile = findViewById(R.id.mobile);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        hospital = findViewById(R.id.hospital);
        designation = findViewById(R.id.designation);
        specify = findViewById(R.id.specify);
        profilePic = findViewById(R.id.profilePic);

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        checkBox8 = findViewById(R.id.checkBox8);
        checkBox9 = findViewById(R.id.checkBox9);
        checkBox10 = findViewById(R.id.checkBox10);

        emailLayout = findViewById(R.id.email_layout);
        mobileLayout = findViewById(R.id.mobile_layout);
        passLayout = findViewById(R.id.pass_layout);
        confpassLayout = findViewById(R.id.confpass_layout);

        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final StorageReference storageReference = firebaseStorage.getReference();

        String[] designations = {"Consultant","Specialist","Fellow","Resident"};

        ArrayList<String> arrCoutry = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.country_arrays)));
        final ArrayList<String> arrCode = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.country_code)));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, arrCoutry);
        country.setAdapter(dataAdapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,designations);
        designation.setAdapter(adapter1);

        checkBox10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    specify.setVisibility(View.VISIBLE);
                }
                else {
                    specify.setVisibility(View.GONE);
                }
            }
        });

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith(countryCode)) {
                    mobile.setText(countryCode);
                    Selection.setSelection(mobile.getText(),mobile.getText().length());
                }
            }
        });

        mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!isValidMobile(mobile.getText().toString())) {
                        mobileLayout.setError("Enter valid mobile no.");
                    }
                    else {
                        mobileLayout.setError(null);
                    }
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"), PICK_IMAGE);
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!isValidEmail(email.getText().toString())) {
                        emailLayout.setError("Enter a valid email address");
                    }
                    else {
                        emailLayout.setError(null);
                    }
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(pass.getText().toString().length() <=6) {
                        passLayout.setError("Password should be more than 6 characters");
                    }
                    else {
                        passLayout.setError(null);
                    }

                    if (confpass.getText().toString().trim().equals(pass.getText().toString().trim())) {
                        confpassLayout.setError(null);
                    }

                }
            }
        });

        confpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(confpass.getText().toString().trim().equals(pass.getText().toString().trim()))) {
                    confpassLayout.setError("Passwords should match");
                }
                else {
                    confpassLayout.setError(null);
                }
            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode = arrCode.get(country.getSelectedItemPosition());
                mobile.setText(countryCode);
                setEditTextMaxLength(countryCode.length()+10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mobile.setText("");
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {

                    //upload to database
                     sname = name.getText().toString().trim();
                     semail = email.getText().toString().trim();
                     spass = pass.getText().toString().trim();
                     sregno = regno.getText().toString().trim();
                     sconfpass = confpass.getText().toString().trim();
                     scountry = country.getSelectedItem().toString().trim();
                     sstate = state.getText().toString().trim();
                     scity = city.getText().toString().trim();
                     smobile = mobile.getText().toString().trim();
                     shospital = hospital.getText().toString().trim();
                     sdesignation = designation.getSelectedItem().toString().trim();
                     sdegree = "";

                    if (!(checkBox1.isChecked()) && !(checkBox2.isChecked()) && !(checkBox3.isChecked()) && !(checkBox4.isChecked()) && !(checkBox5.isChecked()) && !(checkBox6.isChecked()) && !(checkBox7.isChecked()) && !(checkBox8.isChecked()) && !(checkBox9.isChecked()) && !(checkBox10.isChecked()))
                        sdegree = "None";
                    if(checkBox1.isChecked())
                         sdegree+=checkBox1.getText()+", ";
                    if(checkBox2.isChecked())
                        sdegree+=checkBox2.getText()+", ";
                    if(checkBox3.isChecked())
                        sdegree+=checkBox3.getText()+", ";
                    if(checkBox4.isChecked())
                        sdegree+=checkBox4.getText()+", ";
                    if(checkBox5.isChecked())
                        sdegree+=checkBox5.getText()+", ";
                    if(checkBox6.isChecked())
                        sdegree+=checkBox6.getText()+", ";
                    if(checkBox7.isChecked())
                        sdegree+=checkBox7.getText()+", ";
                    if(checkBox8.isChecked())
                        sdegree+=checkBox8.getText()+", ";
                    if(checkBox9.isChecked())
                        sdegree+=checkBox9.getText()+", ";
                    if(checkBox10.isChecked())
                        sdegree+=specify.getText()+", ";

                    sdegree.trim();
                    if(sdegree != null)
                        sdegree = sdegree.substring(0,sdegree.length()-2);



                        mAuth.createUserWithEmailAndPassword(semail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendEmailVerification();
                                StorageReference imageReference = storageReference.child(mAuth.getUid()).child("Images/Profile Pic");

                                if(imagePath != null) {
                                    UploadTask uploadTask = imageReference.putFile(imagePath);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpPage.this, "Error in uploading profile pic", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(SignUpPage.this, "Profile pic uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            else {
                                Log.w(TAG, "Registration failed",task.getException());
                                Toast.makeText(SignUpPage.this, task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }



    private boolean validateFields() {
        boolean result = false;

        sname = name.getText().toString().trim();
        semail = email.getText().toString().trim();
        spass = pass.getText().toString().trim();
        sregno = regno.getText().toString().trim();
        sconfpass = confpass.getText().toString().trim();
        scountry = country.getSelectedItem().toString().trim();
        sstate = state.getText().toString().trim();
        smobile = mobile.getText().toString().trim();
        shospital = hospital.getText().toString().trim();
        sdesignation = designation.getSelectedItem().toString();

        if(sname.isEmpty() || spass.isEmpty() || sconfpass.isEmpty() || sstate.isEmpty() || scountry.isEmpty() || semail.isEmpty() || smobile.isEmpty()) {
            Toast.makeText(this, "Fill all necessary details",Toast.LENGTH_SHORT).show();
        }
        else if(mobile.getText().toString().trim().length()-countryCode.length()!=10) {
            Toast.makeText(this,"Enter valid mobile no.", Toast.LENGTH_SHORT).show();
        }
        else if(!(mobile.getText().toString().trim().startsWith(countryCode))) {
            Toast.makeText(this, "Mobile No. should start with ISD of selected country", Toast.LENGTH_SHORT).show();
        }
        else if(spass.length()<=6) {
            Toast.makeText(this, "Password should be more than 6 characters",Toast.LENGTH_SHORT).show();
        }
        else if(!(spass.equals(sconfpass))) {
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }
        return result;
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(SignUpPage.this, "Registration Successful",Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpPage.this,"Verification mail sent",Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUpPage.this,LoginPage.class));
                    }
                    else {
                        Toast.makeText(SignUpPage.this, "Error in sending verification mail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(mAuth.getUid());
        UserProfile userProfile = new UserProfile(sname, sdegree, scountry, sstate, scity, sregno, shospital, sdesignation, semail, smobile);
        myRef.setValue(userProfile);
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            if (Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
                String domain = target.substring(target.indexOf('@'),target.indexOf('.'));
                if(!(domain.contains("gmail") || domain.contains("hotmail") || domain.contains("yahoo") || domain.contains("msn") || domain.contains("orange") || domain.contains("live") || domain.contains("rediff"))) {
                    return false;
                }
                else {
                    return true;
                }
            }
                return false;
        }
    }

    private final boolean isValidMobile(String target) {
        if(target == null) {
            return false;
        } else {
            if( target.trim().length() - countryCode.length()!=10  || !(target.trim().startsWith(countryCode))) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    public void setEditTextMaxLength(int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        mobile.setFilters(filterArray);
    }

}