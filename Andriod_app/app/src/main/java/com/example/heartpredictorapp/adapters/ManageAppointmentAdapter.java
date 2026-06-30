package com.example.heartpredictorapp.adapters;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.admin.GroupParticipationsActivity;
import com.example.heartpredictorapp.admin.UpdateGroupActivity;
import com.example.heartpredictorapp.models.Appointment;
import com.example.heartpredictorapp.parent.UpdateAppointmentActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageAppointmentAdapter extends RecyclerView.Adapter<ManageAppointmentAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Appointment> appointmentArrayList;

    public ManageAppointmentAdapter(Context context, ArrayList<Appointment> appointmentArrayList) {
        this.context = context;
        this.appointmentArrayList = appointmentArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_appointments_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appoint = appointmentArrayList.get(position);
        holder.txtName.setText(appoint.getDoctorName());
        holder.txtDate.setText(appoint.getAppDate() + " - " + appoint.getAppTime() + " [" + appoint.getAppStatus() + "]");
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete);
                builder.setMessage("Delete selected Appointment?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deleteAPP(appoint.getAppID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appoint.getAppStatus().equals("wait")){
                    Intent i = new Intent(view.getContext(), UpdateAppointmentActivity.class);
                    i.putExtra("id", appoint.getAppID());
                    i.putExtra("parent", appoint.getParentID());
                    i.putExtra("doctorID", appoint.getDoctorID());
                    i.putExtra("doctorName", appoint.getDoctorName());
                    i.putExtra("appDate", appoint.getAppDate());
                    i.putExtra("appTime", appoint.getAppTime());
                    i.putExtra("appState", appoint.getAppStatus());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }else{
                    Toast.makeText(context, "Appointment is handled you can not update", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtDate;
        private ImageView imgDelete, imgEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
    private boolean deleteAPP(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("appointment").child(id);
        dR.removeValue();
        Toast.makeText(context, "Appointment Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
}
