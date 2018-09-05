package com.example.alton.myapplication.sections;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.R;
import com.example.alton.myapplication.SearchActivity.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alton on 2/3/2018.
 */
public class RecentlyAdded extends AppCompatActivity {
    private RecyclerView recyclerView,recyclerViewsearch;
    private RecyclerView.Adapter adapter;
    DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private List<BookOwn> uploads;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    EditText search_edit_text;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<BookOwn> fullNameList;
    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recentlyadded);

        Log.v("Check1","into second activity");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        recyclerViewsearch = (RecyclerView) findViewById(R.id.recyclerViewSeacrh);

        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerViewsearch.setHasFixedSize(true);
        recyclerViewsearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewsearch.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /*
        * Create a array list for each node you want to use
        * */
        fullNameList = new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    /*
                    * Clear the list when editText is empty
                    * */
                    fullNameList.clear();
                    recyclerViewsearch.removeView(recyclerViewsearch);
                }
            }
        });




        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new RecentlyAdded.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        progressDialog=new ProgressDialog(this);

        uploads=new ArrayList<>();




        //displaying progress dialog while fetching images

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase=firebaseDatabase.getReference("books");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressDialog.dismiss();


                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String s1= String.valueOf(ds.getValue(BookOwn.class).getBookname());
                    String s2= String.valueOf(ds.getValue(BookOwn.class).getCategory());

                    String s3= String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

                    BookOwn m=new BookOwn();
                    m.setBookname(s1);
                    m.setCategory(s2);
                    m.setPicUrl(s3);
                    uploads.add(m);
                }
                adapter=new ImageAdapter(RecentlyAdded.this,uploads);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                progressDialog.dismiss();

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    String s1= String.valueOf(ds.getValue(BookOwn.class).getBookname());

                    String s2= String.valueOf(ds.getValue(BookOwn.class).getCategory());
                    String s3= String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

                    BookOwn m=new BookOwn();
                    m.setBookname(s1);
                    m.setCategory(s2);
                    m.setPicUrl(s3);

                    uploads.add(m);
                }
                adapter=new ImageAdapter(RecentlyAdded.this,uploads);
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

        //iterating through all the values in database
    }

    private void setAdapter(final String searchedString) {
        databaseReference.child("books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                recyclerViewsearch.removeAllViews();

               int counter = 0;
               /*
                * Search all users for matching searched string
                * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    BookOwn b=new BookOwn();
                    String full_name = snapshot.getValue(BookOwn.class).getBookname();
                    String user_name = snapshot.getValue(BookOwn.class).getAuthor();
                    String profile_pic = snapshot.getValue(BookOwn.class).getPicUrl();


                    b.setBookname(full_name);
                    b.setAuthor(user_name);
                    b.setPicUrl(profile_pic);
                    if (full_name.toLowerCase().contains(searchedString.toLowerCase())) {
                        fullNameList.add(b);
                        counter++;


                    } else if (user_name.toLowerCase().contains(searchedString.toLowerCase())) {
                        fullNameList.add(b);
                        counter++;

                    }
                    /*
                     * Get maximum of 15 searched results only
                    * */
                    if (counter == 15)
                        break;
                }

                searchAdapter = new SearchAdapter(RecentlyAdded.this, fullNameList);
                recyclerViewsearch.setAdapter(searchAdapter);


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
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
