package com.example.petsplace.fragments.lists;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.petsplace.Chat;
import com.example.petsplace.R;
import com.example.petsplace.auxiliary.UserInformation;
import com.example.petsplace.adapters.FriendListDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.fragments.profiles.YourProfileFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class FriendsListFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private HashMap<String, String> ages;
    private ArrayList<String> friends;
    private EditText searchBar;
    private DatabaseReference db;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_all_users_list, container, false);
        view = inflatedView;
        // Inflate the layout for this fragment

        searchBar = inflatedView.findViewById(R.id.searchBar);

        friends = new ArrayList<String>();

        RelativeLayout relativeList = inflatedView.findViewById(R.id.relativeList);

        recyclerView = inflatedView.findViewById(R.id.listOfFriends);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FriendListDataAdapter adapter = new FriendListDataAdapter(getActivity(),friends,this);
        recyclerView.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference("users").child(UserInformation.username).child("friends");

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msgFromDB = snapshot.getValue(String.class);
                friends.add(msgFromDB);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        search();


        return inflatedView;
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
            FriendListDataAdapter adapter = new FriendListDataAdapter(getActivity(),friends,this);
            recyclerView.setAdapter(adapter);
            recyclerView.invalidate();
        }
        else {
            ArrayList<String> newFriends = new ArrayList<String>();

            for (String friend : friends ){
                if (friend.startsWith(search)) {
                    newFriends.add(friend);
                }
            }

            FriendListDataAdapter newAdapter = new FriendListDataAdapter(getActivity(),newFriends,this);
            recyclerView.setAdapter(newAdapter);
            recyclerView.invalidate();
        }

    }

    @Override
    public void onItemClick(int position) {
        if (FriendListDataAdapter.pressedAll == 0){
            Intent intent = new Intent(getActivity(), Chat.class);
            intent.putExtra("chat", FriendListDataAdapter.goToUsername);
            startActivity(intent);
        }
        else if (FriendListDataAdapter.pressedAll == 1){
            //Аналогично, добавить
            YourProfileFragment.userClick = FriendListDataAdapter.goToUsername;
            Navigation.findNavController(view).navigate(R.id.action_nav_globalList_to_nav_usernameProfile);
        }
    }
}