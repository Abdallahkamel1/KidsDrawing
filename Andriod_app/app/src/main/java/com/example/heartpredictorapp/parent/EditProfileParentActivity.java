package com.example.heartpredictorapp.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.models.Parent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileParentActivity extends AppCompatActivity {

    TextInputLayout txtPhone, txtPass, txtName;
    ImageView imgBack;
    AppCompatButton btnSave;
    ProgressBar progress;
    String strID, strName, strEmail, strPhone, strPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile_parent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtPhone = findViewById(R.id.txtPhone);
        txtName = findViewById(R.id.txtName);
        txtPass = findViewById(R.id.txtPass);
        btnSave = findViewById(R.id.btnSave);
        progress = findViewById(R.id.progress);
        imgBack = findViewById(R.id.imgBack);

        txtPhone.getEditText().setText(LoginActivity.parent.getParentPhone());
        txtName.getEditText().setText(LoginActivity.parent.getParentName());
        txtPass.getEditText().setText(LoginActivity.parent.getParentPass());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPass.getEditText().getText().toString().trim().isEmpty()) {
                    txtPass.getEditText().setError(getString(R.string.pass_error));
                    txtPass.requestFocus();
                    return;
                }
                if (txtName.getEditText().getText().toString().trim().isEmpty()) {
                    txtName.getEditText().setError(getString(R.string.name_error));
                    txtName.getEditText().requestFocus();
                    return;
                }
                if (txtPhone.getEditText().getText().toString().trim().isEmpty()) {
                    txtPhone.getEditText().setError(getString(R.string.phone_error));
                    txtPhone.getEditText().requestFocus();
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
                    strName = txtName.getEditText().getText().toString().trim();
                    strPhone = txtPhone.getEditText().getText().toString().trim();
                    EditUser();
                }
            }
        });
    }
    public void EditUser(){
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("parent");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     //update password
                     strID = LoginActivity.parent.getParentID();
                     strEmail = LoginActivity.parent.getParentEmail();
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("parent").child(strID);
                     Parent user = new Parent(strID, strName, strEmail, strPhone, strPass);
                     dR.setValue(user);
                     progress.setVisibility(View.INVISIBLE);
                     LoginActivity.parent.setParentName(strName);
                     LoginActivity.parent.setParentPhone(strPhone);
                     LoginActivity.parent.setParentPass(strPass);
                     //displaying a success toast
                     Toast.makeText(getApplicationContext(), "Success Edit Profile", Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                     startActivity(intent);
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
        );
    }
}