package com.example.heartpredictorapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.doctor.ViewAppointmentsActivity;
import com.example.heartpredictorapp.doctor.ViewDrawingsActivity;
import com.example.heartpredictorapp.models.Appointment;
import com.example.heartpredictorapp.models.Drawing;
import com.example.heartpredictorapp.models.Report;
import com.example.heartpredictorapp.parent.ParentActivity;
import com.example.heartpredictorapp.parent.UpdateAppointmentActivity;
import com.example.heartpredictorapp.parent.ViewDoctorsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAppointmentAdapter extends RecyclerView.Adapter<ViewAppointmentAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Appointment> appointmentArrayList;

    public ViewAppointmentAdapter(Context context, ArrayList<Appointment> appointmentArrayList) {
        this.context = context;
        this.appointmentArrayList = appointmentArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_appointments_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appoint = appointmentArrayList.get(position);
        holder.txtName.setText(appoint.getAppDate() + " - " + appoint.getAppTime() + " [" + appoint.getAppStatus() + "]");
        holder.imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Accept");
                builder.setMessage("Do you accept the appointment?");
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        AcceptAppoint(appoint.getAppID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Reject");
                builder.setMessage("Do you reject the appointment?");
                builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        RejectAppoint(appoint.getAppID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgAccept, imgReject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgReject = itemView.findViewById(R.id.imgReject);
            imgAccept = itemView.findViewById(R.id.imgAccept);
        }
    }
    public void AcceptAppoint(String id) {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("appointment");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Appointment groupsRecord = snap.getValue(Appointment.class);
                    if (groupsRecord.getAppID().equals(id)) {
                        String firebaseKey = snap.getKey();
                        DatabaseReference updateRef = databaseGroup.child(firebaseKey);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("appStatus", "Accepted");  // Replace with your new value
                        updateRef.updateChildren(updates).addOnCompleteListener(task -> {
                            Intent i = new Intent(context, ViewAppointmentsActivity.class);
                            context.startActivity(i);
                        });
                        break; // Exit loop after update
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });
    }
    public void RejectAppoint(String id) {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("appointment");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Appointment groupsRecord = snap.getValue(Appointment.class);
                    if (groupsRecord.getAppID().equals(id)) {
                        String firebaseKey = snap.getKey();
                        DatabaseReference updateRef = databaseGroup.child(firebaseKey);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("appStatus", "Rejected");  // Replace with your new value
                        updateRef.updateChildren(updates).addOnCompleteListener(task -> {
                            Intent i = new Intent(context, ViewAppointmentsActivity.class);
                            context.startActivity(i);
                        });
                        break; // Exit loop after update
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });
    }
}
