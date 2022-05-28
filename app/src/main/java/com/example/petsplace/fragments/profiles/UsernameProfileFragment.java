package com.example.petsplace.fragments.profiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.petsplace.activities.Chat;
import com.example.petsplace.activities.ChatList;
import com.example.petsplace.R;
import com.example.petsplace.adapters.FriendsDataAdapter;
import com.example.petsplace.adapters.PetsDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.auxiliary.Animal;
import com.example.petsplace.auxiliary.HelperClass;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsernameProfileFragment extends Fragment implements RecyclerViewInterface {

    private CircleImageView profilePicture;
    private TextView username;
    private TextView petsCount;
    private TextView friendsCount;
    private RecyclerView friendList;
    private RecyclerView petList;
    private String mineUsername;
    private Button write;
    private ArrayList<String> friends;
    private ArrayList<Animal> pets;
    private FriendsDataAdapter adapter;
    private View inflatedView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_username_profile, container, false);
        init();
        return inflatedView;
    }

    private void init(){
        Log.d("MyTAG","Ваше имя:"+mineUsername);
        profilePicture = inflatedView.findViewById(R.id.profilePicture);
        friends = new ArrayList<String>();
        pets = new ArrayList<Animal>();

        mineUsername = YourProfileFragment.userClick;

        friendList = inflatedView.findViewById(R.id.friendsList);
        BottomNavigationView bottomNavigationView = inflatedView.findViewById(R.id.bottom);

        FriendsDataAdapter adapter = new FriendsDataAdapter(getActivity(),friends,this);

        friendList.setAdapter(adapter);
        friendList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        friendsCount = inflatedView.findViewById(R.id.friendsCount);
        petsCount = inflatedView.findViewById(R.id.petsCount);

        friendsCount.setText(String.valueOf(friends.size()));
        petsCount.setText(String.valueOf(pets.size()));

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(mineUsername).child("friends");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friends.add(0,snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friends.remove(friends.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friends.remove(friends.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friends.remove(friends.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(mineUsername);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String upload = snapshot.getValue(String.class);

                    Log.d("MyTAG", "Ваша ссылка:" + upload);

                    Picasso.with(getActivity())
                            .load(upload)
                            .into(profilePicture);
                }
                else{
                }
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



        petList = inflatedView.findViewById(R.id.petsRecyclerList);
        PetsDataAdapter adapterPet = new PetsDataAdapter(getActivity(),pets,this);
        petList.setAdapter(adapterPet);
        petList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference dbRefPet = FirebaseDatabase.getInstance().getReference("users").child(mineUsername).child("pets");

        dbRefPet.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pets.add(0,snapshot.getValue(Animal.class));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pets.remove(pets.indexOf(snapshot.getValue(Animal.class).getName()));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                pets.remove(pets.indexOf(snapshot.getValue(Animal.class).getName()));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pets.remove(pets.indexOf(snapshot.getValue(Animal.class).getName()));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        username = inflatedView.findViewById(R.id.profileUsername);
        username.setText(mineUsername);

        write = inflatedView.findViewById(R.id.buttonWrite);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Chat.class);
                intent.putExtra("chat",mineUsername);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().equals(getResources().getString(R.string.encyclopedia))){
                    if (HelperClass.hasConnection(getContext())) {
                        Navigation.findNavController(inflatedView).navigate(R.id.action_nav_usernameProfile_to_nav_arcticleList);
                    }
                    else{
                        createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                    }

                }
                else if (item.getTitle().equals(getResources().getString(R.string.messenger))){
                    if (HelperClass.hasConnection(getContext())) {
                        Intent intent = new Intent(getActivity(), ChatList.class);
                        startActivity(intent);
                    }
                    else{
                        createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                    }
                }
                else if (item.getTitle().equals(getResources().getString(R.string.losts))){
                    if (HelperClass.hasConnection(getContext())) {
                        Navigation.findNavController(inflatedView).navigate(R.id.action_nav_usernameProfile_to_nav_missingShow);
                    }
                    else{
                        createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                    }
                }
                return true;
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        if (PetsDataAdapter.isGoToPet){
            PetsDataAdapter.isGoToPet = false;
        }

        else if (FriendsDataAdapter.goToUsername.equals(UserInformation.username)){
            Navigation.findNavController(inflatedView).navigate(R.id.action_nav_usernameProfile_to_nav_yourProfile);
        }

        else if (YourProfileFragment.isHuman){
            YourProfileFragment.isHuman = false;
            Log.d("MyTAG",mineUsername);
            YourProfileFragment.userClick = FriendsDataAdapter.goToUsername;
            Log.d("MyTAG",mineUsername);
            init();
        }
    }

    public void createSnackbarWithText(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}