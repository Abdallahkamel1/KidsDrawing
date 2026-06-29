package com.example.heartpredictorapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.admin.AdminActivity;
import com.example.heartpredictorapp.models.Admin;
import com.example.heartpredictorapp.models.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Doctor> doctorArrayList;
    public String userID;

    public DoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList) {
        this.context = context;
        this.doctorArrayList = doctorArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_doctors_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctors = doctorArrayList.get(position);
        holder.txtName.setText(doctors.getDoctorName());
        holder.txtState.setText("State: " + doctors.getDoctorState());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.ask_delete);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deleteDoctor(doctors.getDoctorID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.activate);
                builder.setMessage(R.string.ask_activate);
                builder.setPositiveButton(R.string.activate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        activateDoctor(doctors.getDoctorID(), doctors.getDoctorName(), doctors.getDoctorEmail(), doctors.getDoctorPhone(), doctors.getDoctorPass());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.deactivate);
                builder.setMessage(R.string.ask_deactivate);
                builder.setPositiveButton(R.string.deactivate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deactivateDoctor(doctors.getDoctorID(), doctors.getDoctorName(), doctors.getDoctorEmail(), doctors.getDoctorPhone(), doctors.getDoctorPass());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog card_doctor = new Dialog(view.getContext());
                card_doctor.setContentView(R.layout.card_doctor);
                AppCompatButton btnClose = card_doctor.findViewById(R.id.btnClose);
                TextView card_txtName = card_doctor.findViewById(R.id.txtName);
                TextView card_txtEmail = card_doctor.findViewById(R.id.txtEmail);
                TextView card_txtPhone = card_doctor.findViewById(R.id.txtPhone);

                card_doctor.show();
                card_txtName.setText(doctors.getDoctorName());
                card_txtEmail.setText(doctors.getDoctorEmail());
                card_txtPhone.setText("Mobile: " + doctors.getDoctorPhone());
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        card_doctor.hide();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtState;
        private ImageView imgDelete, imgView, imgActivate, imgDeactivate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgView = itemView.findViewById(R.id.imgView);
            txtState = itemView.findViewById(R.id.txtState);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgActivate = itemView.findViewById(R.id.imgActivate);
            imgDeactivate = itemView.findViewById(R.id.imgDeactivate);
        }
    }
    private boolean deleteDoctor(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("doctor").child(id);
        dR.removeValue();
        Toast.makeText(context, R.string.doctor_deleted, Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean activateDoctor(String id, String name, String email, String phone, String pass) {
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("doctor");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     //update state
                     String state = "active";
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("doctor").child(id);
                     Doctor user = new Doctor(id, name, email, phone, pass, state);
                     dR.setValue(user);
                     //displaying a success toast
                     Toast.makeText(context, "Success Activate Doctor", Toast.LENGTH_LONG).show();
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
        );
        return true;
    }
    private boolean deactivateDoctor(String id, String name, String email, String phone, String pass) {
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("doctor");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     //update state
                     String state = "disable";
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("doctor").child(id);
                     Doctor user = new Doctor(id, name, email, phone, pass, state);
                     dR.setValue(user);
                     //displaying a success toast
                     Toast.makeText(context, "Doctor Deactivated", Toast.LENGTH_LONG).show();
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
        );
        return true;
    }
}
