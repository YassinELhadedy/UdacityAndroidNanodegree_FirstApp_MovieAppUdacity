/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nextbit.yassin.movieappudacity1.infrastructure.repository;



import com.nextbit.yassin.movieappudacity1.domain.model.Movie;
import com.nextbit.yassin.movieappudacity1.domain.model.MoviesList;
import com.nextbit.yassin.movieappudacity1.domain.model.ReviewList;
import com.nextbit.yassin.movieappudacity1.domain.model.TrailerList;
import com.nextbit.yassin.movieappudacity1.domain.repository.MovieRepository;
import com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource.MovieDataStore;
import com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource.MovieDataStoreFactory;

import rx.Observable;


public class MovieDataRepository implements MovieRepository {
  private final MovieDataStoreFactory movieDataStoreFactory;

  public MovieDataRepository(MovieDataStoreFactory movieDataStoreFactory) {
    this.movieDataStoreFactory = movieDataStoreFactory;
  }

  @Override
  public Observable<MoviesList> movieListPopularStore(String spec,String page) {
    final MovieDataStore movieDataStore = this.movieDataStoreFactory.create();
    return movieDataStore.movieListPopularStore(spec,page);
  }

  @Override
  public Observable<MoviesList> movieListTopRatedStore(String spec, String page) {
    final MovieDataStore movieDataStore = this.movieDataStoreFactory.create();
    return movieDataStore.movieListTopRatedStore(spec,page);
  }

  @Override
  public Observable<TrailerList> movieTrailerStore(String id, String page) {
    final MovieDataStore movieDataStore = this.movieDataStoreFactory.createTrailer(id);
    return movieDataStore.movieTrailerStore(id,page);
  }

  @Override
  public Observable<ReviewList> movieReviewsStore(String id, String page) {

    final MovieDataStore movieDataStore = this.movieDataStoreFactory.createReview(id);
    return movieDataStore.movieReviewsStore(id,page);
  }

  @Override
  public Observable<MoviesList> getFavMovies() {


    final MovieDataStore movieDataStore = this.movieDataStoreFactory.createFavMovies();
    return movieDataStore.movieFavList();
  }




  @Override
  public void putFavMovie(MoviesList moviesListObservable) {
    movieDataStoreFactory.getEntityCache().putFavMovies(moviesListObservable);

  }
  @Override
  public void deleteFavMovie(Movie movie) {
    movieDataStoreFactory.getEntityCache().deleteFavMovie(movie);

  }
  @Override
  public boolean isFoundFavMov(Movie movie) {
    return movieDataStoreFactory.getEntityCache().isExistFav(movie);
  }
}
