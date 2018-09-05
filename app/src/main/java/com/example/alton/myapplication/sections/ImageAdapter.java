package com.example.alton.myapplication.sections;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.R;

import java.util.List;

/**
 * Created by alton on 2/3/2018.
 */

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private Context context;
        private List<BookOwn> uploads;

        public ImageAdapter(Context context, List<BookOwn> uploads){
            this.uploads=uploads;
            this.context=context;
        }

        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image,parent,false);
            ViewHolder viewHolder=new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {

            BookOwn upload=uploads.get(position);

            holder.textViewName.setText(upload.getBookname());
            holder.cg.setText(upload.getCategory());

            Glide.with(context).load(upload.getPicUrl()).into(holder.imageView);

             }

        @Override
        public int getItemCount() {
            return uploads.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView textViewName,cg;
            public ImageView imageView;

            public ViewHolder(View itemView){
                super(itemView);

                textViewName=(TextView)itemView.findViewById(R.id.textViewName);
                cg=(TextView)itemView.findViewById(R.id.categoryname);
                imageView=(ImageView)itemView.findViewById(R.id.imageViewbook);
            }
        }
    }

