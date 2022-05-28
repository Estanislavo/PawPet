package com.example.petsplace.fragments.missing;

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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.petsplace.activities.ChatList;
import com.example.petsplace.activities.MissingAdd;
import com.example.petsplace.R;
import com.example.petsplace.adapters.MissingDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.auxiliary.HelperClass;
import com.example.petsplace.auxiliary.Upload;
import com.example.petsplace.auxiliary.UserInformation;
import com.example.petsplace.fragments.profiles.YourProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MissingShowFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private ArrayList<Upload> uploads;
    private ArrayList<Upload> uploadsStart;
    private DatabaseReference mDatabaseReference;
    private MissingDataAdapter adapter;
    private ProgressBar progressBar;
    private View view;
    private String username;
    private FloatingActionButton floatingActionButton;
    private EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_missing_show, container, false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("missing");
        uploads = new ArrayList<Upload>();
        uploadsStart = uploads;

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom);
        searchBar = view.findViewById(R.id.searchBar);
        floatingActionButton = view.findViewById(R.id.floatingAdd);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.missingList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MissingDataAdapter(getActivity(), uploads, this);
        recyclerView.setAdapter(adapter);

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Upload upload = snapshot.getValue(Upload.class);
                uploads.add(upload);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Upload upload = snapshot.getValue(Upload.class);
                uploads.add(upload);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Upload upload = snapshot.getValue(Upload.class);
                uploads.remove(upload);
                adapter.notifyDataSetChanged(); }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (HelperClass.hasConnection(getContext())) {
                    if (item.getTitle().equals(getResources().getString(R.string.encyclopedia))) {
                        Navigation.findNavController(view).navigate(R.id.action_nav_missingShow_to_nav_arcticleList);
                    } else if (item.getTitle().equals(getResources().getString(R.string.messenger))) {
                        Intent intent = new Intent(getActivity(), ChatList.class);
                        startActivity(intent);
                    }
                    return true;
                }

                else{
                    createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                    return  true;
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MissingAdd.class);
                startActivity(intent);
            }
        });

        search();

        return view;
    }

    @Override
    public void onItemClick(int position) {
        if (!MissingDataAdapter.goToUsername.equals(UserInformation.username)) {
            Log.d("myTAG","ВАШЕ ИМЯ:"+MissingDataAdapter.goToUsername);
            YourProfileFragment.userClick = MissingDataAdapter.goToUsername;
            Navigation.findNavController(view).navigate(R.id.action_nav_missingShow_to_nav_usernameProfile);
        }
        else{
            YourProfileFragment.userClick = MissingDataAdapter.goToUsername;
            Navigation.findNavController(view).navigate(R.id.action_nav_missingShow_to_nav_yourProfile);
        }
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
            uploads = uploadsStart;
            MissingDataAdapter adapter = new MissingDataAdapter(getActivity(),uploads,this);
            recyclerView.setAdapter(adapter);
            recyclerView.invalidate();
        }
        else {
            ArrayList<Upload> newUploads = new ArrayList<Upload>();

            for (Upload upload : uploads ){
                if (upload.getText().toLowerCase().contains(search)) {
                    newUploads.add(upload);
                }
            }
            uploads = newUploads;

            MissingDataAdapter newAdapter = new MissingDataAdapter(getActivity(),uploads,this);
            recyclerView.setAdapter(newAdapter);
            recyclerView.invalidate();
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