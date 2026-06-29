package com.example.heartpredictorapp.doctor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.adapters.ItemArrayAdapter;
import com.example.heartpredictorapp.models.Chat;
import com.example.heartpredictorapp.models.Parent;
import com.example.heartpredictorapp.parent.ParentAllDoctorsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;


public class ChatDoctorActivity extends AppCompatActivity {

    TextView txtName;
    RecyclerView recyclerView;
    ArrayList<Chat> messages;
    private ImageView buttonSend;
    String msgTxt;
    ImageView imgBack;
    ItemArrayAdapter adapter;
    String doctorID, doctorName, parentID, parentName;
    EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        parentID = intent.getStringExtra("id");
        doctorID = LoginActivity.doctor.getDoctorID();

        imgBack = findViewById(R.id.imgBack);
        txtName = findViewById(R.id.txtName);
        buttonSend = findViewById(R.id.buttonSend);
        editTextMessage = findViewById(R.id.editTextMessage);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ReadParent();

        messages = new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewDrawingsActivity.class);
                startActivity(intent);
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextMessage.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Type a message", Toast.LENGTH_SHORT).show();
                } else {
                    msgTxt = editTextMessage.getText().toString().trim();
                    AddMessage();
                }
            }
        });

        clearAll();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                Chat messag;
                for(DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getParentID().equals(parentID) && chat.getDoctorID().equals(doctorID)){
                        if(chat.getSenderType().equals("doctor")){
                            messag = new Chat(chat.getChatID(), chat.getParentID(), chat.getDoctorID(), chat.getChatMsg(), chat.getChatDate(), chat.getSenderType());
                        }else{
                            messag = new Chat(chat.getChatID(), chat.getParentID(), chat.getDoctorID(), chat.getChatMsg(), chat.getChatDate(), chat.getSenderType());
                        }
                        messages.add(messag);
                    }
                }
                adapter = new ItemArrayAdapter(getApplicationContext(), messages, "doctor");
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void clearAll(){
        if(messages != null){
            messages.clear();
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    public void AddMessage() {
        DatabaseReference databaseMSG = FirebaseDatabase.getInstance().getReference("chat");
        databaseMSG.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       LocalDate currentDate = null;
                       String id = databaseMSG.push().getKey();
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                           currentDate = LocalDate.now();
                       }
                       String shareDate = String.valueOf(currentDate);
                       Chat chat = new Chat(id, parentID, doctorID, msgTxt, shareDate, "doctor");
                       databaseMSG.child(id).setValue(chat);
                       editTextMessage.setText("");
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               }
        );
    }
    public void ReadParent() {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("parent");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Parent groupsRecord = snap.getValue(Parent.class);
                    if (groupsRecord.getParentID().equals(parentID)) {
                        parentName = groupsRecord.getParentName();
                        break; // Exit loop after update
                    }
                }
                txtName.setText("Chat with " + parentName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });
    }
}