package com.example.alton.myapplication;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.sections.ImageAdapter;
import com.example.alton.myapplication.sections.RecentlyAdded;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alton on 2/25/2018.
 */

public class MyBooks extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
   RecyclerView recyclerView;
     MyBookAdapter adapter;
     List<BookOwn> uploads;
ProgressDialog progressDialog;
TextView empty;
View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooks);


        databaseReference=firebaseDatabase.getReference("books");


        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewMyBooks);
        empty=(TextView)findViewById(R.id.empty_view);
      progressDialog=new ProgressDialog(this);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyBooks.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        uploads=new ArrayList<>();
        adapter=new MyBookAdapter(this,uploads);
        recyclerView.setAdapter(adapter);



        progressDialog.setTitle("Viewing your books");
        progressDialog.setMessage("Please wait");

databaseReference.addChildEventListener(new ChildEventListener() {

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        progressDialog.dismiss();

     for(DataSnapshot ds:dataSnapshot.getChildren()){
         String s1=String.valueOf(ds.getValue(BookOwn.class).getUid());
         if(s1.contentEquals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
             String s2= String.valueOf(ds.getValue(BookOwn.class).getBookname());
             String s3= String.valueOf(ds.getValue(BookOwn.class).getCategory());

             String s4= String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

             BookOwn m=new BookOwn();
             m.setBookname(s2);
             m.setCategory(s3);
             m.setPicUrl(s4);
             uploads.add(m);
         }

         adapter.notifyDataSetChanged();

       }

       if(uploads.isEmpty()){
             setContentView(R.layout.dummy);
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        progressDialog.dismiss();

        for(DataSnapshot ds:dataSnapshot.getChildren()){
            String s1=String.valueOf(ds.getValue(BookOwn.class).getUid());
            if(s1.contentEquals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                String s2= String.valueOf(ds.getValue(BookOwn.class).getBookname());
                String s3= String.valueOf(ds.getValue(BookOwn.class).getCategory());

                String s4= String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

                BookOwn m=new BookOwn();
                m.setBookname(s2);
                m.setCategory(s3);
                m.setPicUrl(s4);
                uploads.add(m);
            }

            adapter.notifyDataSetChanged();

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


/*if(adapter.getItemCount()==0){
    setContentView(R.layout.dummy);
        return ;
}*/

/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No books added yet!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(MyBooks.this, HomePageActivity.class));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
*/
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
