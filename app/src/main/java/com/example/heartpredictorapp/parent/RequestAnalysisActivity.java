package com.example.heartpredictorapp.parent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.example.heartpredictorapp.models.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class RequestAnalysisActivity extends AppCompatActivity {

    ImageView imgBack, imgLogout, imgEditProfile;
    AppCompatButton btnDoctor, btnAI;
    String strDrawID, strFile, strParentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_analysis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        strDrawID = getIntent().getStringExtra("id");
        strFile = getIntent().getStringExtra("file");
        strParentID = getIntent().getStringExtra("parent");

        btnDoctor = findViewById(R.id.btnDoctor);
        btnAI = findViewById(R.id.btnAI);
        imgBack = findViewById(R.id.imgBack);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

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
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewDrawingParentActivity.class);
                startActivity(intent);
            }
        });
        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDoctorsActivity.class);
                intent.putExtra("id", strDrawID);
                intent.putExtra("file", strFile);
                intent.putExtra("parent", strParentID);
                startActivity(intent);
            }
        });
        btnAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestAI("0", strDrawID, strFile, strParentID);
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
    private boolean RequestAI(String doctorID, String drawID, String dFile, String parentID) {
        DatabaseReference databaseDraw = FirebaseDatabase.getInstance().getReference("report");
        databaseDraw.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uploadDate = "";
                        String id = databaseDraw.push().getKey();
                        String diagnose = "-";
                        String recommend = "-";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            LocalDate currentDate = LocalDate.now();
                            uploadDate = String.valueOf(currentDate);
                        }
                        strParentID = LoginActivity.parent.getParentID();
                        Report report = new Report(id, drawID, strParentID, doctorID, dFile, diagnose, recommend, uploadDate);
                        databaseDraw.child(id).setValue(report);
                        //displaying a success toast
                        Toast.makeText(getApplicationContext(), "Success Submit Request", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), RequestAIAnalysisActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("draw", strDrawID);
                        intent.putExtra("file", strFile);
                        intent.putExtra("parent", strParentID);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        return true;
    }
}