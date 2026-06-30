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

import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.doctor.ChatDoctorActivity;
import com.example.heartpredictorapp.models.Parent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DoctorAllParentsAdapter extends RecyclerView.Adapter<DoctorAllParentsAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Parent> parentArrayList;
    public String userID;

    public DoctorAllParentsAdapter(Context context, ArrayList<Parent> parentArrayList) {
        this.context = context;
        this.parentArrayList = parentArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_parents_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parent parent = parentArrayList.get(position);
        holder.txtName.setText(parent.getParentName());
        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ChatDoctorActivity.class);
                i.putExtra("id", parent.getParentID());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parentArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgChat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgChat = itemView.findViewById(R.id.imgChat);
        }
    }
}
