package com.example.petsplace.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class FriendRequestsDataAdapter extends RecyclerView.Adapter<FriendRequestsDataAdapter.FriendRequestsListViewHolder> {


    private final RecyclerViewReqInterface recyclerViewInterface;
    public static int pressedAll = -1;
    public static int isAccept = -1;
    public static int isDecline = -1;
    public static String whoIs = "none";
    String friendN;

    private ArrayList<String> usersNames;

    private Context context;

    public FriendRequestsDataAdapter(Context context, ArrayList<String> names, RecyclerViewReqInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        usersNames = names;
        this.context = context;
    }


    @NonNull
    @Override
    public FriendRequestsDataAdapter.FriendRequestsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_friend_requests, parent, false);
        FriendRequestsListViewHolder friendRequestsListViewHolder = new FriendRequestsListViewHolder(view, recyclerViewInterface);
        return friendRequestsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestsDataAdapter.FriendRequestsListViewHolder holder, int position) {

        friendN = usersNames.get(position);

        holder.friendName.setText(friendN);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(usersNames.get(position));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String upload = snapshot.getValue(String.class);

                Picasso.with(context)
                        .load(upload)
                        .fit()
                        .centerCrop()
                        .into(holder.friendIcon);
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

    @Override
    public int getItemCount() {
        return usersNames.size();
    }

    protected static class FriendRequestsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendName;
        TextView friendAge;
        CircleImageView friendIcon;
        ImageView decline;
        ImageView accept;


        public FriendRequestsListViewHolder(@NonNull View itemView, RecyclerViewReqInterface recyclerViewInterface) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendNameListtt);
            friendAge = itemView.findViewById(R.id.friendAgeList);
            friendIcon = itemView.findViewById(R.id.friendPictureList);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);


            accept.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Log.d("MyTAG","Пользователь "+friendName.getText().toString()+" добавлен в друзья.");
                    isAccept = 1;
                    whoIs = friendName.getText().toString();
                    if ( recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }

                    }
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isAccept = 0;
                    if ( recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }

                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    whoIs = friendName.getText().toString();
                    pressedAll = 1;
                    if ( recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }

                    }
                }
            });


        }
        @Override
        public void onClick(View view) {
        }
    }
}
