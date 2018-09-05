package com.example.alton.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.craftman.cardform.Card;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by alton on 3/3/2018.
 */

public class TourGuideActivity extends AppCompatActivity{

    CardView c1,c2,c3,c4;
    TourGuide mTourGuideHandler;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        c1=(CardView)findViewById(R.id.cardFiction);
        c2=(CardView)findViewById(R.id.cardEducation);
     c3=(CardView)findViewById(R.id.cardRomatic);
     c4=(CardView)findViewById(R.id.cardHorror);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer()) .setToolTip(new ToolTip()
                        .setTitle("Welcome!").setDescription("Here you'll find fiction books added by users"))
                .setOverlay(new Overlay()) .playOn(c1);

    }

    public void fiction(View view){

        mTourGuideHandler.cleanUp();


        mTourGuideHandler .setToolTip(new ToolTip() .setTitle("Education Section!!")
                .setDescription("Find all study materials over here.")
                .setBackgroundColor(Color.parseColor("#58ce44")))
                .playOn(c2);

    }

    public void education(View view){

        mTourGuideHandler.cleanUp();

        mTourGuideHandler .setToolTip(new ToolTip() .setTitle("Romatic section!!!")
                .setDescription("Find all the romatic books in this section :)")
                .setBackgroundColor(Color.parseColor("#0E37EC"))
                .setGravity(Gravity.TOP | Gravity.RIGHT)) .playOn(c3);


    }

public void roman(View view){
    mTourGuideHandler.cleanUp();

    mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
            .setPointer(new Pointer()) .setToolTip(new ToolTip()
                    .setTitle("Horror Section!!").setDescription("find all the  scary tale books added by users"))
            .setOverlay(new Overlay()) .playOn(c4);


}

public void horror(View view){
    mTourGuideHandler.cleanUp();

    startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
}


}
