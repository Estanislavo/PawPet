package com.example.petsplace.animal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.petsplace.activities.Profile;
import com.example.petsplace.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnimalCreate extends AppCompatActivity {

    private TextView tv;
    private TextView newPet;
    private CircleImageView image;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_create);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        anim = AnimationUtils.loadAnimation(this,R.anim.for_text_view);
        tv = (TextView) findViewById(R.id.congrats);
        newPet = findViewById(R.id.congratsPet);
        image = findViewById(R.id.circleImage);

        switch (type){
            case ("кошка"):
                image.setImageResource(R.drawable.kitty);
                break;
            case ("собака"):
                image.setImageResource(R.drawable.puppy);
                break;
            case ("птица"):
                image.setImageResource(R.drawable.bird);
                break;
            case ("рыба"):
                image.setImageResource(R.drawable.fish);
                break;
            case ("грызун"):
                image.setImageResource(R.drawable.rabbit);
                break;
            case ("экзотический"):
                image.setImageResource(R.drawable.lion);
                break;
        }
        newPet.startAnimation(anim);
        tv.startAnimation(anim);
        image.startAnimation(anim);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAfterTransition();
            }
        });
    }
}