package com.example.alton.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.Models.Review;
import com.example.alton.myapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by alton on 1/10/2018.
 */

public class BookDetails extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
     TextView textBookName,rev;
  private RatingBar ratingBar;
    TextView textAuthor,textdesc,price;
     Button mBorrow,mBuy,bpost,comment;
     DatabaseReference databaseReference;
FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    EditText com;
ProgressDialog progressDialog;
DatabaseReference data;
DatabaseReference mdata;
String s2;
TextView des,mob;
int s5;
String s4;
int price1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);

        imageView=(ImageView)findViewById(R.id.image);
        textBookName=(TextView)findViewById(R.id.text1);
      com=(EditText)findViewById(R.id.comments);
        textAuthor=(TextView)findViewById(R.id.text2);
        mBorrow=(Button)findViewById(R.id.borrow);
        textdesc=(TextView)findViewById(R.id.description);
price=(TextView)findViewById(R.id.pricetag);
mob=(TextView)findViewById(R.id.phonedetail);
        des=(TextView)findViewById(R.id.textdes);
                mBuy=(Button)findViewById(R.id.buy);
      bpost=(Button)findViewById(R.id.post);

comment=(Button)findViewById(R.id.showComments);
comment.setOnClickListener(this);
progressDialog=new ProgressDialog(this);
        databaseReference = firebaseDatabase.getReference("books");

        //displaying progress dialog while fetching images

       imageView.setImageURI(Uri.parse(getIntent().getStringExtra("image")));
       textBookName.setText(getIntent().getStringExtra("bookname"));
       textAuthor.setText(getIntent().getStringExtra("author"));

mdata=FirebaseDatabase.getInstance().getReference();

mBorrow.setOnClickListener(this);
mBuy.setOnClickListener(this);
bpost.setOnClickListener(this);

        data=firebaseDatabase.getReference("users");
progressDialog.setMessage("Please wait...");
progressDialog.show();
databaseReference.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


        progressDialog.dismiss();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            String s1=ds.getValue(BookOwn.class).getBookname();
            String s3=ds.getValue(BookOwn.class).getDesc();


            if(s1.contains(getIntent().getStringExtra("bookname"))){
             s2=ds.getValue(BookOwn.class).getUid();
             String s4=ds.getValue(BookOwn.class).getDesc();
              s5=ds.getValue(BookOwn.class).getPrice();

                textdesc.setText(s2);
                des.setText(s4);
                price.setText(String.valueOf(s5));
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

data.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        for(DataSnapshot ds:dataSnapshot.getChildren()){
            String s1=String.valueOf(ds.getValue(User.class).getEmail());

         if(s1.contentEquals(s2)){
                 s4=ds.getValue(User.class).getPhone();
                mob.setText(s4);
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


    public void writeReview(String emailId,String review,String bookName){
        Review review1=new Review();
        review1.setEmailId(emailId);
        review1.setReview(review);
        review1.setBookName(bookName);

        mdata.child("reviews").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getIntent().getStringExtra("bookname")).setValue(review1);
    }

    @Override
    public void onClick(View view) {
        if(view==findViewById(R.id.buy)){

            Intent i=new Intent(getApplicationContext(),PaymentActivity.class);
            i.putExtra("price", s5);

                     startActivity(i);
        }
        if (view==findViewById(R.id.post)){
            String review=com.getText().toString().trim();



if(review.isEmpty()){
    AlertDialog.Builder alert=new AlertDialog.Builder(this);
    alert.setMessage("Cannot post blank review");
    alert.setCancelable(false);
    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
       dialogInterface.dismiss();
        }
    });

    AlertDialog dialog=alert.create();
    dialog.show();
}else {

    writeReview(FirebaseAuth.getInstance().getCurrentUser().getEmail(), review, getIntent().getStringExtra("bookname"));
    Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show();
    com.setVisibility(View.GONE);
    bpost.setVisibility(View.GONE);
}
        }
        if(view==findViewById(R.id.showComments)){


Intent i=new Intent(getApplicationContext(),Reviews.class);
i.putExtra("book",getIntent().getStringExtra("bookname"));
startActivity(i);
        }

        if(view==findViewById(R.id.borrow)){
            Intent i=new Intent(getApplicationContext(),ComposeEmail.class);
            i.putExtra("book",getIntent().getStringExtra("bookname"));
            i.putExtra("email",s2);
            i.putExtra("phone",s4);
            startActivity(i);
        }

    }
}
