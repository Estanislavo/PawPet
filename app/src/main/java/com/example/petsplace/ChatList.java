package com.example.petsplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.petsplace.adapters.ChatDataAdapter;
import com.example.petsplace.adapters.FriendListDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.auxiliary.MessageClass;
import com.example.petsplace.auxiliary.UserInformation;
import com.example.petsplace.fragments.lists.FriendRequestsListFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatList extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private ArrayList<String> chatsList = new ArrayList<String>();
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.chatList);
        searchBar = findViewById(R.id.searchBar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getChats();
    }



    protected void getChats(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(UserInformation.username).child("chats_names");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatDataAdapter adapter = new ChatDataAdapter(this,chatsList,this);
        recyclerView.setAdapter(adapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msgFromDB = snapshot.getKey();
                chatsList.add(msgFromDB);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatsList.remove(chatsList.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                chatsList.remove(chatsList.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatsList.remove(chatsList.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        search();
    }

    protected void search(){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSearch();
            }
        });
    }

    protected void updateSearch(){
        String search = searchBar.getText().toString();

        if (search.length() == 0){
            ChatDataAdapter adapter = new ChatDataAdapter(getApplicationContext(),chatsList,this);
            recyclerView.setAdapter(adapter);
            recyclerView.invalidate();
        }
        else {
            ArrayList<String> newChats = new ArrayList<String>();

            for (String friend : chatsList ){
                if (friend.startsWith(search)) {
                    newChats.add(friend);
                }
            }

            ChatDataAdapter newAdapter = new ChatDataAdapter(getApplicationContext(),newChats,this);
            recyclerView.setAdapter(newAdapter);
            recyclerView.invalidate();
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChatList.this,Chat.class);
        intent.putExtra("chat", chatsList.get(position));
        startActivity(intent);

        //Логирование имени пользователя. Почему бы и нет?
        Log.d("MyTAG",UserInformation.username);
    }
}