package com.example.petsplace.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petsplace.auxiliary.MessageClass;
import com.example.petsplace.R;
import com.example.petsplace.auxiliary.UserInformation;

import java.util.ArrayList;

public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.MessageViewHolder> {

    ArrayList<MessageClass> messages;
    int isMine = 1;

    private final int userNotMine = 0;
    private final int userMine = 1;

    LayoutInflater inflater;

    public MessageDataAdapter(Context context, ArrayList<MessageClass> messages) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case userMine:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_message, parent, false);
                break;
            case userNotMine:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_not_mine_message, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_message, parent, false);
        }
        return new MessageViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        // условие для определения айтем какого типа выводить в конкретной позиции
        if (messages.get(position).getUsername().compareTo(UserInformation.username) == 0) return userMine;
        return userNotMine;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        MessageClass msg = messages.get(position);
        holder.message.setText(msg.getMessage());

        if (msg.getUsername() == UserInformation.username){
            isMine = 1;
            Log.d("MyTAG","Изменение");
        }
        else{
            isMine = 0;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView message;

        public MessageViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_item);
        }
    }
}
