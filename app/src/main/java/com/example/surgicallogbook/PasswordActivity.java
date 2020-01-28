package com.example.surgicallogbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText resetEmail;
    private Button resetPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getSupportActionBar().setTitle("Forgot Password");

        resetEmail = findViewById(R.id.reset_email);
        resetPass = findViewById(R.id.reset_pass);
        mAuth = FirebaseAuth.getInstance();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmail.getText().toString().trim();
                if(email.isEmpty()) {
                    Toast.makeText(PasswordActivity.this,"Enter your email ID",Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(PasswordActivity.this,"Password Reset email sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this, LoginPage.class));
                            }
                            else {
                                Toast.makeText(PasswordActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
