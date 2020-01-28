package com.example.surgicallogbook;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Media extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private String TAG = "0";

    private Button btnChoose, btnUpload;
    private EditText textUpload;
    private ImageView img;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        btnChoose = findViewById(R.id.btn_choose);
        btnUpload = findViewById(R.id.btn_upload);
        textUpload = findViewById(R.id.textUpload);
        img = findViewById(R.id.imgUpload);
        progressBar = findViewById(R.id.progress_upload);

        String serialNo = getIntent().getStringExtra("Serial No");
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(firebaseAuth.getUid()).child("Images").child(serialNo);
        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getUid()).child("Patients").child(serialNo).child("Uploads");


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(Media.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if(imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);

                    Toast.makeText(Media.this, "Upload Successful", Toast.LENGTH_LONG).show();
                    if (imageUri != null)
                    {
                        storageReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                        {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                            {
                                if (!task.isSuccessful())
                                {
                                    throw task.getException();
                                }
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Uri downloadUri = task.getResult();
                                    Log.e(TAG, "then: " + downloadUri.toString());


                                    Upload upload = new Upload(textUpload.getText().toString().trim(),
                                            downloadUri.toString());

                                    databaseReference.push().setValue(upload);
                                } else
                                {
                                    Toast.makeText(Media.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Media.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }
        else {
            Toast.makeText(this, "No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(img);
        }
    }
}
