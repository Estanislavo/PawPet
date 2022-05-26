package com.example.petsplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.petsplace.adapters.FriendRequestsDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.adapters.RecyclerViewReqInterface;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class FriendRequests extends AppCompatActivity implements RecyclerViewReqInterface {

    private RecyclerView friendRequests;
    private ArrayList<String> users = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        friendRequests = findViewById(R.id.friendAdd);

        friendRequests.hasFixedSize();
        friendRequests.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FriendRequestsDataAdapter adapter = new FriendRequestsDataAdapter(getApplicationContext(),users,this);
        friendRequests.setAdapter(adapter);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(UserInformation.username)
                .child("friend_requests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object> info = (HashMap<String, Object>) snapshot.getValue();
                Set<String> names = info.keySet();
                Log.d("MyTAG",String.valueOf(names));
                for (String name:names){
                    users.add(name);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(int position) {

    }
}