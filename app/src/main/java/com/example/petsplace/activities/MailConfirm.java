package com.example.petsplace.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.petsplace.R;
import com.google.firebase.auth.FirebaseAuth;

public class MailConfirm extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_confirm);

        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAfterTransition();
                }

                else{
                    createSnackbarWithText(R.string.email_not_verified,R.string.verify_email);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAfterTransition();
            }
        });
    }

    public void init(){
        textView = findViewById(R.id.text);
        button = findViewById(R.id.check);
        exit = findViewById(R.id.exit);
    }

    public void createSnackbarWithText(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MailConfirm.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}