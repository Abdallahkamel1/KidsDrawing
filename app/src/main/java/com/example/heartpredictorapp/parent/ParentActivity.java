package com.example.heartpredictorapp.parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;

public class ParentActivity extends AppCompatActivity {

    TextView txtManageChild, txtUploadDrawing, txtDoctors, txtGroups, txtChat, txtManageApp;
    ImageView imgManageChild, imgLogout, imgEditProfile, imgUploadDrawing, imgDoctors, imgGroups, imgChat, imgManageApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtManageApp = findViewById(R.id.txtManageApp);
        imgManageApp = findViewById(R.id.imgManageApp);
        txtGroups = findViewById(R.id.txtGroups);
        imgGroups = findViewById(R.id.imgGroups);
        txtChat = findViewById(R.id.txtChat);
        imgChat = findViewById(R.id.imgChat);
        txtDoctors = findViewById(R.id.txtDoctors);
        imgDoctors = findViewById(R.id.imgDoctors);
        txtManageChild = findViewById(R.id.txtManageChild);
        imgManageChild = findViewById(R.id.imgManageChild);
        txtUploadDrawing = findViewById(R.id.txtUploadDrawing);
        imgUploadDrawing = findViewById(R.id.imgUploadDrawing);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, EditProfileParentActivity.class);
                startActivity(intent);
            }
        });

        txtManageChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ManageKidsActivity.class);
                startActivity(intent);
            }
        });
        imgManageChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ManageKidsActivity.class);
                startActivity(intent);
            }
        });
        txtManageApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ManageAppointmentActivity.class);
                startActivity(intent);
            }
        });
        imgManageApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ManageAppointmentActivity.class);
                startActivity(intent);
            }
        });
        txtGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ParentDiscussionGroupActivity.class);
                startActivity(intent);
            }
        });
        imgGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ParentDiscussionGroupActivity.class);
                startActivity(intent);
            }
        });
        txtDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ParentAllDoctorsActivity.class);
                startActivity(intent);
            }
        });
        imgDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ParentAllDoctorsActivity.class);
                startActivity(intent);
            }
        });
        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ChatDoctorsActivity.class);
                startActivity(intent);
            }
        });
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ChatDoctorsActivity.class);
                startActivity(intent);
            }
        });
        txtUploadDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ViewDrawingParentActivity.class);
                startActivity(intent);
            }
        });
        imgUploadDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentActivity.this, ViewDrawingParentActivity.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
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
            Intent intent = new Intent(ParentActivity.this, LoginActivity.class);
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