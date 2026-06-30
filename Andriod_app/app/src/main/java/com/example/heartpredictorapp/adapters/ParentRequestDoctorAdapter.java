package com.example.heartpredictorapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.example.heartpredictorapp.models.Child;
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Drawing;
import com.example.heartpredictorapp.models.Report;
import com.example.heartpredictorapp.parent.ParentActivity;
import com.example.heartpredictorapp.parent.ViewDoctorsActivity;
import com.example.heartpredictorapp.parent.ViewDrawingParentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class ParentRequestDoctorAdapter extends RecyclerView.Adapter<ParentRequestDoctorAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Doctor> doctorArrayList;
    String strDrawID, strFile, strParentID;

    public ParentRequestDoctorAdapter(Context context, ArrayList<Doctor> doctorArrayList) {
        this.context = context;
        this.doctorArrayList = doctorArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_doctors_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctors = doctorArrayList.get(position);
        holder.txtName.setText(doctors.getDoctorName());
        holder.imgRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.request);
                builder.setMessage(R.string.request_analysis);
                builder.setPositiveButton(R.string.request, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        RequestDoctor(doctors.getDoctorID(),ViewDoctorsActivity.strDrawID,ViewDoctorsActivity.strFile,ViewDoctorsActivity.strParentID);
                        Intent i = new Intent(view.getContext(), ParentActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgRequest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgRequest = itemView.findViewById(R.id.imgRequest);
        }
    }
    private boolean RequestDoctor(String doctorID, String drawID, String dFile, String parentID) {
        DatabaseReference databaseDraw = FirebaseDatabase.getInstance().getReference("report");
        databaseDraw.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String uploadDate = "";
                    String id = databaseDraw.push().getKey();
                    String diagnose = "-";
                    String recommend = "-";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate currentDate = LocalDate.now();
                        uploadDate = String.valueOf(currentDate);
                    }
                    strParentID = LoginActivity.parent.getParentID();
                    Report report = new Report(id, drawID, strParentID, doctorID, dFile, diagnose, recommend, uploadDate);
                    databaseDraw.child(id).setValue(report);
                    //displaying a success toast
                    Toast.makeText(context, "Success Submit Request", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }
        );
        return true;
    }
}
