package com.nextbit.yassin.movieappudacity1.presenter.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
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

    private ImageView detailFav, detailBackdrop, detailUnFav, poster1;
    private TextView detailDate, detailVoteAvr, detailDur, detailOverview;
    private RecyclerView reviewRecyclerView, trailerRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        detailBackdrop = findViewById(R.id.backdrop2);
        detailFav = findViewById(R.id.detailFav);
        detailUnFav = findViewById(R.id.detailUnFav);
        poster1 = findViewById(R.id.detailPoster2);
        detailDate = findViewById(R.id.detailDate);
        detailVoteAvr = findViewById(R.id.detailVoteAvr);
        detailDur = findViewById(R.id.detailDur);
        detailOverview = findViewById(R.id.detailOverview);
        reviewRecyclerView = findViewById(R.id.revsList);
        trailerRecyclerView = findViewById(R.id.detail_trailers);
        Toolbar toolbar = findViewById(R.id.toolbardetails);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        Movie m = getIntent().getExtras().getParcelable("Movie");

        setView(getIntent().getExtras().getParcelable("Movie"));
    }

    private void setView(Movie movie) {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(movie.getTitle());

        String backdropUrl = "http://image.tmdb.org/t/p/w185/"
                + movie.getBackdropPath();


        Glide.with(this).load(backdropUrl).into(detailBackdrop);
        Glide.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath()).into(poster1);
        detailOverview.setText(movie.getOverview());
        detailDate.setText(movie.getReleaseDate());
        detailDur.setText("2 hr");
        if (movie.getVoteCount() != null) {
//            detailVoteAvr.setText(movie.getVoteCount());
        }
        int id = movie.getId();
        detailFav.setVisibility(View.INVISIBLE);
        detailUnFav.setVisibility(View.INVISIBLE);
        List<Review> reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setAdapter(reviewAdapter);

        List<Trailer> trailerList = new ArrayList<>();
        String name = movie.getTitle();
        String poster = movie.getPosterPath();
        trailerAdapter = new TrailerAdapter(this, trailerList, name, poster);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailerRecyclerView.setAdapter(trailerAdapter);


        isFavMovies(id);
        loadReviews(String.valueOf(id));
        load(String.valueOf(id));
    }


    private void isFavMovies(int id) {
        Movie movie = new Movie();
        movie.setId(id);
        MovieDataRepository movieDataRepository = new MovieDataRepository(new MovieDataStoreFactory(this, new CacheImpl(this)));

        if (movieDataRepository.isFoundFavMov(movie)) {
            detailUnFav.setVisibility(View.VISIBLE);

        } else {

            detailFav.setVisibility(View.VISIBLE);
        }
    }

    public void loadReviews(String id) {

        MovieDataRepository movieDataRepository = new MovieDataRepository(new MovieDataStoreFactory(this, new CacheImpl(this)));
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

    public void load(String id) {
        MovieDataRepository movieDataRepository = new MovieDataRepository(new MovieDataStoreFactory(this, new CacheImpl(this)));
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
                        trailerAdapter.updateMovies(soAnswersResponse.getTraliers());
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
