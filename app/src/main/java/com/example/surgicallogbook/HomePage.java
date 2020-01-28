package com.example.surgicallogbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference, patientsReference;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private TextView profName, profEmail, nologs;
    private EditText searchView;
    private ImageView profPic;
    RecyclerViewAdapter adapter;
    Context context;
    private ArrayList<Logs> logsList;
    private int logsTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ProfileActivity.class));
            }
        });
        profName = headerView.findViewById(R.id.nav_profileName);
        profEmail = headerView.findViewById(R.id.nav_profileEmail);
        profPic = headerView.findViewById(R.id.nav_profilePic);

        nologs = findViewById(R.id.nologs);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //searchView = findViewById(R.id.searchText);



        logsList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));

        databaseReference = firebaseDatabase.getReference(Objects.requireNonNull(mAuth.getUid()));
        patientsReference = databaseReference.child("Patients");
        patientsReference.keepSynced(true);
        patientsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logsList.clear();
                if(dataSnapshot.getChildrenCount() == 0) {
                    nologs.setVisibility(View.VISIBLE);
                }
                else {
                    nologs.setVisibility(View.GONE);
                }
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PatientProfile p = dataSnapshot1.child("Patient").getValue(PatientProfile.class);
                    OperationProfile o = dataSnapshot1.child("Operation").getValue(OperationProfile.class);
                    ProcedureProfile pr = dataSnapshot1.child("Procedure").getValue(ProcedureProfile.class);
                    if(p!= null && o!= null && pr!= null) {
                        Logs l = new Logs(o.getSurgeryDate(), "Name: " + p.getName(), "Age: " + p.getAge(), "Procedure: " + pr.getProcedureName());
                        logsList.add(l);
                    }
                }


                adapter =  new RecyclerViewAdapter(HomePage.this, logsList);
                adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(HomePage.this,SurgicalProcedure.class);
                        intent.putExtra("Number",String.valueOf(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(int position) {

                    }
                });
                recyclerView.setAdapter(adapter);
                logsTotal = logsList.size();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePage.this,"Error in displaying logs",Toast.LENGTH_SHORT).show();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                profName.setText(userProfile.getName());
                profEmail.setText(userProfile.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePage.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(mAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profPic);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.addLog:
                Intent intent = new Intent(HomePage.this,Patient.class);
                intent.putExtra("Username",profName.getText().toString());
                startActivity(intent);
                break;
        }
        return true;
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(HomePage.this, "Logged Out",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(HomePage.this, LoginPage.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.nav_profile) {
            Intent intent = new Intent(HomePage.this, ProfileActivity.class);
            intent.putExtra("Logs",Integer.toString(logsTotal));
            startActivity(intent);
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void onStart() {
        super.onStart();

    }
}
