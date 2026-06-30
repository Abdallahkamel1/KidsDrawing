package com.example.heartpredictorapp.doctor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.adapters.PatricipateAdapter;
import com.example.heartpredictorapp.models.GroupParticipation;
import com.example.heartpredictorapp.parent.ParentDiscussionGroupActivity;
import com.example.heartpredictorapp.parent.ParentParticipateGroupActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class DoctorParticipateGroupActivity extends AppCompatActivity {

    TextView txtTitle;
    TextInputLayout txtParticipate;
    AppCompatButton btnSubmit;
    RecyclerView recyclerView;
    ArrayList<GroupParticipation> partArrayList;
    PatricipateAdapter partAdapter;
    ImageView imgBack;
    String strID, strTitle, strParticipate;
    boolean group_found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_participate_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strID = getIntent().getStringExtra("id");
        strTitle = getIntent().getStringExtra("title");

        txtTitle = findViewById(R.id.txtGroup);
        recyclerView = findViewById(R.id.recyclerView);
        imgBack = findViewById(R.id.imgBack);
        txtParticipate = findViewById(R.id.txtParticipate);
        btnSubmit = findViewById(R.id.butSubmit);

        txtTitle.setText("Group: " + strTitle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(partAdapter);
        recyclerView.setHasFixedSize(true);

        partArrayList = new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoctorDiscussionGroupActivity.class);
                startActivity(i);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtParticipate.getEditText().getText().toString().trim().isEmpty()) {
                    txtParticipate.getEditText().setError("Write your Contribution");
                    txtParticipate.requestFocus();
                    return;
                }
                else {
                    strParticipate = txtParticipate.getEditText().getText().toString().trim();
                    AddParticipation();
                }
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

    public void AddParticipation() {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("group_participation");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot snap : snapshot.getChildren()) {
                     GroupParticipation groupsRecord = snap.getValue(GroupParticipation.class);
                     if (groupsRecord.getGroupID().equals(strID)) {
                         group_found = true;
                     }
                 }
                 if (group_found == true) {
                     LocalDate currentDate = null;
                     String id = databaseGroup.push().getKey();
                     String doctorID = LoginActivity.doctor.getDoctorID();
                     String doctorName = LoginActivity.doctor.getDoctorName();
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                         currentDate = LocalDate.now();
                     }
                     String shareDate = String.valueOf(currentDate);
                     GroupParticipation user = new GroupParticipation(id, strID, "doctor", doctorID, doctorName, shareDate, strParticipate);
                     databaseGroup.child(id).setValue(user);
                     txtParticipate.getEditText().setText("");
                     //displaying a success toast
                     Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                     //Intent intent = new Intent(getApplicationContext(), DoctorParticipateGroupActivity.class);
                     //intent.putExtra("id", strID);
                    // intent.putExtra("title", strTitle);
                   //  startActivity(intent);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         }
        );
    }
}