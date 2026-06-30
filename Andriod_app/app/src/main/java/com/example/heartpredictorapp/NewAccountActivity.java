package com.example.heartpredictorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Parent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewAccountActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    TextInputLayout txtPhone, txtPass, txtName, txtEmail;
    ImageView imgBack;
    AppCompatButton btnRegister;
    ProgressBar progress;
    String strID, strName, strEmail, strPhone, strPass, strType = "Parent";

    boolean email_found_admin=true, email_found_parent=true, email_found_doctor=true;
    CheckerAdmin emailCheckerAdmin;
    CheckerParent emailCheckerParent;
    CheckerDoctor emailCheckerDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroup = findViewById(R.id.radioGroup);
        txtPhone = findViewById(R.id.txtPhone);
        txtPass = findViewById(R.id.txtPass);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        btnRegister = findViewById(R.id.btnRegister);
        imgBack = findViewById(R.id.imgBack);
        progress = findViewById(R.id.progress);

        emailCheckerAdmin = new CheckerAdmin();
        emailCheckerParent = new CheckerParent();
        emailCheckerDoctor = new CheckerDoctor();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedRadioButton = findViewById(i);
                strType = selectedRadioButton.getText().toString();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtName.getEditText().getText().toString().trim().isEmpty()) {
                    txtName.getEditText().setError(getString(R.string.name_error));
                    txtName.requestFocus();
                    return;
                }
                if (txtEmail.getEditText().getText().toString().trim().isEmpty()) {
                    txtEmail.getEditText().setError(getString(R.string.email_error));
                    txtEmail.getEditText().requestFocus();
                    return;
                }
                if (txtEmail.getEditText().getText().toString().indexOf("@") == -1) {
                    txtEmail.getEditText().setError(getString(R.string.email_invalid));
                    txtEmail.requestFocus();
                    return;
                }
                if (txtPhone.getEditText().getText().toString().trim().isEmpty()) {
                    txtPhone.getEditText().setError(getString(R.string.phone_error));
                    txtPhone.getEditText().requestFocus();
                    return;
                }
                if (txtPass.getEditText().getText().toString().trim().isEmpty()) {
                    txtPass.getEditText().setError(getString(R.string.pass_error));
                    txtPass.getEditText().requestFocus();
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
                    strName = txtName.getEditText().getText().toString().trim();
                    strEmail = txtEmail.getEditText().getText().toString().trim();
                    strPass = txtPass.getEditText().getText().toString().trim();
                    strPhone = txtPhone.getEditText().getText().toString().trim();
                    //email_found_admin = true;
                    //email_found_parent = true;
                    //email_found_doctor = true;
                    CheckAdmin();
                    CheckParent();
                    CheckDoctor();
                    if(!email_found_admin && !email_found_parent && !email_found_doctor){
                        if(strType.equals("Parent")){
                            AddParent();
                        }else{
                            AddDoctor();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Check for Email", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public void CheckAdmin(){
        emailCheckerAdmin.isEmailUnique(strEmail, isUnique -> {
            if (isUnique) {
                email_found_admin = false;
            } else {
                email_found_admin = true;
            }
        });
    }
    public void CheckParent(){
        emailCheckerParent.isEmailUnique(strEmail, isUnique -> {
            if (isUnique) {
                email_found_parent = false;
            } else {
                email_found_parent = true;
            }
        });
    }
    public void CheckDoctor(){
        emailCheckerDoctor.isEmailUnique(strEmail, isUnique -> {
            if (isUnique) {
                email_found_doctor = false;
            } else {
                email_found_doctor = true;
            }
        });
    }

public static class CheckerAdmin {

    public void isEmailUnique(String email, EmailCheckCallback callback) {
        // Reference to the Users node in the database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("admin");
        databaseRef.orderByChild("adminEmail").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email already exists
                            callback.onResult(false);
                        } else {
                            // Email is unique
                            callback.onResult(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                        System.err.println("Database error: " + databaseError.getMessage());
                        callback.onResult(false);
                    }
                });
    }
    // Callback interface for the result
     interface EmailCheckCallback {
        void onResult(boolean isUnique);
    }
}
    public static class CheckerParent {

        public void isEmailUnique(String email, EmailCheckCallback callback) {
            // Reference to the Users node in the database
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("parent");

            // Query to check if the email exists
            databaseRef.orderByChild("parentEmail").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email already exists
                                callback.onResult(false);
                            } else {
                                // Email is unique
                                callback.onResult(true);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors
                            System.err.println("Database error: " + databaseError.getMessage());
                            callback.onResult(false);
                        }
                    });
        }
        // Callback interface for the result
        interface EmailCheckCallback {
            void onResult(boolean isUnique);
        }
    }

    public static class CheckerDoctor {

        public void isEmailUnique(String email, EmailCheckCallback callback) {
            // Reference to the Users node in the database
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("doctor");
            // Query to check if the email exists
            databaseRef.orderByChild("doctorEmail").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email already exists
                                callback.onResult(false);
                            } else {
                                // Email is unique
                                callback.onResult(true);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors
                            System.err.println("Database error: " + databaseError.getMessage());
                            callback.onResult(false);
                        }
                    });
        }
        // Callback interface for the result
        interface EmailCheckCallback {
            void onResult(boolean isUnique);
        }
    }
    public void AddParent() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseParent = FirebaseDatabase.getInstance().getReference("parent");
        databaseParent.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(!email_found_admin && !email_found_parent && !email_found_doctor){
                       String id = databaseParent.push().getKey();
                       Parent user = new Parent(id, strName, strEmail, strPhone, strPass);
                       databaseParent.child(id).setValue(user);
                       progress.setVisibility(View.INVISIBLE);
                       txtName.getEditText().setText("");
                       txtEmail.getEditText().setText("");
                       txtPhone.getEditText().setText("");
                       txtPass.getEditText().setText("");
                       //displaying a success toast
                       Toast.makeText(getApplicationContext(), "New Parent added successfully", Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                       startActivityForResult(intent, 100);
                   }
               }
              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
        }
        );
    }
    public void AddDoctor() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseDoctor = FirebaseDatabase.getInstance().getReference("doctor");
        databaseDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (!email_found_admin && !email_found_parent && !email_found_doctor) {

                          String id = databaseDoctor.push().getKey();
                          String state = "disable";
                          Doctor user = new Doctor(id, strName, strEmail, strPhone, strPass, state);
                          databaseDoctor.child(id).setValue(user);
                          progress.setVisibility(View.INVISIBLE);
                          txtName.getEditText().setText("");
                          txtEmail.getEditText().setText("");
                          txtPhone.getEditText().setText("");
                          txtPass.getEditText().setText("");
                          //displaying a success toast
                          Toast.makeText(getApplicationContext(), "New Doctor added successfully", Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                          startActivityForResult(intent, 100);
                      }
                  }
                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  }
        );
    }

}
