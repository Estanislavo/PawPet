package com.example.petsplace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petsplace.R;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArcticleShow extends AppCompatActivity implements RecyclerViewInterface {
    private ImageView imageA;
    private TextView textView;
    private String text = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcticle_show);

        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("url");
        text = intent.getStringExtra("texts");

        imageA = findViewById(R.id.imageA);
        textView = findViewById(R.id.text);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Picasso.with(getApplicationContext())
                .load(imageURL)
                .fit()
                .centerCrop()
                .into(imageA);


        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // ночная тема не активна, используется светлая тема
                textView.setText(text);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // ночная тема активна, и она используется
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setText(text);
                break;
        }

    }

    @Override
    public void onItemClick(int position) {

    }
}