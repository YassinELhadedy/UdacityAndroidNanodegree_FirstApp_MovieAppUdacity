package com.nextbit.yassin.movieappudacity1.presenter.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nextbit.yassin.movieappudacity1.R;
import com.nextbit.yassin.movieappudacity1.presenter.fragment.DetailsFra;
import com.nextbit.yassin.movieappudacity1.presenter.fragment.MoviesGrid;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isTablet(this)){
            //tablet mode
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, new MoviesGrid(), "").commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.description_fragment, new DetailsFra(), "").commit();


        }else {
            // phone mode
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, new MoviesGrid(), "").commit();


        }



    }
    public  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }



}
