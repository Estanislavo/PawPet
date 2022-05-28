package com.example.petsplace.fragments.editprofile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petsplace.R;
import com.example.petsplace.auxiliary.ProfilePictureUpload;
import com.example.petsplace.auxiliary.Upload;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private CircleImageView changeIcon;
    private TextView username;
    private TextView city;
    private Button confirm;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        username = inflatedView.findViewById(R.id.username);
        city = inflatedView.findViewById(R.id.city);

        username.setText(UserInformation.username);
        city.setText("Moscow");

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(UserInformation.username);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String upload = snapshot.getValue(String.class);
                Picasso.with(getActivity())
                        .load(upload)
                        .fit()
                        .centerCrop()
                        .into(changeIcon);
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

        mStorageRef = FirebaseStorage.getInstance().getReference("profilePhotos");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("profilePhotos");

        changeIcon = inflatedView.findViewById(R.id.changeProfileIcon);
        confirm = inflatedView.findViewById(R.id.confirm);

        changeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(mImageUri);
            }
        });





        return inflatedView;
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtensions(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(Uri uri){
        if (uri != null){
            StorageReference imageReference = mStorageRef.child(UserInformation.username + "." + getFileExtensions(uri));
            imageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(),"Success!",Toast.LENGTH_SHORT).show();

                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photoStringLink = uri.toString();

                                    ProfilePictureUpload upload = new ProfilePictureUpload(photoStringLink);
                                    String keyID = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(UserInformation.username).setValue(upload);
                                }
                            });


                            //Intent intent = new Intent(getApplicationContext(),MissingShow.class);
                            //startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        else{
            Toast.makeText(getActivity(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(getActivity()).load(mImageUri).into(changeIcon);
            changeIcon.setImageURI(mImageUri);
        }
    }
}