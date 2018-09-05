package com.example.alton.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alton.myapplication.LoginModule.LoginActivity;
import com.example.alton.myapplication.Maps.MapsActivity;
import com.example.alton.myapplication.Profile.ProfileDetailsActivity;
import com.example.alton.myapplication.Profile.ViewProfileActivity;
import com.example.alton.myapplication.sections.EducationBooks;
import com.example.alton.myapplication.sections.Favorites;
import com.example.alton.myapplication.sections.FictionBooks;
import com.example.alton.myapplication.sections.HorrorBooks;
import com.example.alton.myapplication.sections.RecentlyAdded;
import com.example.alton.myapplication.sections.RomanticBooks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by alton on 3/3/2018.
 */

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DatabaseReference mDatabase;
    Toolbar toolbar;
    Date date;
    CardView c1,c2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();



    }

    @Override
    public void onBackPressed () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }




    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.item_option_view_profile:
                Intent intent = new Intent(this, ProfileDetailsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_option_logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), ViewProfileActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_myBook) {
            startActivity(new Intent(getApplicationContext(), MyBooks.class));
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(getApplicationContext(), AddBooks.class));

        } else if (id == R.id.nav_near) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

        } else if (id == R.id.nav_tool) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            shareText();

        } else if (id == R.id.nav_feed) {
            startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareText() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Want to borrow/exchange books from friends?Download BookBarter now";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }


    public void fiction(View view) {
        startActivity(new Intent(getApplicationContext(), FictionBooks.class));
    }

    public void education(View view) {
        startActivity(new Intent(getApplicationContext(), EducationBooks.class));

    }

    public void recent(View view) {
        startActivity(new Intent(getApplicationContext(), RecentlyAdded.class));
    }

    public void fav(View view) {
        startActivity(new Intent(getApplicationContext(), Favorites.class));
    }

    public void roman(View view) {
        startActivity(new Intent(getApplicationContext(), RomanticBooks.class));
    }

    public void horror(View view) {
        startActivity(new Intent(getApplicationContext(), HorrorBooks.class));
    }

}
