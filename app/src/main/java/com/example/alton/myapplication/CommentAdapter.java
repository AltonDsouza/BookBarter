package com.example.alton.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alton.myapplication.Models.Review;

import java.util.List;

/**
 * Created by alton on 2/15/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{


    private Context context;
    private List<Review> uploads;

    public CommentAdapter(Context context, List<Review> uploads){
        this.uploads=uploads;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_comment_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Review upload=uploads.get(position);

        holder.textViewName.setText(upload.getEmailId());
        holder.t2.setText(upload.getReview());

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName,t2;


        public ViewHolder(View itemView){
            super(itemView);

            textViewName=(TextView)itemView.findViewById(R.id.textViewEmailId);
            t2=(TextView)itemView.findViewById(R.id.textViewReview);
         }
    }
}
