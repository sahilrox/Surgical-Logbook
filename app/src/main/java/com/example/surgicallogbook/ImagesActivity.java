package com.example.surgicallogbook;



import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private ValueEventListener DBListener;
    private List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.images_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<>();
        progressBar = findViewById(R.id.progress_circle);

        adapter = new ImageAdapter(ImagesActivity.this, uploads);
        adapter.setOnItemClickListener(ImagesActivity.this);
        recyclerView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getUid()).child("Patients").child(getIntent().getStringExtra("Serial No")).child("Uploads");
        firebaseStorage = FirebaseStorage.getInstance();

        DBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren() ) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploads.add(upload);

                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"Normal click at position "+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = uploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selectedItem.getUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(DBListener);
    }
}
