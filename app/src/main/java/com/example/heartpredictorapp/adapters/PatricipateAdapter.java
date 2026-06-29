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
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.example.heartpredictorapp.models.GroupParticipation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PatricipateAdapter extends RecyclerView.Adapter<PatricipateAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<GroupParticipation> partsArrayList;

    public PatricipateAdapter(Context context, ArrayList<GroupParticipation> groupArrayList) {
        this.context = context;
        this.partsArrayList = groupArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participate_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupParticipation group = partsArrayList.get(position);
        holder.txtName.setText(group.getUserName() + " [" + group.getUserType() + "]");
        holder.txtMsg.setText(group.getPartMsg());
        holder.txtDate.setText(group.getPartDate());
    }

    @Override
    public int getItemCount() {
        return partsArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtMsg, txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtMsg = itemView.findViewById(R.id.txtMsg);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
