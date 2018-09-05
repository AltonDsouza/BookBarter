package com.example.alton.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.alton.myapplication.Models.Review;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alton on 2/15/2018.
 */

public class Reviews extends AppCompatActivity {

    List<Review> uploads;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog=new ProgressDialog(this);

        uploads=new ArrayList<>();


        progressDialog.setMessage("Please wait...");
        progressDialog.show();
databaseReference=firebaseDatabase.getReference("reviews");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
progressDialog.dismiss();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String s1=ds.getValue(Review.class).getBookName();

                    if(s1.contains(getIntent().getStringExtra("book"))){
                        Review r1 = new Review();
                        r1.setEmailId(ds.getValue(Review.class).getEmailId());
                        r1.setReview(ds.getValue(Review.class).getReview());

                        uploads.add(r1);
                    }}

                adapter=new CommentAdapter(getApplicationContext(),uploads);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
progressDialog.dismiss();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String s1=ds.getValue(Review.class).getBookName();
                    if(s1.contains(getIntent().getStringExtra("book"))) {

                        Review r1 = new Review();
                        r1.setEmailId(ds.getValue(Review.class).getEmailId());
                        r1.setReview(ds.getValue(Review.class).getReview());

                        uploads.add(r1);

                    }                }

                adapter=new CommentAdapter(getApplicationContext(),uploads);
                recyclerView.setAdapter(adapter);

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
