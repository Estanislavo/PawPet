package com.example.petsplace.fragments.lists;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.petsplace.ArcticleShow;
import com.example.petsplace.Chat;
import com.example.petsplace.ChatList;
import com.example.petsplace.R;
import com.example.petsplace.adapters.ArticleDataAdapter;
import com.example.petsplace.adapters.ChatDataAdapter;
import com.example.petsplace.adapters.RecyclerViewInterface;
import com.example.petsplace.auxiliary.ArticleHelper;
import com.example.petsplace.auxiliary.ArticleIntroduction;
import com.example.petsplace.auxiliary.UserInformation;

import java.util.ArrayList;

public class ArcticleListFragment extends Fragment implements RecyclerViewInterface {

    public static ArrayList<ArticleIntroduction> articles;
    private ArrayList<ArticleIntroduction> articlesStart;
    private RecyclerView recycler;
    private ArticleDataAdapter adapter;
    private EditText searchBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_arcticle_list, container, false);

        searchBar = view.findViewById(R.id.searchBar);

        recycler = view.findViewById(R.id.recycler);
        recycler.hasFixedSize();
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Убирает запрет с использования интернета в общем потоке

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                articles = ArticleHelper.getArticleIntroductionList();
                articlesStart = articles;
            }
        });

        thread.run();
        //Ва

        adapter = new ArticleDataAdapter(getActivity(), articles, this);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        String texts = ArticleHelper.getArticle(articles.get(1).getArticleUrl());
        search();

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ArcticleShow.class);
        intent.putExtra("url",articles.get(position).getIntroductionImage());
        intent.putExtra("texts",ArticleHelper.getArticle(articles.get(position).getArticleUrl()));
        startActivity(intent);
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
}