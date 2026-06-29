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
import com.example.heartpredictorapp.models.Child;
import com.example.heartpredictorapp.parent.UpdateChildActivity;
import com.example.heartpredictorapp.parent.UploadDrawingParentActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Child> childArrayList;

    public ChildAdapter(Context context, ArrayList<Child> childArrayList) {
        this.context = context;
        this.childArrayList = childArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_kids_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Child child = childArrayList.get(position);
        holder.txtName.setText(child.getChildName());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.ask_delete_child);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deleteChild(child.getChildID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpdateChildActivity.class);
                i.putExtra("id", child.getChildID());
                i.putExtra("name", child.getChildName());
                i.putExtra("age", child.getChildAge());
                i.putExtra("sex", child.getChildSex());
                i.putExtra("pass", child.getChildPass());
                i.putExtra("parentID", child.getParentID());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.imgDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UploadDrawingParentActivity.class);
                i.putExtra("id", child.getChildID());
                i.putExtra("name", child.getChildName());
                i.putExtra("parentID", child.getParentID());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgDelete, imgEdit, imgDraw;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDraw = itemView.findViewById(R.id.imgProgress);
        }
    }
    private boolean deleteChild(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("child").child(id);
        dR.removeValue();
        Toast.makeText(context, R.string.child_deleted, Toast.LENGTH_LONG).show();
        return true;
    }
}
