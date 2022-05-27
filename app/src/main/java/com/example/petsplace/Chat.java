package com.example.petsplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.TimeAnimator;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petsplace.adapters.MessageDataAdapter;
import com.example.petsplace.auxiliary.HelperClass;
import com.example.petsplace.auxiliary.MessageClass;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private CircleImageView friendPictureChat;
    private EditText messageEdit;
    private RecyclerView recyclerView;
    private ImageButton send;
    private DatabaseReference myRef;
    private DatabaseReference friendRef;
    private ArrayList<MessageClass> messagesList;
    private TextView friendName;
    private String friendUsername;
    private NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        friendUsername = intent.getStringExtra("chat");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        friendName = findViewById(R.id.usernameMessage);
        friendName.setText(friendUsername);


        friendPictureChat = findViewById(R.id.friendPictureChat);


        if (HelperClass.hasConnection(getApplicationContext())) {
            init();
        }
        else{
            createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
        }

    }

    protected void getMessages(String myUsername, String friiendUsername) {

        messagesList = new ArrayList<MessageClass>();

        if (myUsername != null && friiendUsername != null) {
            myRef = FirebaseDatabase.getInstance().getReference("users").child(UserInformation.username).child("chats").child(friiendUsername);
            friendRef = FirebaseDatabase.getInstance().getReference("users").child(friendUsername).child("chats").child(myUsername);

            recyclerView = findViewById(R.id.listOfMessages);
            messageEdit = findViewById(R.id.messageComplete);
            send = findViewById(R.id.sendMessage);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageEdit.length() != 0) {
                        MessageClass message = new MessageClass(UserInformation.username, messageEdit.getText().toString());
                        myRef.push().setValue(message);
                        friendRef.push().setValue(message);
                        FirebaseDatabase.getInstance().getReference("users").child(UserInformation.username).child("chats_names").child(friiendUsername).setValue("1");
                        FirebaseDatabase.getInstance().getReference("users").child(friiendUsername).child("chats_names").child(UserInformation.username).setValue("1");
                        messageEdit.setText("");
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Введите сообщение", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MessageDataAdapter adapter = new MessageDataAdapter(this, messagesList);
            recyclerView.setAdapter(adapter);

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    MessageClass msgFromDB = snapshot.getValue(MessageClass.class);
                    messagesList.add(msgFromDB);
                    adapter.notifyDataSetChanged();
                    recyclerView.invalidate();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    messagesList.remove(messagesList.indexOf(snapshot.getValue(MessageClass.class)));
                    adapter.notifyDataSetChanged();
                    recyclerView.invalidate();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    messagesList.remove(messagesList.indexOf(snapshot.getValue(MessageClass.class)));
                    adapter.notifyDataSetChanged();
                    recyclerView.invalidate();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    messagesList.remove(messagesList.indexOf(snapshot.getValue(MessageClass.class)));
                    adapter.notifyDataSetChanged();
                    recyclerView.invalidate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child("profilePhotos")
                .child(friendUsername);
        myRef.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String upload = snapshot.getValue(String.class);

                Picasso.with(getApplicationContext())
                        .load(upload)
                        .fit()
                        .centerCrop()
                        .into(friendPictureChat);
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

        getMessages(UserInformation.username, friendUsername);
    }

    public void createSnackbarWithText(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

}