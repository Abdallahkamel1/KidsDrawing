package com.example.heartpredictorapp.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.adapters.GroupAdapter;
import com.example.heartpredictorapp.adapters.ManageAppointmentAdapter;
import com.example.heartpredictorapp.admin.AddGroupActivity;
import com.example.heartpredictorapp.admin.AdminActivity;
import com.example.heartpredictorapp.admin.EditProfileAdminActivity;
import com.example.heartpredictorapp.models.Appointment;
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ManageAppointmentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Appointment> appointArrayList;
    ManageAppointmentAdapter appointAdapter;
    ImageView imgBack, imgLogout, imgEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        imgBack = findViewById(R.id.imgBack);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appointAdapter);
        recyclerView.setHasFixedSize(true);

        appointArrayList = new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ParentActivity.class);
                startActivity(i);
            }
        });
        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileParentActivity.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        clearAll();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("appointment");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for(DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    Date currentDate = new Date();
                    Appointment appoint = dataSnapshot.getValue(Appointment.class);
                    try {
                        Date appointmentDate = dateFormat.parse(appoint.getAppDate());
                        if (appointmentDate != null && appointmentDate.after(currentDate)) {
                            appointArrayList.add(appoint); // Only add upcoming appointments
                        }
                    } catch (ParseException e) {
                        e.printStackTrace(); // Handle incorrect date format
                    }
                }
                appointAdapter = new ManageAppointmentAdapter(getApplicationContext(), appointArrayList);
                recyclerView.setAdapter(appointAdapter);
                appointAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void clearAll(){
        if(appointArrayList != null){
            appointArrayList.clear();
        }
        if(appointAdapter != null){
            appointAdapter.notifyDataSetChanged();
        }
    }
    private void showConfirmationDialog() {
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_logout));
        builder.setMessage(getString(R.string.logout_msg));
        // Positive button (Proceed)
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            // Navigate to the login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
        // Negative button (Cancel)
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
            // Dismiss the dialog
            dialog.dismiss();
        });
        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}