package com.nextbit.yassin.movieappudacity1.presenter.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DetailsFra extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static ImageView detailPoster, favoriteFra;
    public static int favoriteId;
    private TextView date, overview;
    private RecyclerView detailTrailers, overViewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private View view;

    public DetailsFra() {
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_details_actv, container, false);
        detailPoster = view.findViewById(R.id.detailPoster1);
        detailTrailers = view.findViewById(R.id.detail_trailers);

        date = view.findViewById(R.id.date);
        favoriteFra = view.findViewById(R.id.favorite);
        overview = view.findViewById(R.id.overview);
        overViewRecyclerView = view.findViewById(R.id.OvrecyclerView);
        view.setVisibility(View.INVISIBLE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            setView(getArguments().getParcelable("Movie"));
        }


        return view;
    }

    private void setView(Movie movie) {
        if (movie != null) {
            view.setVisibility(View.VISIBLE);

            //Toast.makeText(getActivity(), "" +args.getString("image"), Toast.LENGTH_SHORT).show();
            Glide.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath()).into(detailPoster);
            date.setText(movie.getReleaseDate());
            overview.setText(movie.getOverview());
            int id = movie.getId();
            List<Review> reviewList = new ArrayList<>();
            List<Trailer> trailerList = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
            trailerAdapter = new TrailerAdapter(getActivity(), trailerList, movie.getTitle(), movie.getPosterPath());

            detailTrailers.setLayoutManager(new LinearLayoutManager(getActivity()));
            detailTrailers.setAdapter(trailerAdapter);

            overViewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            overViewRecyclerView.setAdapter(reviewAdapter);
            favoriteId = id;
            isFavMovies(id);
            loadReviews(String.valueOf(id));
            load(String.valueOf(id));
        }
    }


    private void isFavMovies(int id) {

        Movie movie = new Movie();
        movie.setId(id);
        MovieDataRepository movieDataRepository = new MovieDataRepository(new MovieDataStoreFactory(getActivity(), new CacheImpl(getActivity())));

        if (movieDataRepository.isFoundFavMov(movie)) {

//            favorite.setVisibility(View.VISIBLE);
            favoriteFra.setImageResource(R.drawable.favorite);


        } else {

//            favorite.setVisibility(View.INVISIBLE);
            favoriteFra.setImageResource(R.drawable.un_favorite);

        }
    }

    public void loadReviews(String id) {

        MovieDataRepository movieDataRepository = new MovieDataRepository(new MovieDataStoreFactory(getActivity(), new CacheImpl(getActivity())));
        movieDataRepository.movieReviewsStore(id, String.valueOf(MoviesGrid.pageNum))
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
        MovieDataRepository movieDataRepository = new MovieDataRepository(new MovieDataStoreFactory(getActivity(), new CacheImpl(getActivity())));
        movieDataRepository.movieTrailerStore(id, String.valueOf(MoviesGrid.pageNum))
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

}
