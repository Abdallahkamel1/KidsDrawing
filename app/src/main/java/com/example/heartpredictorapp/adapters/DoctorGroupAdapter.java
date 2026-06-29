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

import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.doctor.DoctorParticipateGroupActivity;
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.example.heartpredictorapp.parent.ParentParticipateGroupActivity;

import java.util.ArrayList;

public class DoctorGroupAdapter extends RecyclerView.Adapter<DoctorGroupAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<DiscussionGroup> groupsArrayList;

    public DoctorGroupAdapter(Context context, ArrayList<DiscussionGroup> groupArrayList) {
        this.context = context;
        this.groupsArrayList = groupArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_group_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscussionGroup group = groupsArrayList.get(position);
        holder.txtName.setText(group.getGroupTitle());
        if(group.getGroupState().equals("active")) {
            holder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), DoctorParticipateGroupActivity.class);
                    i.putExtra("id", group.getGroupID());
                    i.putExtra("title", group.getGroupTitle());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }else{
            Toast.makeText(context, "Discussion Group is disabled", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return groupsArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgShare;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgShare = itemView.findViewById(R.id.imgShare);
        }
    }
}
