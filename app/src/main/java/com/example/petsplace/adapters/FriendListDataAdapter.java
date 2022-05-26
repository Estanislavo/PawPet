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
import com.example.petsplace.auxiliary.UserInformation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListDataAdapter extends RecyclerView.Adapter<FriendListDataAdapter.FriendListViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    public static int pressedAll = -1;
    public static String goToUsername = "none";
    private Context context;

    private ArrayList<String> friendNames;

    public FriendListDataAdapter(Context context, ArrayList<String> names, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        friendNames = names;
        this.context = context;
    }


    @NonNull
    @Override
    public FriendListDataAdapter.FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_friends_list, parent, false);
        return new FriendListDataAdapter.FriendListViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListDataAdapter.FriendListViewHolder holder, int position) {
        String friendN;

        friendN = friendNames.get(position);


        holder.friendName.setText(friendN);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(friendNames.get(position));
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
        holder.friendIcon.setImageResource(R.drawable.man_avatr);
    }

    @Override
    public int getItemCount() {
        return friendNames.size();
    }

    protected static class FriendListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendName;
        TextView friendAge;
        CircleImageView friendIcon;
        ImageView goToChatListButton;


        public FriendListViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendNameList);
            friendAge = itemView.findViewById(R.id.friendAgeList);
            friendIcon = itemView.findViewById(R.id.friendPictureList);
            goToChatListButton = itemView.findViewById(R.id.goToChatList);

            goToChatListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("MyTAG","Выбран переход");
                    goToUsername = friendName.getText().toString();
                    pressedAll = 0;
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
                    Log.d("MyTAG","Нажата вся карта");
                    goToUsername = friendName.getText().toString();
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
