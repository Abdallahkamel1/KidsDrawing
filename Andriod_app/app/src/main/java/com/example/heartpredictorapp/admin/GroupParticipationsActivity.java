package com.example.heartpredictorapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.adapters.GroupAdapter;
import com.example.heartpredictorapp.adapters.PatricipateAdapter;
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.example.heartpredictorapp.models.GroupParticipation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupParticipationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<GroupParticipation> partArrayList;
    PatricipateAdapter partAdapter;
    ImageView imgBack, imgLogout, imgEditProfile;
    String strID, strTitle, strState;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_participations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strID = getIntent().getStringExtra("id");
        strTitle = getIntent().getStringExtra("title");
        strState = getIntent().getStringExtra("state");

        txtTitle = findViewById(R.id.txtTitle);
        recyclerView = findViewById(R.id.recyclerView);
        imgBack = findViewById(R.id.imgBack);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        txtTitle.setText("Group: " + strTitle + " (" + strState + ")");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(partAdapter);
        recyclerView.setHasFixedSize(true);

        partArrayList = new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ManageDiscussionGroupsActivity.class);
                startActivity(i);
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

        clearAll();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("group_participation");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for(DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    GroupParticipation group = dataSnapshot.getValue(GroupParticipation.class);
                    if(group.getGroupID().equals(strID)){
                        partArrayList.add(group);
                    }
                }
                partAdapter = new PatricipateAdapter(getApplicationContext(), partArrayList);
                recyclerView.setAdapter(partAdapter);
                partAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void clearAll(){
        if(partArrayList != null){
            partArrayList.clear();
        }
        if(partAdapter != null){
            partAdapter.notifyDataSetChanged();
        }
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