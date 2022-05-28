package com.example.petsplace.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.petsplace.R;
import com.example.petsplace.auxiliary.UserInformation;
import com.example.petsplace.fragments.lists.AllUsersListFragment;
import com.example.petsplace.fragments.lists.FriendsListFragment;
import com.example.petsplace.fragments.profiles.UsernameProfileFragment;
import com.example.petsplace.fragments.profiles.YourProfileFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petsplace.databinding.ActivityProfileBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private Fragment fragment;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        Log.d("MyTAG","Ваш юзернейм:"+UserInformation.username);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarProfile.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_usernameProfile, R.id.nav_yourProfile, R.id.nav_missingShow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_profile);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);

        TextView textView = findViewById(R.id.headerUsername);
        textView.setText(UserInformation.username);
        CircleImageView profileImage = findViewById(R.id.imageView);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(UserInformation.username);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String upload = snapshot.getValue(String.class);

                Picasso.with(getApplicationContext())
                        .load(upload)
                        .into(profileImage);
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
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_profile);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}