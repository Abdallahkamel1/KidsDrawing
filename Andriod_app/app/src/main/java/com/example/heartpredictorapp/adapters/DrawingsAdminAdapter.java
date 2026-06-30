package com.example.heartpredictorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.admin.ViewDrawingAdminActivity;
import com.example.heartpredictorapp.admin.ViewReportsActivity;
import com.example.heartpredictorapp.doctor.ViewDrawingDoctorActivity;
import com.example.heartpredictorapp.models.Report;

import java.util.ArrayList;

public class DrawingsAdminAdapter extends RecyclerView.Adapter<DrawingsAdminAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Report> drawingArrayList;

    public DrawingsAdminAdapter(Context context, ArrayList<Report> drawingArrayList) {
        this.context = context;
        this.drawingArrayList = drawingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_drawing_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report draw = drawingArrayList.get(position);
        holder.txtDate.setText(draw.getReportDate());
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ViewDrawingAdminActivity.class);
                i.putExtra("file", draw.getDrawFile());
                i.putExtra("drawID", draw.getDrawID());
                i.putExtra("doctorID", draw.getDoctorID());
                i.putExtra("parentID", draw.getParentID());
                i.putExtra("reportID", draw.getReportID());
                i.putExtra("reportDate", draw.getReportDate());
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
        private ImageView imgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}
