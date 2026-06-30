package com.example.heartpredictorapp.doctor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.admin.ManageDiscussionGroupsActivity;
import com.example.heartpredictorapp.models.Child;
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Drawing;
import com.example.heartpredictorapp.models.Report;
import com.example.heartpredictorapp.parent.ManageKidsActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class AddDiagnosisActivity extends AppCompatActivity {

    TextInputLayout txtRecommend, txtDiagnose;
    ImageView imgBack, imgLogout, imgEditProfile;
    AppCompatButton btnAdd;
    ProgressBar progress;
    String strFile, drawID, parentID, reportID, reportDate, strDiagnose, strRecommend;
    String childID, childName, uploadDate, analyzeState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_diagnosis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strFile = getIntent().getStringExtra("file");
        drawID = getIntent().getStringExtra("drawID");
        parentID = getIntent().getStringExtra("parent");
        reportID = getIntent().getStringExtra("reportID");
        reportDate = getIntent().getStringExtra("reportDate");

        txtRecommend = findViewById(R.id.txtRecommend);
        txtDiagnose = findViewById(R.id.txtDiagnose);
        btnAdd = findViewById(R.id.btnAdd);
        imgBack = findViewById(R.id.imgBack);
        progress = findViewById(R.id.progress);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileDoctorActivity.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDiagnose.getEditText().getText().toString().trim().isEmpty()) {
                    txtDiagnose.getEditText().setError(getString(R.string.diagnose_error));
                    txtDiagnose.requestFocus();
                    return;
                }
                if (txtRecommend.getEditText().getText().toString().trim().isEmpty()) {
                    txtRecommend.getEditText().setError(getString(R.string.recommend_error));
                    txtRecommend.getEditText().requestFocus();
                    return;
                }else {
                    strDiagnose = txtDiagnose.getEditText().getText().toString().trim();
                    strRecommend = txtRecommend.getEditText().getText().toString().trim();
                    AddDiagnose();
                    UpdateDrawState();
                }
            }
        });

    }

    public void AddDiagnose() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("report");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     LocalDate currentDate = null;
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                         currentDate = LocalDate.now();
                     }
                     String uploadDate = String.valueOf(currentDate);
                     String doctorID = LoginActivity.doctor.getDoctorID();
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("report").child(reportID);
                     Report report = new Report(reportID, drawID, parentID, doctorID, strFile, strDiagnose, strRecommend, uploadDate);
                     dR.setValue(report);
                     progress.setVisibility(View.INVISIBLE);

                     //displaying a success toast
                     Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
        );
    }
    public void UpdateDrawState() {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("drawing");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot snap : snapshot.getChildren()) {
                         Drawing groupsRecord = snap.getValue(Drawing.class);
                         if (groupsRecord.getDrawID().equals(drawID)) {
                             String firebaseKey = snap.getKey();
                             childID = groupsRecord.getChildID();
                             childName = groupsRecord.getChildName();
                             uploadDate = groupsRecord.getUploadDate();
                             analyzeState = groupsRecord.getAnalyzeState();
                             DatabaseReference updateRef = databaseGroup.child(firebaseKey);
                             Map<String, Object> updates = new HashMap<>();
                             updates.put("analyzeState", "done");  // Replace with your new value
                             updateRef.updateChildren(updates).addOnCompleteListener(task -> {
                                 Intent intent = new Intent(getApplicationContext(), ViewDrawingsActivity.class);
                                 startActivity(intent);
                             });

                             break; // Exit loop after update
                         }
                     }
                 }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Database error: " + error.getMessage());
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