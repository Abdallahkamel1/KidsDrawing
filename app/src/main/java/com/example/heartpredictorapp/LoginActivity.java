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

import com.example.heartpredictorapp.admin.AdminActivity;
import com.example.heartpredictorapp.child.ChildActivity;
import com.example.heartpredictorapp.doctor.DoctorActivity;
import com.example.heartpredictorapp.models.Admin;
import com.example.heartpredictorapp.models.Child;
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Parent;
import com.example.heartpredictorapp.parent.ParentActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView txtForget, txtSign;
    TextInputLayout txtAccount, txtPassword;
    AppCompatButton btnLogin;
    ProgressBar progress;
    String strEmail, strPass;
    //boolean parent_found, admin_found, doctor_found, child_found;
    public static Admin admin;
    public static Parent parent;
    public static Doctor doctor;
    public static Child child;

    private boolean userFound = false;
    private int completedChecks = 0;
    int exist = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtForget = findViewById(R.id.txtForget);
        txtSign = findViewById(R.id.txtSign);
        txtAccount = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPass);
        txtForget = findViewById(R.id.txtForget);
        btnLogin = findViewById(R.id.btnLogin);
        progress = findViewById(R.id.progress);

        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(i);
            }
        });
        txtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtAccount.getEditText().getText().toString().trim().isEmpty()) {
                    txtAccount.getEditText().setError(getString(R.string.user_error));
                    txtAccount.getEditText().requestFocus();
                    return;
                }
                if (txtPassword.getEditText().getText().toString().trim().isEmpty()) {
                    txtPassword.getEditText().setError(getString(R.string.pass_error));
                    txtPassword.getEditText().requestFocus();
                    return;
                }
                strEmail = txtAccount.getEditText().getText().toString().trim();
                strPass = txtPassword.getEditText().getText().toString().trim();
                LoginUser();
            }
        });
    }

    public void LoginUser() {
        progress.setVisibility(View.VISIBLE);
        checkAdmin();
        checkParent();
        checkDoctor();
        checkChild();
    }

    private void checkAdmin() {
        DatabaseReference databaseAdmin = FirebaseDatabase.getInstance().getReference("admin");
        databaseAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Admin user = snap.getValue(Admin.class);
                    if (user.getAdminEmail().equals(strEmail) && user.getAdminPass().equals(strPass)) {
                        userFound = true;
                        progress.setVisibility(View.INVISIBLE);
                        admin = new Admin(user.getAdminID(), user.getAdminName(), user.getAdminEmail(),
                                user.getAdminPhone(), user.getAdminPass());
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                finishCheck();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finishCheck();
            }
        });
    }

    private void checkParent() {
        DatabaseReference databaseParent = FirebaseDatabase.getInstance().getReference("parent");
        databaseParent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Parent user = snap.getValue(Parent.class);
                    if (user.getParentEmail().equals(strEmail) && user.getParentPass().equals(strPass)) {
                        userFound = true;
                        progress.setVisibility(View.INVISIBLE);
                        parent = new Parent(user.getParentID(), user.getParentName(), user.getParentEmail(),
                                user.getParentPhone(), user.getParentPass());
                        Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                finishCheck();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finishCheck();
            }
        });
    }

    private void checkDoctor() {
        DatabaseReference databaseDoctor = FirebaseDatabase.getInstance().getReference("doctor");
        databaseDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Doctor user = snap.getValue(Doctor.class);
                    if (user.getDoctorEmail().equals(strEmail) && user.getDoctorPass().equals(strPass)) {
                        userFound = true;
                        progress.setVisibility(View.INVISIBLE);
                        doctor = new Doctor(user.getDoctorID(), user.getDoctorName(), user.getDoctorEmail(),
                                user.getDoctorPhone(), user.getDoctorPass(), user.getDoctorState());
                        Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                finishCheck();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finishCheck();
            }
        });
    }

    private void checkChild() {
        DatabaseReference databaseChild = FirebaseDatabase.getInstance().getReference("child");
        databaseChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Child user = snap.getValue(Child.class);
                    if (user.getChildName().equals(strEmail) && user.getChildPass().equals(strPass)) {
                        userFound = true;
                        progress.setVisibility(View.INVISIBLE);
                        child = new Child(user.getChildID(), user.getParentID(), user.getChildName(),
                                user.getChildAge(), user.getChildPass(), user.getChildSex());
                        Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                finishCheck();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finishCheck();
            }
        });
    }

    // This method is called after each Firebase check completes
    private void finishCheck() {
        completedChecks++;
        if (completedChecks == 4 && !userFound) {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}