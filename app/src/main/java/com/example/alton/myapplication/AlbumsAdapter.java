package com.example.alton.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.sections.Favorites;
import com.google.android.gms.common.api.GoogleApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alton on 1/9/2018.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private Context mContext;
    private List<BookOwn> albumList;
DatabaseReference mdatabase;
FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);
             title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
}}

   public AlbumsAdapter(Context mContext, List<BookOwn> albumList) {
        this.mContext =  mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BookOwn album = albumList.get(position);
        holder.title.setText(album.getBookname());
        holder.count.setText(album.getAuthor());

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getPicUrl()).into(holder.thumbnail);


      holder.thumbnail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             Intent i=new Intent(mContext,BookDetails.class);
             i.putExtra("image",album.getPicUrl());
             i.putExtra("bookname",album.getBookname());
             i.putExtra("author",album.getAuthor());

             mContext.startActivity(i);
         }
      });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdatabase=firebaseDatabase.getReference();
addFav(FirebaseAuth.getInstance().getCurrentUser().getEmail(),album.getBookname(),
        album.getAuthor(),album.getPicUrl());
                Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

 /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:

                    Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Description", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }

            }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void addFav(String userID, String bookname, String author, String pic) {
        BookOwn newUser = new BookOwn();
        newUser.setUid(userID);
        newUser.setPicUrl(pic);
        newUser.setBookname(bookname);
        newUser.setAuthor(author);
        mdatabase.child("favorites")
                .child(bookname).setValue(newUser);

    }

}

