package com.example.alton.myapplication.Profile;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alton.myapplication.HomePageActivity;
import com.example.alton.myapplication.Models.User;
import com.example.alton.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewProfileActivity extends AppCompatActivity{


    TextView userFName,userLName,userGender;
    ImageView userProfilePic;
    Button btnBack;
    User existingUserProfile=new User();
    ProgressDialog progress;

    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progress=new ProgressDialog(this);
        userFName= (TextView) findViewById(R.id.textViewShowUFName);
        userLName= (TextView) findViewById(R.id.textViewShowULName);
        userGender= (TextView) findViewById(R.id.textViewShowUGender);
        userProfilePic= (ImageView) findViewById(R.id.imageViewUserProfilePicture);
        btnBack= (Button) findViewById(R.id.buttonBack);

        progress.setMessage("Please wait");
        progress.show();


        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progress.dismiss();
                User existingUserProfile= dataSnapshot.getValue(User.class);
                userFName.setText(existingUserProfile.getFirstName());
                userLName.setText(existingUserProfile.getLastName());
                userGender.setText(existingUserProfile.getGender());
//                Log.d("existingProfilePic",existingUserProfile.getUserPicUrl());
                if (!existingUserProfile.getUserPicUrl().isEmpty() && existingUserProfile.getUserPicUrl()!=null){
                    Picasso.with(getApplicationContext()).load(existingUserProfile.getUserPicUrl()).into(userProfilePic);
                }

                ViewProfileActivity.this.existingUserProfile=existingUserProfile;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewProfileActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
