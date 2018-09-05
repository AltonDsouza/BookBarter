package com.example.alton.myapplication.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.alton.myapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alton on 2/11/2018.
 */

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.ViewHolder>{


    private Context context;
    private List<BookOwn> uploads;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    AlertDialog.Builder demo;



    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName,cg;
        public ImageView imageView,overflow;

        public ViewHolder(View itemView){
            super(itemView);

            textViewName=(TextView)itemView.findViewById(R.id.textViewName);
            cg=(TextView)itemView.findViewById(R.id.categoryname);
            imageView=(ImageView)itemView.findViewById(R.id.imageViewbook);
            overflow=(ImageView)itemView.findViewById(R.id.overflow2);
        }
    }

    public DeleteAdapter(Context context, List<BookOwn> uploads){
        this.uploads=uploads;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.del_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final BookOwn upload=uploads.get(position);



        holder.textViewName.setText(upload.getBookname());
        holder.cg.setText(upload.getCategory());


        Glide.with(context).load(upload.getPicUrl()).into(holder.imageView);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                demo=new AlertDialog.Builder(context);

                demo.setMessage("Do you want to delete this book");
                demo.setCancelable(false);
                demo.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference=firebaseDatabase.getReference("books");
                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String s1=String.valueOf(ds.getValue(BookOwn.class).getBookname());
                                    if(s1==holder.textViewName.getText()){
                                        ds.getRef().removeValue();
                                        uploads.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }



                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {


                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
                demo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog=demo.create();
                alertDialog.show();
            }


                });
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.del_menu, popup.getMenu());
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
                case R.id.action_delete:


                    return true;
                case R.id.action_play:
                    Toast.makeText(context, "Description", Toast.LENGTH_SHORT).show();
                    return true;


                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }



}
