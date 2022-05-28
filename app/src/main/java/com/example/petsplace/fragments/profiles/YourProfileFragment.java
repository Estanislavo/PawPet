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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petsplace.activities.ChatList;
import com.example.petsplace.activities.Registration;
import com.example.petsplace.R;
import com.example.petsplace.adapters.FriendsDataAdapter;
import com.example.petsplace.adapters.PetsDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.animal.AnimalChoice;
import com.example.petsplace.auxiliary.Animal;
import com.example.petsplace.auxiliary.HelperClass;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class YourProfileFragment extends Fragment implements RecyclerViewInterface {

    private TextView username;
    private TextView petsCount;
    private TextView friendsCount;
    private RecyclerView petList;
    private Button exit;
    private View view;
    private CircleImageView profilePicture;
    private BottomNavigationView bottomNavigationView;
    private String upload;
    private ImageView friendAddList;

    public static String userClick = "";
    public static boolean isHuman = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_your_profile, container, false);
        view = inflatedView;

        //HelperClass.changeLanguage(getActivity(),"en");

        ArrayList<String> friends = new ArrayList<String>();
        ArrayList<Animal> pets = new ArrayList<Animal>();

        friendsCount = inflatedView.findViewById(R.id.friendsCount);
        petsCount = inflatedView.findViewById(R.id.petsCount);
        profilePicture = inflatedView.findViewById(R.id.profilePicture);
        friendAddList = inflatedView.findViewById(R.id.friendAddList);

        bottomNavigationView = inflatedView.findViewById(R.id.bottom);
        friendsCount.setText(String.valueOf(friends.size()));
        petsCount.setText(String.valueOf(pets.size()));

        friends.add("+");
        pets.add(new Animal("-","-","-"));

        String mineUsername = UserInformation.username;

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().equals(getResources().getString(R.string.encyclopedia))){
                    if (HelperClass.hasConnection(getContext())) {
                        Navigation.findNavController(view).navigate(R.id.action_nav_yourProfile_to_nav_arcticleList);
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
                        Navigation.findNavController(view).navigate(R.id.action_nav_yourProfile_to_nav_missingShow);
                    }
                    else{
                        createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                    }
                }
                return true;
            }
        });


        RecyclerView friendList = inflatedView.findViewById(R.id.friendsList);
        FriendsDataAdapter adapter = new FriendsDataAdapter(getActivity(),friends,this);
        friendList.setAdapter(adapter);
        friendList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(mineUsername).child("friends");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friends.add(0,snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()-1));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friends.remove(friends.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()-1));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friends.remove(friends.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()-1));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                friends.remove(friends.indexOf(snapshot.getValue(String.class)));
                adapter.notifyDataSetChanged();
                friendsCount.setText(String.valueOf(friends.size()-1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
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
                petsCount.setText(String.valueOf(pets.size()-1));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pets.remove(pets.indexOf(snapshot.getValue(Animal.class).getName()));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()-1));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                pets.remove(pets.indexOf(snapshot.getValue(Animal.class).getName()));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()-1));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pets.remove(pets.indexOf(snapshot.getValue(Animal.class).getName()));
                adapterPet.notifyDataSetChanged();
                petsCount.setText(String.valueOf(pets.size()-1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        exit = inflatedView.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Registration.class);
                FirebaseAuth.getInstance().signOut();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finishAfterTransition();
            }
        });

        username = inflatedView.findViewById(R.id.profileUsername);
        username.setText(mineUsername);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(UserInformation.username);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                upload = snapshot.getValue(String.class);

                Picasso.with(getActivity())
                        .load(upload)
                        .into(profilePicture);
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

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        friendAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_yourProfile_to_nav_friendRequests);
            }
        });

        return inflatedView;
    }

    @Override
    public void onItemClick(int position) {

        if (PetsDataAdapter.goToPet == "-") {
            Intent intent  = new Intent(getActivity(), AnimalChoice.class);
            PetsDataAdapter.goToPet = "";
            startActivity(intent);
        }

        else if (PetsDataAdapter.isGoToPet){
            PetsDataAdapter.isGoToPet = false;
        }

        else if (FriendsDataAdapter.goToUsername == "+"){
            Navigation.findNavController(view).navigate(R.id.action_nav_yourProfile_to_nav_globalList);
            FriendsDataAdapter.goToUsername = "";

        }
        else {
            isHuman = true;
            userClick = FriendsDataAdapter.goToUsername;
            Navigation.findNavController(view).navigate(R.id.action_nav_yourProfile_to_nav_usernameProfile);

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