package com.example.alton.myapplication.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alton.myapplication.HomePageActivity;
import com.example.alton.myapplication.R;
import com.example.alton.myapplication.LoginModule.SignUpActivity;
import com.example.alton.myapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by alton on 1/18/2018.
 */

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText editUserFName,editUserLName;
    private Spinner userGender;
    private Button btnUpdate,btnCancel;
    private ImageView editUserPic;
    private String[] items;
    private ArrayAdapter<String> adapter;
    private CharSequence editPicOptions[] = new CharSequence[] {"Upload Photo", "Remove Photo"};
    private Uri profilePicUrl;
    private static final int RESULT_LOAD_IMG=1;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
     FirebaseAuth mAuth;
     FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    User existinguserProfile=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editUserPic= (ImageView) findViewById(R.id.imageViewEditProfilePic);
        editUserFName= (EditText) findViewById(R.id.editTextUserFName);
        editUserLName= (EditText) findViewById(R.id.editTextUserLName);
        userGender= (Spinner) findViewById(R.id.spinnerUserGender);
        items =getResources().getStringArray(R.array.gender_list);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userGender.setAdapter(adapter);
        btnUpdate= (Button) findViewById(R.id.buttonUpdate);
        btnCancel= (Button) findViewById(R.id.buttonCancel);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editUserPic.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (getIntent().getExtras().getSerializable("existingUser")!=null){
            Log.d("esitingUserDetails",getIntent().getExtras().getSerializable("existingUser").toString());
            existinguserProfile= (User) getIntent().getExtras().getSerializable("existingUser");
            editUserFName.setText(existinguserProfile.getFirstName());
            editUserLName.setText(existinguserProfile.getLastName());
            if (existinguserProfile.getGender()==null || existinguserProfile.getGender().isEmpty()){
                userGender.setSelection(0);
            } else if (existinguserProfile.getGender().toString().equalsIgnoreCase("Female")){
                userGender.setSelection(2);
            }else {
                userGender.setSelection(1);
            }
            if (existinguserProfile.getUserPicUrl()!=null && !existinguserProfile.getUserPicUrl().isEmpty()) {
                profilePicUrl= Uri.parse(existinguserProfile.getUserPicUrl());
                Picasso.with(getApplicationContext()).load(existinguserProfile.getUserPicUrl()).into(editUserPic);
            }
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("demo", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("demo", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.imageViewEditProfilePic){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update Profile Pic");
            builder.setCancelable(true);
            builder.setItems(editPicOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==0){
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                    }else if (which==1){
                        editUserPic.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                        profilePicUrl=null;
                    }
                }
            });
            builder.show();
        }else if (v.getId()==R.id.buttonUpdate){
            if (userGender.getSelectedItemPosition()!=0){
                Log.d("updateuser",editUserFName.getText().toString()+editUserLName.getText().toString()+userGender.getSelectedItem().toString()+profilePicUrl);
                if (profilePicUrl!=null) {
                    updateUserProfile(existinguserProfile.getUid(), editUserFName.getText().toString(), editUserLName.getText().toString(), userGender.getSelectedItem().toString(), profilePicUrl.toString());
                }else {
                    updateUserProfile(existinguserProfile.getUid(), editUserFName.getText().toString(), editUserLName.getText().toString(), userGender.getSelectedItem().toString(),null);
                }
                Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EditProfileActivity.this,ProfileDetailsActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Please select gender",Toast.LENGTH_SHORT).show();
            }

        }else if (v.getId()==R.id.buttonCancel){
            Intent intent=new Intent(EditProfileActivity.this,HomePageActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data
            Uri url = data.getData();
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(url);
                byte[] inputData = SignUpActivity.getBytes(iStream);
                StorageReference photosRef= storage.getReference("profileImages/" + url.getLastPathSegment());

                UploadTask uploadTask=photosRef.putBytes(inputData);
                uploadTask.addOnProgressListener(EditProfileActivity.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.d("demo","Upload is " + progress + "% done");
                    }
                });
                uploadTask.addOnSuccessListener(EditProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profilePicUrl= taskSnapshot.getDownloadUrl();
                        Picasso.with(getApplicationContext()).load(profilePicUrl.toString()).into(editUserPic);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUserProfile(String userID,String firstName,String lastName, String gender, String picUrl) {
        User newUser = new User();
        newUser.setUid(userID);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setGender(gender);
        if (picUrl!=null){
            newUser.setUserPicUrl(picUrl);
        }else {
            newUser.setUserPicUrl("");
        }

        mDatabase.child("users").child(userID).child("profile").setValue(newUser);
    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
}
