package com.example.surgicallogbook;

import android.app.ProgressDialog;
import android.net.Uri;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 123;
    private ImageView profilePic;
    private TextView profileName, profileEmail, profileRegNo, profileMobile, profileHosp, profileCity, profileState, profileCountry, logsTotal;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    Uri imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.textViewName);
        profileEmail = findViewById(R.id.textViewEmail);
        profileRegNo = findViewById(R.id.textViewRegNo);
        profileMobile = findViewById(R.id.textViewMobile);
        profileHosp = findViewById(R.id.textViewHospital);
        profileCity = findViewById(R.id.textViewCity);
        profileState = findViewById(R.id.textViewState);
        profileCountry = findViewById(R.id.textViewCountry);
        profilePic = findViewById(R.id.imgProfilePic);
        logsTotal = findViewById(R.id.logsTotal);

        logsTotal.setText(getIntent().getStringExtra("Logs"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Profile...");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic");
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilePic);
            }
        });


        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                profileName.setText(userProfile.getName());
                profileEmail.setText(userProfile.getEmail());
                profileRegNo.setText(userProfile.getRegNo());
                profileMobile.setText(userProfile.getMobile());
                profileHosp.setText(userProfile.getHospital());
                profileCity.setText(userProfile.getCity());
                profileState.setText(userProfile.getState());
                profileCountry.setText(userProfile.getCountry());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
