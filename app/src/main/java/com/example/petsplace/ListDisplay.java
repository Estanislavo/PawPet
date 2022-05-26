package com.example.petsplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.petsplace.fragments.lists.AllUsersListFragment;
import com.example.petsplace.fragments.lists.FriendsListFragment;

public class ListDisplay extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        String path = intent.getStringExtra("isGlobalSearch");

        Log.d("MyTAG","Now we are here");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (path.equals("true")){
            fragment = new AllUsersListFragment();
            ft.replace(R.id.fragmentView,fragment);
        }

        else{
            fragment = new FriendsListFragment();
            ft.replace(R.id.fragmentView,fragment);
        }
    }
}