package com.example.alton.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alton.myapplication.Models.BookOwn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by alton on 2/25/2018.
 */

public class EditBooks extends AppCompatActivity {

    ImageView imageBook;
    EditText bname,bauth,pub,des,price;
    DatabaseReference databaseReference;
    DatabaseReference mdatabase;
    private ArrayAdapter<String> adapter;
    private String[] items;
    Spinner spinner;

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
ProgressDialog progressDialog;
Button b1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);
        imageBook=(ImageView)findViewById(R.id.bookimgedit);
        bname=(EditText)findViewById(R.id.booknameedit);
        bauth=(EditText)findViewById(R.id.authoredit);
        pub=(EditText)findViewById(R.id.publishermsgedit);
        des=(EditText)findViewById(R.id.your_descedit);
        price=(EditText)findViewById(R.id.price_edit);
        progressDialog=new ProgressDialog(this);
        databaseReference=firebaseDatabase.getReference("books");
       mdatabase=firebaseDatabase.getReference();
        items =getResources().getStringArray(R.array.category);

        spinner=(Spinner)findViewById(R.id.spinnerCategoryedit);

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout1, items);
        adapter.setDropDownViewResource(R.layout.spinner_layout1);
        spinner.setAdapter(adapter);

        b1=(Button)findViewById(R.id.update);

        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(imageBook);
        bname.setText(getIntent().getStringExtra("bookname"));
        bname.setEnabled(false);
        spinner.setEnabled(false);


        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                progressDialog.dismiss();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String s1=String.valueOf(ds.getValue(BookOwn.class).getBookname());
                    if(s1.contentEquals(getIntent().getStringExtra("bookname"))){
                        String s2=String.valueOf(ds.getValue(BookOwn.class).getAuthor());
                        String s3=String.valueOf(ds.getValue(BookOwn.class).getDesc());
                        String s4=String.valueOf(ds.getValue(BookOwn.class).getPublisher());
                        String s6=String.valueOf(ds.getValue(BookOwn.class).getCategory());
                        int s5=ds.getValue(BookOwn.class).getPrice();

                        bauth.setText(s2);
                        des.setText(s3);
                        pub.setText(s4);
                        price.setText(String.valueOf(s5));
                        if (s6 != null) {
                            int spinnerPosition = adapter.getPosition(s6);
                            spinner.setSelection(spinnerPosition);
                    }
                }
            }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                progressDialog.dismiss();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String s1=String.valueOf(ds.getValue(BookOwn.class).getBookname());
                    if(s1.contentEquals(getIntent().getStringExtra("bookname"))){
                        String s2=String.valueOf(ds.getValue(BookOwn.class).getAuthor());
                        String s3=String.valueOf(ds.getValue(BookOwn.class).getDesc());
                        String s4=String.valueOf(ds.getValue(BookOwn.class).getPublisher());
                        String s6=String.valueOf(ds.getValue(BookOwn.class).getCategory());
                        int s5=ds.getValue(BookOwn.class).getPrice();

                        bauth.setText(s2);
                        des.setText(s3);
                        pub.setText(s4);
                        price.setText(String.valueOf(s5));
                        if (s6 != null) {
                            int spinnerPosition = adapter.getPosition(s6);
                            spinner.setSelection(spinnerPosition);
                        }
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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBooks(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        bauth.getText().toString(), String.valueOf(spinner.getSelectedItem()),bname.getText().toString(),
                        des.getText().toString(),
                        pub.getText().toString(),Integer.parseInt(String.valueOf(price.getText())),
                        getIntent().getStringExtra("image"));
                     Toast.makeText(EditBooks.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateBooks(String email,String author,String category, String bookname,String des, String publisher, int price,String picUrl){
        BookOwn b=new BookOwn();
        b.setUid(email);
        b.setAuthor(author);
        b.setCategory(category);
        b.setBookname(bookname);
        b.setDesc(des);
        b.setPublisher(publisher);
        b.setPrice(price);
        b.setPicUrl(picUrl);

        mdatabase.child("books").child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("bookname"))
                .setValue(b);
    }
}
