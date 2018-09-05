package com.example.alton.myapplication.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alton.myapplication.HomePageActivity;
import com.example.alton.myapplication.R;
import com.example.alton.myapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by alton on 1/18/2018.
 */

public class ProfileDetailsActivity extends AppCompatActivity{


    TextView userFName,userLName,userGender;
    ImageView userProfilePic,imgEditProfile;
    Button btnOk;
    User existingUserProfile=new User();
    ProgressDialog progressDialog;

    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        userFName= (TextView) findViewById(R.id.textViewUserFName);
        userLName= (TextView) findViewById(R.id.textViewEditLName);
        userGender= (TextView) findViewById(R.id.textViewUserGender);
        userProfilePic= (ImageView) findViewById(R.id.imageViewProfilePic);
        imgEditProfile= (ImageView) findViewById(R.id.imageViewEditProfile);
        btnOk= (Button) findViewById(R.id.buttonOk);

        mDatabase=FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                User existingUserProfile= dataSnapshot.getValue(User.class);
                userFName.setText(existingUserProfile.getFirstName());
                userLName.setText(existingUserProfile.getLastName());
                userGender.setText(existingUserProfile.getGender());
//                Log.d("existingProfilePic",existingUserProfile.getUserPicUrl());
                if (!existingUserProfile.getUserPicUrl().isEmpty() && existingUserProfile.getUserPicUrl()!=null){
                    Picasso.with(getApplicationContext()).load(existingUserProfile.getUserPicUrl()).into(userProfilePic);
                }

                ProfileDetailsActivity.this.existingUserProfile=existingUserProfile;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileDetailsActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (existingUserProfile!=null){
                    Log.d("demoOnEditClick",existingUserProfile.toString());
                    Intent intent=new Intent(ProfileDetailsActivity.this,EditProfileActivity.class);
                    intent.putExtra("existingUser",existingUserProfile);
                    startActivity(intent);
                    finish();
                }else {

                }
            }
        });


    }

}
