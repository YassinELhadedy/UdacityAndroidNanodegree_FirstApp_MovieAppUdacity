package com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource;



import com.nextbit.yassin.movieappudacity1.domain.model.MoviesList;
import com.nextbit.yassin.movieappudacity1.domain.model.ReviewList;
import com.nextbit.yassin.movieappudacity1.domain.model.TrailerList;
import com.nextbit.yassin.movieappudacity1.domain.service.SOService;
import com.nextbit.yassin.movieappudacity1.infrastructure.cache.EntityCache;
import com.nextbit.yassin.movieappudacity1.infrastructure.service.ApiUtils;

import rx.Observable;



public class CloudDataStore implements MovieDataStore {
    private final EntityCache entityCache;
    private SOService mService;

    public CloudDataStore(EntityCache entityCache) {
        this.entityCache = entityCache;
        mService= ApiUtils.getSOService();
    }


    @Override
    public Observable<MoviesList> movieListPopularStore(String spec, String page) {
        return this.mService.getMoviesListPopular(spec,page).doOnNext(entityCache::putMovies);
    }

    @Override
    public Observable<MoviesList> movieListTopRatedStore(String spec,String page) {

        return this.mService.getMoviesListTopRated(spec,page).doOnNext(entityCache::putMovies);

    }

    @Override
    public Observable<TrailerList> movieTrailerStore(String movId, String page) {
        return this.mService.getTrailares(movId,page).doOnNext(entityCache::putTrailers);

    }

    @Override
    public Observable<ReviewList> movieReviewsStore(String movId, String page) {
        return this.mService.getReviews(movId,page).doOnNext(entityCache::putReviews);
    }

    @Override
    public Observable<MoviesList> movieFavList() {
        return null;
    }
}
