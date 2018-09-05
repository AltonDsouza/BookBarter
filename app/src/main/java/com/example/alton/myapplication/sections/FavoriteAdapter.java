package com.example.alton.myapplication.sections;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by alton on 2/17/2018.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {


    private Context context;
    private List<BookOwn> uploads;
    AlertDialog.Builder builder;
    private CharSequence editPicOptions[];

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public FavoriteAdapter(Context context, List<BookOwn> uploads){
        this.uploads=uploads;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.favcard,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final BookOwn upload=uploads.get(position);

        holder.textViewName.setText(upload.getBookname());
        holder.cg.setText(upload.getAuthor());

        Glide.with(context).load(upload.getPicUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPicOptions= new CharSequence[] {"View Book", "Remove Book"};
                 builder = new AlertDialog.Builder(context);
                builder.setTitle("Favorites");
                builder.setCancelable(true);
                builder.setItems(editPicOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            Intent i=new Intent(context, BookDetails.class);
                            i.putExtra("bookname",upload.getBookname());
                            i.putExtra("author",upload.getAuthor());
                            i.putExtra("image",upload.getPicUrl());
                            context.startActivity(i);
                        }else if (which==1){
                            databaseReference=firebaseDatabase.getReference().child("favorites");

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        String s1=String.valueOf(ds.getValue(BookOwn.class).getBookname());
                                        if(s1==holder.textViewName.getText()){

                                            ds.getRef().removeValue();
                                            uploads.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
               AlertDialog alertDialog=builder.create();
               alertDialog.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    private void removeItem(BookOwn infoData) {

        int currPosition = uploads.indexOf(infoData);
        uploads.remove(currPosition);
        notifyItemRemoved(currPosition);
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName,cg;
        public ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);

            textViewName=(TextView)itemView.findViewById(R.id.titlefav);
            cg=(TextView)itemView.findViewById(R.id.countfav);
            imageView=(ImageView)itemView.findViewById(R.id.thumbnailfav);
        }
    }




}
