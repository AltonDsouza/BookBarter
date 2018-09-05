package com.example.alton.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.sections.ImageAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alton on 2/25/2018.
 */

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder> {

    private Context context;
    private CharSequence editPicOptions[];
    AlertDialog.Builder builder;
    private List<BookOwn> uploads;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public MyBookAdapter(Context context, List<BookOwn> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final BookOwn upload = uploads.get(position);

        holder.textViewName.setText(upload.getBookname());
        holder.cg.setText(upload.getCategory());

        Glide.with(context).load(upload.getPicUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editPicOptions = new CharSequence[]{"Edit Book", "Remove Book"};
                builder = new AlertDialog.Builder(context);
                builder.setTitle("Your Books");
                builder.setCancelable(true);
                builder.setItems(editPicOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(context, EditBooks.class);
                            intent.putExtra("image", upload.getPicUrl());
                            intent.putExtra("bookname", upload.getBookname());
                            intent.putExtra("category", upload.getCategory());
                            context.startActivity(intent);
                        } else if (i == 1) {
                            databaseReference = firebaseDatabase.getReference("books");
                            Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


       /* if(getItemCount()==0){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setMessage("No books added yet!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    context.startActivity(new Intent(context, HomePageActivity.class));
                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return ;
        }
        */

    }

    @Override
    public int getItemCount() {
     return  uploads.size();

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
