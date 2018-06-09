package com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource;




import com.nextbit.yassin.movieappudacity1.domain.model.MoviesList;
import com.nextbit.yassin.movieappudacity1.domain.model.ReviewList;
import com.nextbit.yassin.movieappudacity1.domain.model.TrailerList;
import com.nextbit.yassin.movieappudacity1.infrastructure.cache.EntityCache;

import rx.Observable;

/**
 * Created by yassin on 10/8/17.
 */

public class DiskDataStore implements MovieDataStore {
    private final EntityCache entityCache;

    public DiskDataStore(EntityCache entityCache) {
        this.entityCache = entityCache;
    }


    @Override
    public Observable<MoviesList> movieListPopularStore(String spec,String page) {
        return entityCache.getMoviesCache();
    }

    @Override
    public Observable<MoviesList> movieListTopRatedStore(String spec, String page) {
        return entityCache.getMoviesCache();
    }

    @Override
    public Observable<TrailerList> movieTrailerStore(String movId, String page) {
        return entityCache.getTrailersCache(movId);
    }

    @Override
    public Observable<ReviewList> movieReviewsStore(String movId, String page) {
        return entityCache.getReviewsCache(movId);
    }

    @Override
    public Observable<MoviesList> movieFavList() {
        return entityCache.getFavMovies();
    }
}
