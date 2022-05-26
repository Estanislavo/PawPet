package com.example.petsplace.animal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.petsplace.R;
import com.example.petsplace.animal.AnimalCreate;
import com.example.petsplace.auxiliary.Animal;
import com.example.petsplace.auxiliary.UserInformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimalName extends AppCompatActivity {

    private EditText name,age;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_name);

        String petType = getIntent().getStringExtra("petType");

        name = findViewById(R.id.petName);
        age = findViewById(R.id.petAge);

        confirm = findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length() != 0) {
                    String namePet = name.getText().toString();
                    String agePet = age.getText().toString();
                    Animal animal = new Animal(petType, namePet, agePet);

                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users")
                            .child(UserInformation.username)
                            .child("pets");
                    dbRef.push().setValue(animal)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("MyTAG", "Питомец успешно добавлен");
                                    Intent intent = new Intent(getApplicationContext(), AnimalCreate.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finishAfterTransition();
                                }
                            });
                }
            }
        });
    }
}