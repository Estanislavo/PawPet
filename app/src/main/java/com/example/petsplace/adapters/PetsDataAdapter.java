package com.example.petsplace.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petsplace.auxiliary.Animal;
import com.example.petsplace.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetsDataAdapter extends RecyclerView.Adapter<PetsDataAdapter.PetsViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    public static String goToPet = "none";
    public static boolean isGoToPet = false;
    private final int userNotMine = 0;
    private final int userMine = 1;

    ArrayList<Animal> pets;
    LayoutInflater inflater;


    public PetsDataAdapter(Context context, ArrayList<Animal> messages,RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.pets = messages;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public PetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case userMine:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
                break;
            case userNotMine:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_profile, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_profile, parent, false);
        }
        return new PetsViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PetsViewHolder holder, int position) {
        if (pets.get(position).getName().compareTo("-") == 0){
            String friendN = pets.get(position).getName();
            holder.friendName.setText(friendN);
        }
        else{
            String friendN = pets.get(position).getName();
            String petType = pets.get(position).getType();
            holder.friendName.setText(friendN);

            switch (petType){
                case "кошка":
                    holder.friendImage.setImageResource(R.drawable.kitty);
                    break;
                case "собака":
                    holder.friendImage.setImageResource(R.drawable.puppy);
                    break;
                case "птица":
                    holder.friendImage.setImageResource(R.drawable.bird);
                    break;
                case "рыба":
                    holder.friendImage.setImageResource(R.drawable.fish);
                    break;
                case "грызун":
                    holder.friendImage.setImageResource(R.drawable.rabbit);
                    break;
                case "экзотический":
                    holder.friendImage.setImageResource(R.drawable.lion);
                    break;
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        // условие для определения айтем какого типа выводить в конкретной позиции
        if (pets.get(position).getName().compareTo("-") == 0) {
            return userMine;
        }
        return userNotMine;
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    protected static class PetsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendName;
        CircleImageView friendImage;
        CircleImageView add;


        public PetsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            friendImage = itemView.findViewById(R.id.friendPicture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isGoToPet = true;
                    goToPet = friendName.getText().toString();
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