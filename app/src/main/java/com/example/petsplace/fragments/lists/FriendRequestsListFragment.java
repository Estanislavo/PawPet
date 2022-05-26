package com.example.petsplace.fragments.lists;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petsplace.ChatList;
import com.example.petsplace.R;
import com.example.petsplace.adapters.FriendListDataAdapter;
import com.example.petsplace.adapters.FriendRequestsDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.adapters.RecyclerViewReqInterface;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FriendRequestsListFragment extends Fragment implements RecyclerViewReqInterface {
    private RecyclerView friendRequests;
    private ArrayList<String> users = new ArrayList<String>();
    public static int off = 0;
    public static ArrayList<String> newUsers = new ArrayList<String>();
    private FriendRequestsDataAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_requests_list, container, false);

        friendRequests = view.findViewById(R.id.friendAdd);

        friendRequests.hasFixedSize();
        friendRequests.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FriendRequestsDataAdapter(getActivity(),users,this);
        friendRequests.setAdapter(adapter);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(UserInformation.username)
                .child("friend_requests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> info = (HashMap<String, Object>) snapshot.getValue();
                    Set<String> names = info.keySet();
                    Log.d("MyTAG", String.valueOf(names));
                    for (String name : names) {
                        users.add(name);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    @Override
    public void onItemClick(int position) {
        off = 1;

        if (off == 1) {
            if (FriendRequestsDataAdapter.isAccept == 1) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(UserInformation.username)
                        .child("friend_requests");
                ref.child(users.get(position)).removeValue();
                off = 1;

                DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(UserInformation.username)
                        .child("friends");
                off = 1;


                UserInformation.addUsername = FriendRequestsDataAdapter.whoIs;
                friendRef.push().setValue(users.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });

                DatabaseReference friendRefT = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(users.get(position))
                        .child("friends");
                off = 1;
                friendRefT.push().setValue(UserInformation.username).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

                users.remove(position);
                adapter.notifyDataSetChanged();
            }

            else if (FriendRequestsDataAdapter.isAccept == 0){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(UserInformation.username)
                        .child("friend_requests");
                ref.child(users.get(position)).removeValue();
                users.remove(position);
                adapter.notifyDataSetChanged();

            }
        }
    }
}