package com.example.heartpredictorapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Parent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder>  {
    public Context context;
    public ArrayList<Parent> parentArrayList;
    public String userID;

    public ParentAdapter(Context context, ArrayList<Parent> parentArrayList) {
        this.context = context;
        this.parentArrayList = parentArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_parents_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parent parent = parentArrayList.get(position);
        holder.txtName.setText(parent.getParentName());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.ask_delete_parent);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the RecyclerView
                        deleteParent(parent.getParentID());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog card_doctor = new Dialog(view.getContext());
                card_doctor.setContentView(R.layout.card_doctor);
                AppCompatButton btnClose = card_doctor.findViewById(R.id.btnClose);
                TextView card_txtName = card_doctor.findViewById(R.id.txtName);
                TextView card_txtEmail = card_doctor.findViewById(R.id.txtEmail);
                TextView card_txtPhone = card_doctor.findViewById(R.id.txtPhone);

                card_doctor.show();
                card_txtName.setText(parent.getParentName());
                card_txtEmail.setText(parent.getParentEmail());
                card_txtPhone.setText("Mobile: " + parent.getParentPhone());
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        card_doctor.hide();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return parentArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgDelete, imgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgView = itemView.findViewById(R.id.imgView);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
    private boolean deleteParent(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("parent").child(id);
        dR.removeValue();
        Toast.makeText(context, R.string.parent_deleted, Toast.LENGTH_LONG).show();
        return true;
    }
}
