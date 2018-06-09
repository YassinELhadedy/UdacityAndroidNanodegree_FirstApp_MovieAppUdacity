package com.nextbit.yassin.movieappudacity1.infrastructure.cache;

import android.content.Context;


import com.nextbit.yassin.movieappudacity1.domain.model.Movie;
import com.nextbit.yassin.movieappudacity1.domain.model.MoviesList;
import com.nextbit.yassin.movieappudacity1.domain.model.Review;
import com.nextbit.yassin.movieappudacity1.domain.model.ReviewList;
import com.nextbit.yassin.movieappudacity1.domain.model.Trailer;
import com.nextbit.yassin.movieappudacity1.domain.model.TrailerList;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by yassin on 10/8/17.
 */

public class CacheImpl implements EntityCache {
    private final Context context;
    private DatabaseHandler databaseHandler;

    public CacheImpl(Context context) {
        this.context = context.getApplicationContext();
        databaseHandler=new DatabaseHandler(this.context);

    }


    @Override
    public Observable<MoviesList> getMoviesCache() {
        MoviesList moviesList=new MoviesList();
        moviesList.setmovies(databaseHandler.getMoviesOffline());

        Observable<MoviesList> moviesListObservable = Observable.just(moviesList);
        return moviesListObservable  ;
    }

    @Override
    public Observable<TrailerList> getTrailersCache(String movId) {
        TrailerList trailerList=new TrailerList();
        trailerList.setTraliers(databaseHandler.getTrailers(movId));


        Observable<TrailerList> trailerListObservable = Observable.just(trailerList);
        return trailerListObservable  ;
    }

    @Override
    public Observable<ReviewList> getReviewsCache(String movId) {
        ReviewList reviewList=new ReviewList();
        reviewList.setReviews(databaseHandler.getReviews(movId));
        int s=reviewList.getReviews().size();


        Observable<ReviewList> reviewListObservable = Observable.just(reviewList);
        return reviewListObservable  ;

    }

    @Override
    public Observable<MoviesList> getFavMovies() {
        MoviesList moviesList=new MoviesList();
        moviesList.setmovies(databaseHandler.get_fav_movies());

        Observable<MoviesList> moviesListObservable = Observable.just(moviesList);
        return moviesListObservable  ;

    }


    @Override
    public void putMovies(MoviesList movie) {



        ArrayList<Movie>movieArrayList=new ArrayList<>();
        movieArrayList.addAll(movie.getmovies());
        databaseHandler.addOffline(movieArrayList);

    }

    @Override
    public void putTrailers(TrailerList trailer) {
        ArrayList<Trailer>trailerArrayList=new ArrayList<>();
        trailerArrayList.addAll(trailer.getTraliers());
        databaseHandler.addtrailers(trailerArrayList,trailer.getId());
    }

    @Override
    public void putReviews(ReviewList review) {
        ArrayList<Review>reviewArrayList=new ArrayList<>();
        reviewArrayList.addAll(review.getReviews());
        databaseHandler.addReviews(reviewArrayList,review.getId());


    }

    @Override
    public void putFavMovies(MoviesList moviesList) {
        ArrayList<Movie>movieArrayList=new ArrayList<>();
        movieArrayList.addAll(moviesList.getmovies());
        databaseHandler.isExist(movieArrayList);
    }

    @Override
    public void deleteFavMovie(Movie movie) {
        databaseHandler.deleteMovie(movie);

    }


    @Override
    public boolean isCached() {
      //  Toast.makeText(context, ""+databaseHandler.isEmpty(), Toast.LENGTH_SHORT).show();
        return databaseHandler.isEmpty();
    }

    @Override
    public boolean isCashedReview(String id) {
        return databaseHandler.isCaschedReview(id);
    }

    @Override
    public boolean isCashedTrailer(String id) {

        return databaseHandler.isCaschedTrailer(id);
    }

    @Override
    public void evictAll() {
        databaseHandler.deleteTable();

    }

    @Override
    public void evictAllReviews() {
        databaseHandler.deleteTableReview();
    }

    @Override
    public void evictAllTrailers() {
        databaseHandler.deleteTableTrailer();

    }
    @Override
    public boolean isExistFav(Movie movie) {
        return databaseHandler.isExistFavMov(movie);
    }
}
