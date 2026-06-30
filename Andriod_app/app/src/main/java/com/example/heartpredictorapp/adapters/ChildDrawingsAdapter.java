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
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Drawing;
import com.example.heartpredictorapp.parent.RequestAnalysisActivity;
import com.example.heartpredictorapp.parent.ViewAnalysisActivity;
import com.example.heartpredictorapp.parent.ViewDrawingActivity;
import com.example.heartpredictorapp.parent.ViewDrawingParentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChildDrawingsAdapter extends RecyclerView.Adapter<ChildDrawingsAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Drawing> drawingArrayList;

    public ChildDrawingsAdapter(Context context, ArrayList<Drawing> drawingArrayList) {
        this.context = context;
        this.drawingArrayList = drawingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kids_drawing_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drawing draw = drawingArrayList.get(position);
        holder.txtName.setText(draw.getChildName());
        holder.txtDate.setText("Upload Date: " + draw.getUploadDate());
        holder.txtState.setText("State: "+ draw.getAnalyzeState());
        if(draw.getAnalyzeState().equals("done")){
            holder.txtReport.setVisibility(View.VISIBLE);
            holder.imgReport.setVisibility(View.VISIBLE);
        }else{
            holder.txtReport.setVisibility(View.INVISIBLE);
            holder.imgReport.setVisibility(View.INVISIBLE);
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.ask_delete_drawing);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deleteDrawing(draw.getDrawID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ViewDrawingActivity.class);
                i.putExtra("file", draw.getDrawFile());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ViewAnalysisActivity.class);
                i.putExtra("drawID", draw.getDrawID());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
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
                        RequestAnalysis(draw.getDrawID(),draw.getParentID(),draw.getChildID(),draw.getChildName(),draw.getDrawFile(), draw.getUploadDate());
                        Intent i = new Intent(view.getContext(), RequestAnalysisActivity.class);
                        i.putExtra("id", draw.getDrawID());
                        i.putExtra("file", draw.getDrawFile());
                        i.putExtra("parent", draw.getParentID());
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
        return drawingArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtDate, txtState, txtReport;
        private ImageView imgDelete, imgView, imgRequest, imgReport;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtUploadDate);
            txtState = itemView.findViewById(R.id.txtState);
            imgView = itemView.findViewById(R.id.imgView);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgRequest = itemView.findViewById(R.id.imgWrite);
            imgReport = itemView.findViewById(R.id.imgReport);
            txtReport = itemView.findViewById(R.id.txtReport);
        }
    }
    private boolean deleteDrawing(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("drawing").child(id);
        dR.removeValue();
        Toast.makeText(context, R.string.draw_deleted, Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean RequestAnalysis(String id, String pID, String cID, String cName, String dFile, String upload) {
        DatabaseReference databaseDraw = FirebaseDatabase.getInstance().getReference("drawing");
        databaseDraw.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     //update state
                     String state = "requested";
                     DatabaseReference dR = FirebaseDatabase.getInstance().getReference("drawing").child(id);
                     Drawing drawing = new Drawing(id, pID, cID, cName, dFile, upload, state);
                     dR.setValue(drawing);
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
