package com.example.petsplace.fragments.lists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.petsplace.activities.ArcticleShow;
import com.example.petsplace.activities.ChatList;
import com.example.petsplace.auxiliary.HelperClass;
import com.example.petsplace.viewmodel.NameViewModel;
import com.example.petsplace.R;
import com.example.petsplace.adapters.ArticleDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.auxiliary.ArticleHelper;
import com.example.petsplace.auxiliary.ArticleIntroduction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ArcticleListFragment extends Fragment implements RecyclerViewInterface {

    public static ArrayList<ArticleIntroduction> articles;
    private ArrayList<ArticleIntroduction> articlesStart;
    private RecyclerView recycler;
    private ArticleDataAdapter adapter;
    private EditText searchBar;
    private ProgressBar progressBar;
    private View view;
    private RelativeLayout relativeLayout;
    private  NameViewModel model;
    private BottomNavigationView bottomNavigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_arcticle_list, container, false);




        model = new ViewModelProvider(this).get(NameViewModel.class);
        model.getCurrentName().observe(getViewLifecycleOwner(), users -> {
            // update UI
            if (articles != null) {

                Thread threadInit = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                });
                threadInit.start();

                downloadArticles();
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                articles = ArticleHelper.getArticleIntroductionList();
                articlesStart = articles;
                model.setCurrentName(articles);
            }
        });

        thread.start();

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ArcticleShow.class);
        intent.putExtra("url",articles.get(position).getIntroductionImage());
        intent.putExtra("texts",ArticleHelper.getArticle(articles.get(position).getArticleUrl()));
        startActivity(intent);
    }

    private void init(){
        progressBar = view.findViewById(R.id.progress_bar);
        bottomNavigationView = view.findViewById(R.id.bottom);
    }

    private void downloadArticles(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String texts = ArticleHelper.getArticle(articles.get(1).getArticleUrl());
        progressBar.setVisibility(View.INVISIBLE);
        //String texts = ArticleHelper.getArticle(articles.get(1).getArticleUrl());

        //progressBar.setVisibility(View.INVISIBLE);

        searchBar = view.findViewById(R.id.searchBar);
        recycler = view.findViewById(R.id.recycler);
        recycler.hasFixedSize();
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Ва

        adapter = new ArticleDataAdapter(getActivity(), articles, this);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (HelperClass.hasConnection(getContext())) {
                    if (item.getTitle().equals(getResources().getString(R.string.encyclopedia))) { }

                    else if (item.getTitle().equals(getResources().getString(R.string.messenger))) {
                        Intent intent = new Intent(getActivity(), ChatList.class);
                        startActivity(intent);
                    }

                    else if (item.getTitle().equals(getResources().getString(R.string.losts))) {
                        Navigation.findNavController(view).navigate(R.id.action_nav_arcticleList_to_nav_missingShow);
                    }
                    return true;
                }
                else{
                    createSnackbarWithText(R.string.no_ethernet,R.string.try_again);
                    return false;
                }
            }
        });

        search();
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
            articles = articlesStart;
            ArticleDataAdapter adapter = new ArticleDataAdapter(getActivity(),articles,this);
            recycler.setAdapter(adapter);
            recycler.invalidate();
        }
        else {
            ArrayList<ArticleIntroduction> newArticles = new ArrayList<ArticleIntroduction>();

            for (ArticleIntroduction article : articles ){
                if (article.getIntroductionText().toLowerCase().contains(search)) {
                    newArticles.add(article);
                }
            }
            articles = newArticles;

            ArticleDataAdapter newAdapter = new ArticleDataAdapter(getActivity(),articles,this);
            recycler.setAdapter(newAdapter);
            recycler.invalidate();
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