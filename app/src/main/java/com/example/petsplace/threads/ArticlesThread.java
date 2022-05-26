package com.example.petsplace.threads;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.view.accessibility.AccessibilityEventCompat;

import com.example.petsplace.ArcticleShow;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.auxiliary.ArticleHelper;
import com.example.petsplace.auxiliary.ArticleIntroduction;
import com.example.petsplace.fragments.lists.ArcticleListFragment;

import java.util.ArrayList;

public class ArticlesThread extends AsyncTask {

    ArrayList<ArticleIntroduction> articles;


    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
