package com.example.heartpredictorapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.heartpredictorapp.models.Admin;
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateGroupActivity extends AppCompatActivity {
    TextInputLayout txtDesc, txtTitle;
    Spinner spinState;
    ImageView imgBack, imgLogout, imgEditProfile;
    AppCompatButton btnAdd;
    ProgressBar progress;
    String strTitle, strDesc, strID, strState;
    Boolean group_found =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strID = getIntent().getStringExtra("id");
        strTitle = getIntent().getStringExtra("title");
        strDesc = getIntent().getStringExtra("desc");
        strState = getIntent().getStringExtra("state");

        txtDesc = findViewById(R.id.txtDesc);
        txtTitle = findViewById(R.id.txtTitle);
        btnAdd = findViewById(R.id.btnAdd);
        imgBack = findViewById(R.id.imgBack);
        progress = findViewById(R.id.progress);
        imgLogout = findViewById(R.id.imgLogout);
        spinState = findViewById(R.id.spinState);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        txtTitle.getEditText().setText(strTitle);
        txtDesc.getEditText().setText(strDesc);
        for (int j = 0; j < spinState.getCount(); j++) {
            if (spinState.getItemAtPosition(j).equals(strState)) {
                spinState.setSelection(j);
                break;
            }
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageDiscussionGroupsActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileAdminActivity.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtTitle.getEditText().getText().toString().trim().isEmpty()) {
                    txtTitle.getEditText().setError(getString(R.string.title_error));
                    txtTitle.requestFocus();
                    return;
                }
                if (txtDesc.getEditText().getText().toString().trim().isEmpty()) {
                    txtDesc.getEditText().setError(getString(R.string.desc_error));
                    txtDesc.getEditText().requestFocus();
                    return;
                }
                else {
                    strTitle = txtTitle.getEditText().getText().toString().trim();
                    strDesc = txtDesc.getEditText().getText().toString().trim();
                    strState = spinState.getSelectedItem().toString();
                    UpdateGroup();
                }
            }
        });

    }

    public void UpdateGroup() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("discussion_group");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot snap : snapshot.getChildren()) {
                         DiscussionGroup groupsRecord = snap.getValue(DiscussionGroup.class);
                         if (groupsRecord.getGroupTitle().equals(strTitle) && !(groupsRecord.getGroupID().equals(strID))) {
                             group_found = true;
                         }
                     }
                     if (group_found == true) {
                         Toast.makeText(getApplicationContext(), getString(R.string.title_used), Toast.LENGTH_LONG).show();
                         group_found = false;
                         progress.setVisibility(View.INVISIBLE);
                     } else {
                         DatabaseReference dR = FirebaseDatabase.getInstance().getReference("discussion_group").child(strID);
                         DiscussionGroup user = new DiscussionGroup(strID, strTitle, strDesc, strState);
                         dR.setValue(user);
                         progress.setVisibility(View.INVISIBLE);
                         //displaying a success toast
                         Toast.makeText(getApplicationContext(), "Success Edit Group", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getApplicationContext(), ManageDiscussionGroupsActivity.class);
                         startActivity(intent);
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