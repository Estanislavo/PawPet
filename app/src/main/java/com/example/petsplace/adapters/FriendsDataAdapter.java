package com.example.petsplace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petsplace.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsDataAdapter extends RecyclerView.Adapter<FriendsDataAdapter.FriendsViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    public static String goToUsername = "none";
    private final int userNotMine = 0;
    private final int userMine = 1;

    private int isFriend = 0;

    ArrayList<java.lang.String> friends;
    private Context context;


    public FriendsDataAdapter(Context context, ArrayList<java.lang.String> messages,RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.friends = messages;
        this.context = context;
    }


    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case userMine:
                v = LayoutInflater.from(context).inflate(R.layout.item_add, parent, false);
                break;
            case userNotMine:
                v = LayoutInflater.from(context).inflate(R.layout.item_friends_profile, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_profile, parent, false);
        }
        return new FriendsViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        if (friends.get(position).compareTo("+") == 0){
            String friendN = friends.get(position);
            holder.friendName.setText(friendN);
        }
        else{
            String friendN = friends.get(position);
            holder.friendName.setText(friendN);

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                    .child("profilePhotos")
                    .child(friends.get(position));

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String upload = snapshot.getValue(String.class);

                    Picasso.with(context)
                            .load(upload)
                            .fit()
                            .centerCrop()
                            .into(holder.friendImage);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        // условие для определения айтем какого типа выводить в конкретной позиции
        if (friends.get(position).compareTo("+") == 0) return userMine;
        return userNotMine;
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    protected static class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendName;
        CircleImageView friendImage;


        public FriendsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            friendImage = itemView.findViewById(R.id.friendPicture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUsername = friendName.getText().toString();
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                        }
                }
            });


        }
        @Override
        public void onClick(View view) {
        }
    }
}