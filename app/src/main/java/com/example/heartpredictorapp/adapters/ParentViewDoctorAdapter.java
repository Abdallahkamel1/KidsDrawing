package com.example.heartpredictorapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.parent.AddAppointmentActivity;
import com.example.heartpredictorapp.parent.ChatParentActivity;
import com.example.heartpredictorapp.parent.ChildDrawingsActivity;
import com.example.heartpredictorapp.parent.UploadDrawingParentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentViewDoctorAdapter extends RecyclerView.Adapter<ParentViewDoctorAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Doctor> doctorArrayList;

    public ParentViewDoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList) {
        this.context = context;
        this.doctorArrayList = doctorArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_doctors_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctors = doctorArrayList.get(position);
        holder.txtName.setText(doctors.getDoctorName());
        holder.imgAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddAppointmentActivity.class);
                i.putExtra("id", doctors.getDoctorID());
                i.putExtra("name", doctors.getDoctorName());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ChatParentActivity.class);
                i.putExtra("id", doctors.getDoctorID());
                i.putExtra("name", doctors.getDoctorName());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgAppointment, imgChat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgAppointment = itemView.findViewById(R.id.imgAppointment);
            imgChat = itemView.findViewById(R.id.imgChat);
        }
    }
}
