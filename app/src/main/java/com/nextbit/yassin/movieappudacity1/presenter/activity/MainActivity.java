package com.nextbit.yassin.movieappudacity1.presenter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nextbit.yassin.movieappudacity1.R;
import com.nextbit.yassin.movieappudacity1.domain.model.Movie;
import com.nextbit.yassin.movieappudacity1.presenter.NavigateTo;
import com.nextbit.yassin.movieappudacity1.presenter.fragment.DetailsFra;
import com.nextbit.yassin.movieappudacity1.presenter.fragment.MoviesGrid;

public class MainActivity extends AppCompatActivity implements NavigateTo {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MoviesGrid moviesGrid = new MoviesGrid();
        moviesGrid.setNavigateTo(this);


        if (isTablet(this)) {
            //tablet mode
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, moviesGrid, "").commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.description_fragment, new DetailsFra(), "").commit();


        } else {
            // phone mode
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, moviesGrid, "").commit();

        }
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    public void navigateTo(Movie movie) {
        if (isTablet(this)) {
            DetailsFra detailsFra = new DetailsFra();

            Bundle args = new Bundle();

            args.putParcelable("Movie", movie);


            detailsFra.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.description_fragment, detailsFra).commit();
        } else {
            Intent data = new Intent(this, DetailsMovieAct.class);
            data.putExtra("Movie", movie);
            startActivity(data);
        }

    }
}