package com.example.heartpredictorapp.parent;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.admin.EditProfileAdminActivity;
import com.example.heartpredictorapp.models.Child;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddChildActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    TextInputLayout txtPass, txtName, txtAge;
    ImageView imgBack, imgLogout, imgEditProfile;
    AppCompatButton btnAdd;
    ProgressBar progress;
    String strName, strPass, strParentID, strAge, strSex = "female";
    Boolean child_found=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_child);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroup = findViewById(R.id.radioGroup);
        txtPass = findViewById(R.id.txtPass);
        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        btnAdd = findViewById(R.id.btnAdd);
        imgBack = findViewById(R.id.imgBack);
        progress = findViewById(R.id.progress);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedRadioButton = findViewById(i);
                strSex = selectedRadioButton.getText().toString();
            }
        });
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
                Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtName.getEditText().getText().toString().trim().isEmpty()) {
                    txtName.getEditText().setError(getString(R.string.name_error));
                    txtName.requestFocus();
                    return;
                }
                if (txtAge.getEditText().getText().toString().trim().isEmpty()) {
                    txtAge.getEditText().setError(getString(R.string.age_error));
                    txtAge.getEditText().requestFocus();
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
                    strAge = txtAge.getEditText().getText().toString().trim();
                    strPass = txtPass.getEditText().getText().toString().trim();
                    strParentID = LoginActivity.parent.getParentID();
                    AddChild();
                }
            }
        });

    }

    public void AddChild() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseChild = FirebaseDatabase.getInstance().getReference("child");
        databaseChild.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot snap : snapshot.getChildren()) {
                         Child usersRecord = snap.getValue(Child.class);
                         if (usersRecord.getChildName().equals(strName)) {
                             child_found = true;
                         }
                     }
                     if (child_found == true) {
                         Toast.makeText(getApplicationContext(), getString(R.string.name_used), Toast.LENGTH_LONG).show();
                         child_found = false;
                         progress.setVisibility(View.INVISIBLE);
                     } else {
                         String id = databaseChild.push().getKey();
                         Child user = new Child(id, strParentID, strName, strAge, strPass, strSex);
                         databaseChild.child(id).setValue(user);
                         progress.setVisibility(View.INVISIBLE);
                         txtName.getEditText().setText("");
                         txtPass.getEditText().setText("");
                         txtAge.getEditText().setText("");
                         //displaying a success toast
                         Toast.makeText(getApplicationContext(), "New Child added successfully", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getApplicationContext(), ManageKidsActivity.class);
                         startActivityForResult(intent, 100);
                     }
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
        );
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