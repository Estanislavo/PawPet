package com.example.petsplace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petsplace.R;
import com.example.petsplace.auxiliary.HelperClass;
import com.example.petsplace.auxiliary.ProfilePictureUpload;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Registration extends AppCompatActivity {


    private Button registration, entry;
    private EditText usernameText, emailText, passwordText;
    private TextView reset;

    private DatabaseReference usersDatabase;
    private FirebaseAuth auth;
    private Context context;

    public static final String SHARED_PREFERENCES = "Shared_Preferences";
    public static String usernameS;
    public static String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = getApplicationContext();

        loadData();

        reset = findViewById(R.id.reset);
        registration = findViewById(R.id.registaration);
        entry = findViewById(R.id.entry);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.Email);
        passwordText = findViewById(R.id.editpass);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(context, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAfterTransition();
            }
            else{
                //Intent intent = new Intent(context, Profile.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                //finishAfterTransition();
                Intent intent = new Intent(context, MailConfirm.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAfterTransition();
            }
        }

        else{
            init();
        }
    }

    private void createUser() {
        if (emailText.getText().toString().contains("@")
                && emailText.getText().toString().length() != 1
                && passwordText.getText().toString().length() != 0
                && usernameText.getText().toString().length() != 0) {
            auth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();

                                registration.setClickable(true);
                                saveData();

                                addUser();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                createSnackbarWithText(R.string.verify_email,R.string.verify_email);
                                            }
                                        });
                            } else {
                                createSnackbarWithText(R.string.unable_register,R.string.check_nickname);
                                registration.setClickable(true);
                            }

                        }
                    });
        }
        else {
            emailText.setText("");
            createSnackbarWithText(R.string.wrong_format,R.string.wrong_format_text);
            registration.setClickable(true);
        }
    }

    private void authenticationUser() {
            if (emailText.getText().toString().contains("@")
                    && emailText.getText().toString().length() != 1
                    && passwordText.getText().toString().length() != 0
                    && usernameText.getText().toString().length() != 0) {
                auth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                saveData();
                                if (task.isSuccessful()) {
                                    UserInformation.username = usernameText.getText().toString();

                                    Intent intent = new Intent(context, Profile.class);
                                    startActivity(intent);
                                    finishAfterTransition();
                                } else {
                                    createSnackbarWithText(R.string.wrong_format,R.string.try_again);
                                    entry.setClickable(true);
                                }
                            }
                        });
            } else {
                createSnackbarWithText(R.string.something_went_wrong,R.string.try_again);
                entry.setClickable(true);
            }
    }

    private void addUser() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(usernameText.getText().toString());
            myRef.child("email").push().setValue(emailText.getText().toString());

            DatabaseReference myREF = FirebaseDatabase.getInstance().getReference("users_list");
            myREF.push().setValue(usernameText.getText().toString());

            DatabaseReference myRefа = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(usernameText.getText().toString());

            ProfilePictureUpload upload = new ProfilePictureUpload("https://firebasestorage.googleapis.com/v0/b/pawpet-42870.appspot.com/o/profilePhotos%2F906045-fb.jpg?alt=media&token=df1ced20-a04e-477e-8ac7-1de2e5e33f64");
            myRefа.setValue(upload);

            registration.setClickable(true);
    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(usernameS,usernameText.getText().toString());
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        username = sharedPreferences.getString(usernameS,"undefined");
    }

    public void createSnackbarWithText(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void init(){
        FirebaseUser isAuthorized = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance();
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HelperClass.hasConnection(context)){
                    createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                }
                else {
                    registration.setClickable(false);
                    createUser();
                }

            }
        });

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!HelperClass.hasConnection(getApplicationContext())){
                    createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                }
                else {
                    entry.setClickable(false);
                    authenticationUser();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailText.getText().toString().length() != 0) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailText.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    createSnackbarWithText(R.string.suc_res,R.string.email_send);
                                }
                            });
                }
                else{
                    createSnackbarWithText(R.string.recovery,R.string.enter_email_rec);
                }
            }
        });

    }

}