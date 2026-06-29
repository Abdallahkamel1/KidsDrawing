package com.example.heartpredictorapp.admin;

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

public class AdminActivity extends AppCompatActivity {

    ImageView imgLogout, imgEditProfile, imgManageDoctors, imgManageParents, imgManageGroups, imgViewReports;
    TextView txtManageDoctors, txtManageParents, txtManageGroups, txtViewReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);
        imgManageDoctors = findViewById(R.id.imgManageDoctors);
        txtManageDoctors = findViewById(R.id.txtManageDoctors);
        imgManageParents = findViewById(R.id.imgManageParents);
        txtManageParents = findViewById(R.id.txtManageParents);
        imgManageGroups = findViewById(R.id.imgManageGroups);
        txtManageGroups = findViewById(R.id.txtManageGroups);
        imgViewReports = findViewById(R.id.imgViewReports);
        txtViewReports = findViewById(R.id.txtViewReports);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, EditProfileAdminActivity.class);
                startActivity(intent);
            }
        });
        imgManageDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageDoctorsActivity.class);
                startActivity(intent);
            }
        });
        txtManageDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageDoctorsActivity.class);
                startActivity(intent);
            }
        });
        imgViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ViewReportsActivity.class);
                startActivity(intent);
            }
        });
        txtViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ViewReportsActivity.class);
                startActivity(intent);
            }
        });
        imgManageParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageParentsActivity.class);
                startActivity(intent);
            }
        });
        txtManageParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageParentsActivity.class);
                startActivity(intent);
            }
        });
        imgManageGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageDiscussionGroupsActivity.class);
                startActivity(intent);
            }
        });
        txtManageGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageDiscussionGroupsActivity.class);
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
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
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