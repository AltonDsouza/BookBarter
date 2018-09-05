package com.example.alton.myapplication.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alton.myapplication.LoginModule.LoginActivity;
import com.example.alton.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by alton on 2/8/2018.
 */

public class AdminActivity extends AppCompatActivity{
  AlertDialog.Builder demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        demo=new AlertDialog.Builder(this);

        demo.setMessage("Welcome admin");
        demo.setCancelable(true);
        demo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

      AlertDialog alertDialog= demo.create();
      alertDialog.show();


    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
           startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void view(View view) {
        startActivity(new Intent(getApplicationContext(),AdminShowUsers.class));
    }

    public void add(View view) {
        startActivity(new Intent(getApplicationContext(), AddBookAdmin.class));
    }

    public void delete(View view) {
        startActivity(new Intent(getApplicationContext(),DeleteBooks.class));
    }

    public void update(View view) {
        startActivity(new Intent(getApplicationContext(),UpdateBooks.class));
    }
}
