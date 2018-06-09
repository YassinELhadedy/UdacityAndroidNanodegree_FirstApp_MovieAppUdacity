package com.nextbit.yassin.movieappudacity1.presenter.activity;

import android.annotation.SuppressLint;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nextbit.yassin.movieappudacity1.R;
import com.nextbit.yassin.movieappudacity1.domain.model.Movie;
import com.nextbit.yassin.movieappudacity1.domain.model.Review;
import com.nextbit.yassin.movieappudacity1.domain.model.ReviewList;
import com.nextbit.yassin.movieappudacity1.domain.model.Trailer;
import com.nextbit.yassin.movieappudacity1.domain.model.TrailerList;
import com.nextbit.yassin.movieappudacity1.infrastructure.cache.CacheImpl;
import com.nextbit.yassin.movieappudacity1.infrastructure.repository.MovieDataRepository;
import com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource.MovieDataStoreFactory;
import com.nextbit.yassin.movieappudacity1.presenter.adapter.ReviewAdapter;
import com.nextbit.yassin.movieappudacity1.presenter.adapter.TrailerAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.nextbit.yassin.movieappudacity1.presenter.fragment.MoviesGrid.pageNum;

public class DetailsMovieAct extends AppCompatActivity {

    private ImageView detailFav;
    private ImageView detailUnFav;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter traadapter;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        ImageView detailBackdrop = findViewById(R.id.backdrop2);
        detailFav= findViewById(R.id.detailFav);
        detailUnFav= findViewById(R.id.detailUnFav);
        ImageView poster1 = findViewById(R.id.detailPoster2);
        TextView detailDate = findViewById(R.id.detailDate);
        TextView detailVoteAvr = findViewById(R.id.detailVoteAvr);
        TextView detailDur = findViewById(R.id.detailDur);
        TextView detailOverview = findViewById(R.id.detailOverview);
        RecyclerView rvrecyclerView = findViewById(R.id.revsList);
        RecyclerView trarecycleview = findViewById(R.id.detail_trailers);
        Toolbar toolbar = findViewById(R.id.toolbardetails);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(this.getIntent().getExtras().getString("title"));

        String backdropUrl = "http://image.tmdb.org/t/p/w185/"
                + this.getIntent().getExtras().getString("backdrop");



        Glide.with(this).load(backdropUrl).into(detailBackdrop);
        Glide.with(this).load("http://image.tmdb.org/t/p/w185/"+getIntent().getStringExtra("poster")).into(poster1);
        detailOverview.setText(getIntent().getStringExtra("overview"));
        detailDate.setText(getIntent().getStringExtra("date"));
        detailDur.setText("2 hr");
        detailVoteAvr.setText(""+getIntent().getDoubleExtra("vote",15));
        int id= getIntent().getIntExtra("id",165);
        detailFav.setVisibility(View.INVISIBLE);
        detailUnFav.setVisibility(View.INVISIBLE);
        List<Review> reviewList = new ArrayList<>();
        reviewAdapter=new ReviewAdapter(this, reviewList);
        rvrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvrecyclerView.setAdapter(reviewAdapter);

        List<Trailer> trailerList = new ArrayList<>();
        String name=getIntent().getExtras().getString("title");
        String poster=getIntent().getStringExtra("poster");
        traadapter=new TrailerAdapter(this, trailerList,name,poster);
        trarecycleview.setLayoutManager(new LinearLayoutManager(this));
        trarecycleview.setAdapter(traadapter);




        isFavMovies(id);
        loadReviews(String.valueOf(id));
        load(String.valueOf(id));
    }
    private void isFavMovies(int id){
        Movie movie =new Movie();
        movie.setId(id);
        MovieDataRepository movieDataRepository=new MovieDataRepository(new MovieDataStoreFactory(this,new CacheImpl(this)));

        if(movieDataRepository.isFoundFavMov(movie)){
            detailUnFav.setVisibility(View.VISIBLE);

        }
        else {

            detailFav.setVisibility(View.VISIBLE);
        }
    }
    public void loadReviews(String id){

        MovieDataRepository movieDataRepository=new MovieDataRepository(new MovieDataStoreFactory(this,new CacheImpl(this)));
        movieDataRepository.movieReviewsStore(id, String.valueOf(pageNum))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewList>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ReviewList soAnswersResponse) {


                        reviewAdapter.updateMovies(soAnswersResponse.getReviews());
                    }
                });
    }
    public void load(String id){
        MovieDataRepository movieDataRepository=new MovieDataRepository(new MovieDataStoreFactory(this,new CacheImpl(this)));
        movieDataRepository.movieTrailerStore(id, String.valueOf(pageNum))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrailerList>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TrailerList soAnswersResponse) {
                        traadapter.updateMovies(soAnswersResponse.getTraliers());
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
