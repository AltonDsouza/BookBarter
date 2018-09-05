package com.example.alton.myapplication.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.alton.myapplication.Models.User;
import com.example.alton.myapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminShowUsers extends AppCompatActivity {

    Button b1,b2,b3;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private List<User> uploads;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showusers);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog=new ProgressDialog(this);

        uploads=new ArrayList<>();
        adapter=new AdminAdapter(this,uploads);
        recyclerView.setAdapter(adapter);

        //displaying progress dialog while fetching images

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mDatabase=firebaseDatabase.getReference("users");
     mDatabase.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

             progressDialog.dismiss();


             for(DataSnapshot ds : dataSnapshot.getChildren())
             {
                 User m=new User();
                 m.setFirstName(ds.getValue(User.class).getFirstName());
                 m.setLastName(ds.getValue(User.class).getLastName());
                 m.setGender(ds.getValue(User.class).getGender());


                 uploads.add(m);
             }
adapter.notifyDataSetChanged();
         }

         @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
             uploads.clear();
             progressDialog.dismiss();

             for(DataSnapshot ds : dataSnapshot.getChildren())
             {
                 User m=new User();
                 m.setFirstName(ds.getValue(User.class).getFirstName());
                 m.setLastName(ds.getValue(User.class).getLastName());
                 m.setGender(ds.getValue(User.class).getGender());


                 uploads.add(m);
             }
adapter.notifyDataSetChanged();
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
}
