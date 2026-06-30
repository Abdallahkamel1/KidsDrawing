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

public class NewPasswordActivity extends AppCompatActivity {
    TextInputLayout txtConfirm, txtPass;
    AppCompatButton btnSubmit;
    ProgressBar progress;
    TextView txtLogin;
    String strID, strName, strEmail, strPhone, strPass, strType, strPassedEmail,strState;

    boolean email_found_admin=true, email_found_parent=true, email_found_doctor=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strPassedEmail = getIntent().getStringExtra("email");
        strType = getIntent().getStringExtra("type");

        txtLogin = findViewById(R.id.txtLogin);
        txtConfirm = findViewById(R.id.txtConfirm);
        txtPass = findViewById(R.id.txtPass);
        btnSubmit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progress);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPass.getEditText().getText().toString().trim().isEmpty()) {
                    txtPass.getEditText().setError(getString(R.string.pass_error));
                    txtPass.requestFocus();
                    return;
                }
                if (txtConfirm.getEditText().getText().toString().trim().isEmpty()) {
                    txtConfirm.getEditText().setError(getString(R.string.confirm_error));
                    txtConfirm.getEditText().requestFocus();
                    return;
                }
                if (!txtPass.getEditText().getText().toString().equals(txtConfirm.getEditText().getText().toString())) {
                    txtPass.getEditText().setError(getString(R.string.password_mismatch));
                    txtPass.requestFocus();
                    return;
                }
                if (!(txtPass.getEditText().getText().toString().trim().matches("^[a-zA-Z0-9]*$"))) {
                    txtPass.getEditText().setError(getString(R.string.pass_invalid));
                    txtPass.getEditText().requestFocus();
                    return;
                }
                if (txtPass.getEditText().getText().toString().trim().length() < 6) {
                    txtPass.getEditText().setError(getString(R.string.pass_invalid));
                    txtPass.getEditText().requestFocus();
                    return;
                }
                if (txtPass.getEditText().getText().toString().trim().length() > 20) {
                    txtPass.getEditText().setError(getString(R.string.pass_invalid));
                    txtPass.getEditText().requestFocus();
                } else {
                    strPass = txtPass.getEditText().getText().toString().trim();
                    if(strType.equals("admin")){
                        ResetAdmin();
                    }else if(strType.equals("parent")){
                        ResetParent();
                    }else if(strType.equals("doctor")){
                        ResetDoctor();
                    }
                }
            }
        });
    }
    public void ResetAdmin(){
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("admin");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot snap : snapshot.getChildren()) {
                     Admin usersRecord = snap.getValue(Admin.class);
                     if (usersRecord.getAdminEmail().equals(strPassedEmail)) {
                         strID = usersRecord.getAdminID().toString();
                         strName = usersRecord.getAdminName().toString();
                         strPhone = usersRecord.getAdminPhone().toString();
                         email_found_admin = true;
                     }
                 }
                 if (email_found_admin == true) {
                     //update password
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("admin").child(strID);
                     Admin user = new Admin(strID, strName, strPassedEmail, strPhone, strPass);
                     dR.setValue(user);
                     progress.setVisibility(View.INVISIBLE);
                     //displaying a success toast
                     Toast.makeText(getApplicationContext(), "Success Password Reset", Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                     startActivity(intent);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         }
        );
    }
    public void ResetParent(){
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("parent");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot snap : snapshot.getChildren()) {
                     Parent usersRecord = snap.getValue(Parent.class);
                     if (usersRecord.getParentEmail().equals(strPassedEmail)) {
                         strID = usersRecord.getParentID().toString();
                         strName = usersRecord.getParentName().toString();
                         strPhone = usersRecord.getParentPhone().toString();
                         email_found_parent = true;
                     }
                 }
                 if (email_found_parent == true) {
                     //update password
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("parent").child(strID);
                     Parent user = new Parent(strID, strName, strPassedEmail, strPhone, strPass);
                     dR.setValue(user);
                     progress.setVisibility(View.INVISIBLE);
                     //displaying a success toast
                     Toast.makeText(getApplicationContext(), "Success Password Reset", Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                     startActivity(intent);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         }
        );
    }
    public void ResetDoctor(){
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("doctor");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot snap : snapshot.getChildren()) {
                         Doctor usersRecord = snap.getValue(Doctor.class);
                         if (usersRecord.getDoctorEmail().equals(strPassedEmail)) {
                             strID = usersRecord.getDoctorID().toString();
                             strName = usersRecord.getDoctorName().toString();
                             strPhone = usersRecord.getDoctorPhone().toString();
                             strState = usersRecord.getDoctorState().toString();
                             email_found_doctor = true;
                         }
                     }
                     if (email_found_doctor == true) {
                         //update password
                         DatabaseReference dR = FirebaseDatabase.getInstance().getReference("doctor").child(strID);
                         Doctor user = new Doctor(strID, strName, strPassedEmail, strPhone, strPass, strState);
                         dR.setValue(user);
                         progress.setVisibility(View.INVISIBLE);
                         //displaying a success toast
                         Toast.makeText(getApplicationContext(), "Success Password Reset", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                         startActivity(intent);
                     }
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
        );
    }
}