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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petsplace.R;
import com.example.petsplace.auxiliary.Upload;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MissingDataAdapter extends RecyclerView.Adapter<MissingDataAdapter.MissingListViewHolder> {

    private ArrayList<Upload> picturesUpload;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public static int pressedAll = -1;
    public static String goToUsername = "none";

    public MissingDataAdapter(Context context, ArrayList<Upload> uploads, RecyclerViewInterface recyclerViewInterface) {
        picturesUpload = uploads;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public MissingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_missing, parent, false);
        return new MissingListViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MissingListViewHolder holder, int position) {
        Upload info;
        info = picturesUpload.get(position);

        holder.Header.setText(info.getText());
        holder.Username.setText(info.getAuthor());

        Picasso.with(context)
                .load(info.getmImageUrl())
                .fit()
                .centerCrop()
                .into(holder.Picture);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(picturesUpload.get(position).getAuthor()) ;

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String upload = snapshot.getValue(String.class);

                Picasso.with(context)
                        .load(upload)
                        .fit()
                        .centerCrop()
                        .into(holder.profileImage);
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
        return picturesUpload.size();
    }

    protected static class MissingListViewHolder extends RecyclerView.ViewHolder implements RecyclerViewInterface{
        TextView Username;
        TextView Header;
        CircleImageView profileImage;
        ImageView Picture;
        CardView card;


        public MissingListViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.friendPictureList);
            Username = itemView.findViewById(R.id.friendNameList);
            Header = itemView.findViewById(R.id.missText);
            Picture = itemView.findViewById(R.id.missImage);
            card = itemView.findViewById(R.id.card1);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("MyTAG","Нажата вся карта");
                    goToUsername = Username.getText().toString();
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
        public void onItemClick(int position) {

        }
    }
}
