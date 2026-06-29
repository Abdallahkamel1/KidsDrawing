package com.example.heartpredictorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.models.Admin;
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Parent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassActivity extends AppCompatActivity {

    TextView txtLogin;
    TextInputLayout txtEmail, txtPhone;
    AppCompatButton btnSubmit;
    ProgressBar progress;
    String strEmail, strPhone;
    boolean parent_found, admin_found, doctor_found;
    int exist = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtLogin = findViewById(R.id.txtLogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnSubmit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progress);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtEmail.getEditText().getText().toString().trim().isEmpty()) {
                    txtEmail.getEditText().setError(getString(R.string.email_error));
                    txtEmail.getEditText().requestFocus();
                    return;
                }
                if (txtPhone.getEditText().getText().toString().trim().isEmpty()) {
                    txtPhone.getEditText().setError(getString(R.string.phone_error));
                    txtPhone.getEditText().requestFocus();
                    return;
                }
                strEmail = txtEmail.getEditText().getText().toString().trim();
                strPhone = txtPhone.getEditText().getText().toString().trim();
                CheckUser();
            }
        });
    }

    public void CheckUser() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseAdmin = FirebaseDatabase.getInstance().getReference("admin");
        databaseAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot snap : snapshot.getChildren()) {
                         Admin userRecord = snap.getValue(Admin.class);
                         if (userRecord.getAdminEmail().contains(strEmail) && userRecord.getAdminPhone().contains(strPhone)) {
                             Intent intent = new Intent(getApplicationContext(), NewPasswordActivity.class);
                             intent.putExtra("email", strEmail);
                             intent.putExtra("type", "admin");
                             startActivity(intent);
                             admin_found = true;
                         }
                     }
                     if (admin_found == true) {
                         progress.setVisibility(View.INVISIBLE);
                         exist = 1;
                     } else {
                         admin_found = false;
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {
                 }
             }
        );
        if (admin_found == false) {
            DatabaseReference databaseParent = FirebaseDatabase.getInstance().getReference("parent");
            databaseParent.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          for (DataSnapshot snap : snapshot.getChildren()) {
                              Parent userRecord = snap.getValue(Parent.class);
                              if (userRecord.getParentEmail().contains(strEmail) && userRecord.getParentPhone().contains(strPhone)) {
                                  Intent intent = new Intent(getApplicationContext(), NewPasswordActivity.class);
                                  intent.putExtra("email", strEmail);
                                  intent.putExtra("type", "parent");
                                  startActivity(intent);
                                  parent_found = true;
                              }
                          }
                          if (parent_found == true) {
                              progress.setVisibility(View.INVISIBLE);
                              exist = 1;
                          } else {
                              parent_found = false;
                              exist = 0;
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                      }
                  }
            );
            if (parent_found == false && admin_found == false) {
                DatabaseReference databaseDoctor = FirebaseDatabase.getInstance().getReference("doctor");
                databaseDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              for (DataSnapshot snap : snapshot.getChildren()) {
                                  Doctor userRecord = snap.getValue(Doctor.class);
                                  if (userRecord.getDoctorEmail().contains(strEmail) && userRecord.getDoctorPass().contains(strPhone)){
                                      Intent intent = new Intent(getApplicationContext(), NewPasswordActivity.class);
                                      intent.putExtra("email", strEmail);
                                      intent.putExtra("type", "doctor");
                                      startActivity(intent);
                                      doctor_found = true;
                                  }
                              }
                              if (doctor_found == true) {
                                  progress.setVisibility(View.INVISIBLE);
                                  exist = 1;
                              } else {
                                  doctor_found = false;
                                  exist = 0;
                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {
                          }
                      }
                );
                    if (admin_found == false && parent_found == false && doctor_found == false) {
                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
}