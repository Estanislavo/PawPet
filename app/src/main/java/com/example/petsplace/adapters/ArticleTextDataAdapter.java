/*
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

public class ArticleTextDataAdapter extends RecyclerView.Adapter<ArticleTextDataAdapter.ArticleTextViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;

    ArrayList<String> texts;


    public ArticleTextDataAdapter(Context context, ArrayList<String> messages,RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.texts = messages;
        this.context = context;
    }


    @NonNull
    @Override
    public ArticleTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_article_text, parent, false);

        return new ArticleTextDataAdapter.ArticleTextViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleTextViewHolder holder, int position) {

        String text = texts.get(position);
        holder.articleName.setText(text);
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    protected static class ArticleTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView articleName;


        public ArticleTextViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            articleName = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
 */
