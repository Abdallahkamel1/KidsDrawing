package com.example.heartpredictorapp.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;

public class DoctorActivity extends AppCompatActivity {
    ImageView imgLogout, imgEditProfile, imgViewDrawings, imgGroups, imgChat, imgManageAppointments;
    TextView txtViewDrawings, txtGroups, txtChat, txtManageAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);
        imgViewDrawings = findViewById(R.id.imgViewDrawings);
        txtViewDrawings = findViewById(R.id.txtViewDrawings);
        imgGroups = findViewById(R.id.imgGroups);
        txtGroups = findViewById(R.id.txtGroups);
        imgChat = findViewById(R.id.imgChat);
        txtChat = findViewById(R.id.txtChat);
        imgManageAppointments = findViewById(R.id.imgManageAppointments);
        txtManageAppointments = findViewById(R.id.txtManageAppointments);

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, EditProfileDoctorActivity.class);
                startActivity(intent);
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        imgViewDrawings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, ViewDrawingsActivity.class);
                startActivity(intent);
            }
        });
        txtViewDrawings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, ViewDrawingsActivity.class);
                startActivity(intent);
            }
        });
        imgGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, DoctorDiscussionGroupActivity.class);
                startActivity(intent);
            }
        });
        txtGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, DoctorDiscussionGroupActivity.class);
                startActivity(intent);
            }
        });
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, ViewAllParentsActivity.class);
                startActivity(intent);
            }
        });
        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, ViewAllParentsActivity.class);
                startActivity(intent);
            }
        });
        imgManageAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, ViewAppointmentsActivity.class);
                startActivity(intent);
            }
        });
        txtManageAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, ViewAppointmentsActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showConfirmationDialog() {
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_logout));
        builder.setMessage(getString(R.string.logout_msg));
        // Positive button (Proceed)
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            // Navigate to the login activity
            Intent intent = new Intent(DoctorActivity.this, LoginActivity.class);
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