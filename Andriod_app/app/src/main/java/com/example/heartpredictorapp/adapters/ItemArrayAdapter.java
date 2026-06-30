package com.example.heartpredictorapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.models.Chat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {
    public static ArrayList<Chat> itemList;
    String userType;
    private int SELF = 786;
    Context context;
        // Constructor of the class
    public ItemArrayAdapter(Context context, ArrayList<Chat> itemList, String userType) {
        this.userType = userType;
        this.context = context;
        this.itemList = itemList;
    }
    //IN this method we are tracking the self message
    @Override
    public int getItemViewType(int position) {
        //getting message object of current position
        Chat message = itemList.get(position);
        if (message.getSenderType().equals(userType)) {
            //Returning self
            return SELF;
        }
        //else returning position
        return position;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == SELF){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_thread, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_thread_other, parent, false);
        }
        return new ViewHolder(view);
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        Chat message = itemList.get(listPosition);
        holder.msg.setText(message.getChatMsg());
        holder.msgTime.setText(message.getChatDate());
    }

    // Static inner class to initialize the views of rows
    class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView msg;
        public TextView msgTime;
        public ViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.textViewMessage);
            msgTime = (TextView) itemView.findViewById(R.id.textViewTime);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();
                    // Check if the position is valid
                    if (position != RecyclerView.NO_POSITION) {
                        // Remove the message at the clicked position
                        DeleteMsg(itemList.get(position).getChatID());
                        itemList.remove(position);
                        // Notify the adapter that an item has been removed
                        notifyItemRemoved(position);

                    }
                    return true; // Consume the long click event
                }
            });
        }
    }
    public boolean DeleteMsg(String id){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("chat").child(id);
        dR.removeValue();
        Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
        return true;
    }
}
