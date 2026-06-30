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
import com.example.heartpredictorapp.doctor.ChatDoctorActivity;
import com.example.heartpredictorapp.doctor.ViewDrawingDoctorActivity;
import com.example.heartpredictorapp.models.Drawing;
import com.example.heartpredictorapp.models.Report;
import com.example.heartpredictorapp.parent.ChatParentActivity;
import com.example.heartpredictorapp.parent.RequestAnalysisActivity;
import com.example.heartpredictorapp.parent.ViewAnalysisActivity;
import com.example.heartpredictorapp.parent.ViewDrawingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrawingsDoctorAdapter extends RecyclerView.Adapter<DrawingsDoctorAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Report> drawingArrayList;

    public DrawingsDoctorAdapter(Context context, ArrayList<Report> drawingArrayList) {
        this.context = context;
        this.drawingArrayList = drawingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_drawing_doctor_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report draw = drawingArrayList.get(position);
        holder.txtDate.setText(draw.getReportDate());
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ViewDrawingDoctorActivity.class);
                i.putExtra("file", draw.getDrawFile());
                i.putExtra("drawID", draw.getDrawID());
                i.putExtra("parentID", draw.getParentID());
                i.putExtra("reportID", draw.getReportID());
                i.putExtra("reportDate", draw.getReportDate());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ChatDoctorActivity.class);
                i.putExtra("id", draw.getParentID());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawingArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate;
        private ImageView imgView, imgChat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgView = itemView.findViewById(R.id.imgView);
            imgChat = itemView.findViewById(R.id.imgChat);
        }
    }
}
