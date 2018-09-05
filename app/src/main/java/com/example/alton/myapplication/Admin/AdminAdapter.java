package com.example.alton.myapplication.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alton.myapplication.Models.User;
import com.example.alton.myapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alton on 2/8/2018.
 */

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder>{

   private Context context1;
    private List<User> uploads;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    AlertDialog.Builder demo;


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName,t2,t3;


        public ViewHolder(View itemView){
            super(itemView);

            textViewName=(TextView)itemView.findViewById(R.id.textView1);
            t2=(TextView)itemView.findViewById(R.id.textView2);
            t3=(TextView)itemView.findViewById(R.id.text3);
        }
    }

    public AdminAdapter(Context context, List<User> uploads){
        this.uploads=uploads;
        this.context1=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.showuser_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final User upload=uploads.get(position);

        holder.textViewName.setText(upload.getFirstName());
       holder.t2.setText(upload.getLastName());
       holder.t3.setText(upload.getGender());



        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demo=new AlertDialog.Builder(context1);

                demo.setMessage("Do you want to delete this user");
                demo.setCancelable(false);
                demo.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                 databaseReference=firebaseDatabase.getReference("users");
                 databaseReference.addChildEventListener(new ChildEventListener() {
                     @Override
                     public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                         for(DataSnapshot ds:dataSnapshot.getChildren()){
                             String s1=ds.getValue(User.class).getFirstName();

                             if(s1==holder.textViewName.getText()){
                                 ds.getRef().removeValue();
                                 Toast.makeText(context1, s1+" successfully deleted", Toast.LENGTH_SHORT).show();

                             }

                         }
                     }

                     @Override
                     public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                         for(DataSnapshot ds:dataSnapshot.getChildren()){
                             String s1=ds.getValue(User.class).getFirstName();

                             if(s1==holder.textViewName.getText()){
                                 ds.getRef().removeValue();
                                 Toast.makeText(context1, s1+" successfully deleted", Toast.LENGTH_SHORT).show();

                             }

                         }

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
                AlertDialog alert=demo.create();
                alert.show();
            }

        });

    }




    @Override
    public int getItemCount() {
        return uploads.size();
    }
}
