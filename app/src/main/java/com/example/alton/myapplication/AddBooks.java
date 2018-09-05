package com.example.alton.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.alton.myapplication.Models.BookOwn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by alton on 1/21/2018.
 */

public class AddBooks extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference mDatabase;
    Intent cameraIntent;
    EditText name, auth, publisher, desc,pric;
    ImageView book;
    DatabaseReference data;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Button button;
    private ArrayAdapter<String> adapter;
    private String[] items;
    private static final int RESULT_LOAD_IMG = 1;
    private Uri uri;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog progressDialog;

    Uri downloadUrl;
    Spinner spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);



        name = (EditText) findViewById(R.id.bookname);
        auth = (EditText) findViewById(R.id.author);
        publisher = (EditText) findViewById(R.id.publishermsg);
        desc = (EditText) findViewById(R.id.your_desc);
        book = (ImageView) findViewById(R.id.bookimg);
        pric=(EditText)findViewById(R.id.price);
        button = (Button) findViewById(R.id.post_message);
        items =getResources().getStringArray(R.array.category);
        spinner=(Spinner)findViewById(R.id.spinnerCategory);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout1, items);
        adapter.setDropDownViewResource(R.layout.spinner_layout1);
        spinner.setAdapter(adapter);

        button.setOnClickListener(this);
        book.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        data=FirebaseDatabase.getInstance().getReference();
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

    private void addBooks(String email, String bookname, String author, String publisher,String category, String desc,int price, String pic) {
        BookOwn newUser = new BookOwn();
        newUser.setUid(email);
        newUser.setPicUrl(pic);
        newUser.setBookname(bookname);
        newUser.setAuthor(author);
        newUser.setCategory(category);
        newUser.setPublisher(publisher);
        newUser.setPrice(price);
        newUser.setDesc(desc);


        mDatabase.child("books").child(category).child(bookname).setValue(newUser);
    }



@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

if(data==null){
    Toast.makeText(this, "Click photo", Toast.LENGTH_SHORT).show();
}
else {
     Bitmap bitmap=(Bitmap)data.getExtras().get("data");
     book.setImageBitmap(bitmap);

     uri=getImageUri(getApplicationContext(),bitmap);


    StorageReference storageReference=storage.getReference("bookImages/"+uri.getLastPathSegment());


    UploadTask uploadTask = storageReference.putFile(uri);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle unsuccessful uploads
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
             downloadUrl = taskSnapshot.getDownloadUrl();
            cameraIntent.putExtra("image",downloadUrl);

        }
    });

    }}

    public Uri getImageUri(Context context,Bitmap bitmap){
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path= MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,"Images",null);
        return Uri.parse(path);
    }

    @Override
    public void onClick(View view) {
switch (view.getId()){

    case R.id.post_message:
        final String bname = name.getText().toString().trim();
        String bauth = auth.getText().toString().trim();
        String pub = publisher.getText().toString().trim();
        String des = desc.getText().toString().trim();
        String gender= (String) spinner.getSelectedItem();
        String rate=pric.getText().toString().trim();
        Uri photoUrl = null;






        if(uri==null){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setMessage("Please insert image");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog=alert.create();
            alertDialog.show();
            return;
        }



       if (TextUtils.isEmpty(bname)) {
            name.setError("Enter name");
           return;
        }
       if (TextUtils.isEmpty(bauth)) {
            auth.setError("Enter author");
        return;
        }
        if(gender.contains("Select Category")){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setMessage("Please select category");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog=alert.create();
            alertDialog.show();
            return;
        }

        if (TextUtils.isEmpty(pub)) {
            publisher.setError("Enter publisher");
        return;
        }
         if (TextUtils.isEmpty(des)) {
            desc.setError("Enter Description");
        return;
        }

        if(TextUtils.isEmpty(rate)){
            pric.setError("Enter price");
            return;
        }




        data.child("books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

for(DataSnapshot ds:dataSnapshot.getChildren()) {
    String s1=ds.getValue(BookOwn.class).getBookname();
    if (bname.contains(s1)) {
        Toast.makeText(AddBooks.this, "A book with this name already exists,Please try some other name", Toast.LENGTH_LONG).show();
        return;
    }

}      }

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

        if(uri!=null) {
            photoUrl = Uri.parse(uri.toString());
        }


else {
            addBooks(FirebaseAuth.getInstance().getCurrentUser().getEmail(), bname, bauth, pub, gender, des, Integer.parseInt(rate), photoUrl.toString());
            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
            name.setText("");
            auth.setText("");
            desc.setText("");
            publisher.setText("");
            pric.setText("");
            spinner.setSelection(0);
            book.clearAnimation();

        }   break;

    case R.id.bookimg:
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, RESULT_LOAD_IMG);
break;
default:


}







    }



   /* private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        uri=Uri.parse(imageEncoded);

        StorageReference filepath = storage.child("books").child(uri.getLastPathSegment());

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddBooks.this, "Uploaded image", Toast.LENGTH_SHORT).show();
            }

        });
    }*/


        public static byte[] getBytes (InputStream inputStream) throws IOException {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        }

    }
