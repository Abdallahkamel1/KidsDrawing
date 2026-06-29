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
import com.example.heartpredictorapp.admin.GroupParticipationsActivity;
import com.example.heartpredictorapp.admin.UpdateGroupActivity;
import com.example.heartpredictorapp.models.DiscussionGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<DiscussionGroup> groupsArrayList;

    public GroupAdapter(Context context, ArrayList<DiscussionGroup> groupArrayList) {
        this.context = context;
        this.groupsArrayList = groupArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_discussion_group_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscussionGroup group = groupsArrayList.get(position);
        holder.txtName.setText(group.getGroupTitle());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.ask_delete_group);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deleteGroup(group.getGroupID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpdateGroupActivity.class);
                i.putExtra("id", group.getGroupID());
                i.putExtra("title", group.getGroupTitle());
                i.putExtra("desc", group.getGroupDesc());
                i.putExtra("state", group.getGroupState());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GroupParticipationsActivity.class);
                i.putExtra("id", group.getGroupID());
                i.putExtra("title", group.getGroupTitle());
                i.putExtra("state", group.getGroupState());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupsArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgDelete, imgEdit, imgShare;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgShare = itemView.findViewById(R.id.imgShare);
        }
    }
    private boolean deleteGroup(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("discussion_group").child(id);
        dR.removeValue();
        Toast.makeText(context, R.string.group_deleted, Toast.LENGTH_LONG).show();
        return true;
    }
}
