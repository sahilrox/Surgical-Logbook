package com.example.surgicallogbook;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private EditText email, password;
    private TextView forgotPassword;
    private Button login, signup;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private int count = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().setTitle("Login");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            finish();
            startActivity(new Intent(LoginPage.this, HomePage.class));
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        forgotPassword = findViewById(R.id.forgotPass);

        email.setText("");
        password.setText("");

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,PasswordActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginPage.this,"Fill email and password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    validate(email.getText().toString(),password.getText().toString());
                }
            }
        });

        signup = findViewById(R.id.sign_up_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpPage.class));
            }
        });

    }

    private void validate(String email, String pass) {

        progressDialog.setMessage("Verifying User...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else {
                    String errorMsg = showError(task.getException());
                    Toast.makeText(LoginPage.this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.w("Task error",task.getException().toString());
                    progressDialog.dismiss();
                }
            }
        });
    }

    private String showError(Exception exception) {
        String msg = "Login Failed";
        if (exception.getLocalizedMessage().trim().equalsIgnoreCase("There is no user record corresponding to this identifier. The user may have been deleted.")) {
            msg = "Login Failed, Invalid email ID";
        }
        else if(exception.getLocalizedMessage().trim().equalsIgnoreCase("The email address is badly formatted.")) {
            msg = "Email Address is badly formatted";
        }
        else if(exception.getLocalizedMessage().trim().equalsIgnoreCase("The password is invalid or the user does not have a password.")) {
            msg = "Login failed, Invalid password";
        }
        return msg;
    }

    private void checkEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        boolean emailflag = user.isEmailVerified();

        if(emailflag) {
            finish();
            Toast.makeText(LoginPage.this, "Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginPage.this,HomePage.class));
        }
        else {
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

}
