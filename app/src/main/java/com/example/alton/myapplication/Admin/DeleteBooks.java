package com.example.alton.myapplication.Admin;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alton on 2/11/2018.
 */

public class DeleteBooks extends AppCompatActivity {

RecyclerView recyclerView;
ProgressDialog progressDialog;
    private List<BookOwn> uploads;
    private DeleteAdapter adapter;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
DatabaseReference mDatabaseref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletebooks);
        recyclerView=(RecyclerView)findViewById(R.id.deleterecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DeleteBooks.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        progressDialog=new ProgressDialog(this);

        uploads=new ArrayList<>();
        adapter=new DeleteAdapter(this,uploads);
        recyclerView.setAdapter(adapter);

        //displaying progress dialog while fetching images

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabaseref=firebaseDatabase.getReference("books");

        mDatabaseref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressDialog.dismiss();

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String s1= String.valueOf(ds.getValue(BookOwn.class).getBookname());
                    String s2=String.valueOf(ds.getValue(BookOwn.class).getCategory());
                    String s3= String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

                    BookOwn m=new BookOwn();
                    m.setBookname(s1);
                    m.setCategory(s2);
                    m.setPicUrl(s3);
                    uploads.add(m);
                }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                progressDialog.dismiss();
                 uploads.clear();


                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String s1= String.valueOf(ds.getValue(BookOwn.class).getBookname());
                    String s2=String.valueOf(ds.getValue(BookOwn.class).getCategory());
                    String s3= String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

                    BookOwn m=new BookOwn();
                    m.setBookname(s1);
                    m.setCategory(s2);
                    m.setPicUrl(s3);
                    uploads.add(m);
                }
adapter.notifyDataSetChanged();

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
