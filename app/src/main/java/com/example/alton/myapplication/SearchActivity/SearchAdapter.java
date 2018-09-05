package com.example.alton.myapplication.SearchActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alton.myapplication.BookDetails;
import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.R;

import java.util.ArrayList;

/**
 * Created by alton on 2/11/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    Context context;
    ArrayList<BookOwn> fullNameList;


    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView full_name, user_name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public SearchAdapter(Context context, ArrayList<BookOwn> fullNameList) {
        this.context = context;
        this.fullNameList = fullNameList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
    final BookOwn book=fullNameList.get(position);
        holder.full_name.setText(book.getBookname());
        holder.user_name.setText(book.getAuthor());
        Glide.with(context).load(book.getPicUrl()).into(holder.profileImage);

        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,BookDetails.class);
                i.putExtra("bookname",book.getBookname());
                i.putExtra("author",book.getAuthor());
                i.putExtra("image",book.getPicUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}
