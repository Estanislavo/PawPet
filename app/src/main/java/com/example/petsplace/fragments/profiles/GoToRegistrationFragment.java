package com.example.petsplace.fragments.profiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petsplace.R;
import com.example.petsplace.Registration;
import com.google.firebase.auth.FirebaseAuth;


public class GoToRegistrationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_go_to_registration, container, false);


        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), Registration.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return view;
    }
}