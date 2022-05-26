package com.example.petsplace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petsplace.R;
import com.example.petsplace.auxiliary.ArticleIntroduction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleDataAdapter extends RecyclerView.Adapter<ArticleDataAdapter.ArticleViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    public static String goToUsername = "none";
    private Context context;
    private final int userNotMine = 0;
    private final int userMine = 1;

    private int isFriend = 0;

    ArrayList<ArticleIntroduction> articles;
    LayoutInflater inflater;


    public ArticleDataAdapter(Context context, ArrayList<ArticleIntroduction> messages,RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.articles = messages;
        this.context = context;
    }


    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);

        return new ArticleViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        String text = articles.get(position).getIntroductionText();
        holder.articleName.setText(text);

        Picasso.with(context)
                .load(articles.get(position).getIntroductionImage())
                .fit()
                .centerCrop()
                .into(holder.articleImage);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    protected static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView articleName;
        ImageView articleImage;


        public ArticleViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            articleName = itemView.findViewById(R.id.articleTitleText);
            articleImage = itemView.findViewById(R.id.articleTitleImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUsername = articleName.getText().toString();
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });


        }
        @Override
        public void onClick(View view) {
        }
    }
}
