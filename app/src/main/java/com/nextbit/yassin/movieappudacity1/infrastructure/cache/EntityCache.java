/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nextbit.yassin.movieappudacity1.infrastructure.cache;



import com.nextbit.yassin.movieappudacity1.domain.model.Movie;
import com.nextbit.yassin.movieappudacity1.domain.model.MoviesList;
import com.nextbit.yassin.movieappudacity1.domain.model.Review;
import com.nextbit.yassin.movieappudacity1.domain.model.ReviewList;
import com.nextbit.yassin.movieappudacity1.domain.model.Trailer;
import com.nextbit.yassin.movieappudacity1.domain.model.TrailerList;

import rx.Observable;


/**
 * An interface representing a user Cache.
 */
public interface EntityCache {

  Observable<MoviesList> getMoviesCache();
  Observable<TrailerList> getTrailersCache(String movId);
  Observable<ReviewList> getReviewsCache(String movId);
  Observable<MoviesList> getFavMovies();







  void putMovies(MoviesList movie);
  void putTrailers(TrailerList trailer);
  void putReviews(ReviewList review);
  void putFavMovies(MoviesList moviesList);


  void deleteFavMovie(Movie movie);
  boolean isExistFav(Movie movie);







  boolean isCached();
  boolean isCashedReview(String id);
  boolean isCashedTrailer(String id);





  void evictAll();
  void evictAllReviews();
  void evictAllTrailers();


}
