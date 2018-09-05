package com.example.alton.myapplication.sections;

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
import android.widget.Toast;

import com.example.alton.myapplication.AlbumsAdapter;
import com.example.alton.myapplication.Models.BookOwn;
import com.example.alton.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alton on 2/13/2018.
 */

public class Favorites extends AppCompatActivity{

    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    RecyclerView rec;
    List<BookOwn> albumList;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);


        rec=(RecyclerView)findViewById(R.id.recyclerViewFavorite);

        progressDialog=new ProgressDialog(this);

         albumList = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rec.setLayoutManager(mLayoutManager);
        rec.addItemDecoration(new Favorites.GridSpacingItemDecoration(2, dpToPx(10), true));
        rec.setItemAnimator(new DefaultItemAnimator());
        adapter = new FavoriteAdapter(Favorites.this, albumList);
        rec.setAdapter(adapter);

        progressDialog.setMessage("Please wait");
        progressDialog.show();




        databaseReference=firebaseDatabase.getReference("favorites");

databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        progressDialog.dismiss();
        albumList.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String s4 = String.valueOf(ds.getValue(BookOwn.class).getUid());
            if (s4.contentEquals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                String s1 = String.valueOf(ds.getValue(BookOwn.class).getBookname());
                String s2 = String.valueOf(ds.getValue(BookOwn.class).getAuthor());
                String s3 = String.valueOf(ds.getValue(BookOwn.class).getPicUrl());

                BookOwn m = new BookOwn();
                m.setBookname(s1);
                m.setAuthor(s2);
                m.setPicUrl(s3);

                albumList.add(m);
            }
           adapter.notifyDataSetChanged();

            if(albumList.isEmpty()){
                setContentView(R.layout.fav_dummy);
            }
        }
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }




}
