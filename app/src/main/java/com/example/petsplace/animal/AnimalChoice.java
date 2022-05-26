package com.example.petsplace.animal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.petsplace.R;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnimalChoice extends AppCompatActivity {

    private static String apply = "";

    private CircleImageView acceptA1;
    private CircleImageView acceptA2;
    private CircleImageView acceptA3;
    private CircleImageView acceptA4;
    private CircleImageView acceptA5;
    private CircleImageView acceptA6;

    private CardView a1;
    private CardView a2;
    private CardView a3;
    private CardView a4;
    private CardView a5;
    private CardView a6;

    private MaterialButton next;

    private String petType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_choice);

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        a5 = findViewById(R.id.a5);
        a6 = findViewById(R.id.a6);

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyTAG","Ваш новый питомец - "+petType);
                if (petType.equals("")){
                    Log.d("MyTAG","Питомец не выбран");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnimalChoice.this);
                    builder.setTitle("Питомец не выбран");
                    builder.setMessage("Пожалуйста, выберите питомца");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), AnimalName.class);
                    intent.putExtra("petType", petType);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAfterTransition();
                }
            }
        });

        acceptA1 = findViewById(R.id.acceptA1);
        acceptA2 = findViewById(R.id.acceptA2);
        acceptA3 = findViewById(R.id.acceptA3);
        acceptA4 = findViewById(R.id.acceptA4);
        acceptA5 = findViewById(R.id.acceptA5);
        acceptA6 = findViewById(R.id.acceptA6);

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply(acceptA1,"1",a1);
            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply(acceptA2,"2",a2);
            }
        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply(acceptA3,"3",a3);
            }
        });

        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply(acceptA4,"4",a4);
            }
        });

        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply(acceptA5,"5",a5);
            }
        });

        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply(acceptA6,"6",a6);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        apply = "";
    }

    protected void apply(CircleImageView accept, String ind, CardView a){
        if (apply.equals(ind)){
            apply = "";
            petType = "";
            Log.d("MyTAG",petType);
            accept.setImageResource(R.drawable.unaccept);

            a1.setAlpha(1F);
            a2.setAlpha(1F);
            a3.setAlpha(1F);
            a4.setAlpha(1F);
            a5.setAlpha(1F);
            a6.setAlpha(1F);
        }
        else if (apply == ""){
            apply = ind;
            accept.setImageResource(R.drawable.accept);

            a1.setAlpha(0.5F);
            a2.setAlpha(0.5F);
            a3.setAlpha(0.5F);
            a4.setAlpha(0.5F);
            a5.setAlpha(0.5F);
            a6.setAlpha(0.5F);

            a.setAlpha(1F);

            switch (ind){
                case ("1"):
                    petType = "кошка";
                    break;
                case ("2"):
                    petType = "собака";
                    break;
                case ("3"):
                    petType = "птица";
                    break;
                case ("4"):
                    petType = "рыба";
                    break;
                case ("5"):
                    petType = "грызун";
                    break;
                case ("6"):
                    petType = "экзотический";
                    break;
            }

        }
    }
}