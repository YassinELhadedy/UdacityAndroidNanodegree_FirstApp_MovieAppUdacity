package com.nextbit.yassin.movieappudacity1.presenter.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;
import com.nextbit.yassin.movieappudacity1.R;
import com.nextbit.yassin.movieappudacity1.domain.confiq.Constants;
import com.nextbit.yassin.movieappudacity1.domain.model.Movie;
import com.nextbit.yassin.movieappudacity1.domain.model.MoviesList;
import com.nextbit.yassin.movieappudacity1.infrastructure.cache.CacheImpl;
import com.nextbit.yassin.movieappudacity1.infrastructure.cache.provider.MyFavMovContentProvider;
import com.nextbit.yassin.movieappudacity1.infrastructure.repository.MovieDataRepository;
import com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource.MovieDataStoreFactory;
import com.nextbit.yassin.movieappudacity1.presenter.activity.DetailsMovieAct;
import com.nextbit.yassin.movieappudacity1.presenter.views.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_MOV_IMAGE2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_MOV_OV2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_MOV_RAT2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_NAME2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_R_D2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_TRA_ID2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_V_C2;
import static com.nextbit.yassin.movieappudacity1.presenter.fragment.DetailsFra.favoritefra;


public class MoviesGrid extends Fragment {
    private LinearLayoutManager manager;
    @SuppressLint("StaticFieldLeak")
    public  static RecyclerView recyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    public MenuItem up, popular, topRated, favorite;
    public static int pageNum = 3;
    private Animation anim;
    public  MoviesGridAdapter adapter;
    public static final String SORT_SATE = "state";
    private  static int itemPos =0;

    public MoviesGrid() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         if (savedInstanceState !=null){
           itemPos = savedInstanceState.getInt("itempostion",0);

         }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies_grid, container, false);
        setHasOptionsMenu(true);

        anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        anim.setDuration(200);
        recyclerView =  v.findViewById(R.id.mainGrid);
        mRefreshLayout =  v.findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(0xff6b4d9c);
        mRefreshLayout.setRefreshing(true);

        if (savedInstanceState==null) {
            if (getFromSharedPref().equals("popular") || getFromSharedPref().equals("top_rated")) {
                getMovies(getFromSharedPref());

            }

        }





        if (isBortOrientation()){
           if (isTablet(getContext())){
               // tablet_bort
              manager = new GridLayoutManager(getActivity(), 1);


           }
           else {
               //mob_bort
               manager = new GridLayoutManager(getActivity(), 2);


           }
       }
       else {
           if (isTablet(getContext())){
               // tablet_land
               manager = new GridLayoutManager(getActivity(), 2);



           }
           else {
               // mob_land
              manager = new GridLayoutManager(getActivity(), 3);


           }

       }
       mRefreshLayout.setOnRefreshListener(() -> {
           mRefreshLayout.setRefreshing(true);
           itemPos= 0;
           String sortRefresh = getFromSharedPref();
           switch (sortRefresh) {
               case "popular":
                   getMovies("popular");
                   break;
               case "top_rated":
                   getMovies("top_rated");
                   break;
               default:
                   mRefreshLayout.setRefreshing(false);
                   break;
           }


       });

        return v;
    }

    private void menuCheck() {
        if (getFromSharedPref().equals("popular") ) {
            popular.setChecked(true);

        }
        else if ( getFromSharedPref().equals("top_rated")){
            topRated.setChecked(true);

        }
        else {
            favorite.setChecked(true);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment, menu);
        up = menu.findItem(R.id.action_up).setVisible(false);
        popular = menu.findItem(R.id.action_pop);
        topRated = menu.findItem(R.id.action_high);
        favorite = menu.findItem(R.id.action_fav);
//        SharedPreferences prefs = getActivity().getSharedPreferences(SORT, 0);
//        SharedPreferences.Editor editor = prefs.edit();
//        if (popular.isChecked()) {
//            editor.putString(SORT_SATE, "popular");
//        } else if (topRated.isChecked()) {
//            editor.putString(SORT_SATE, "top_rated");
//        } else {
//            editor.putString(SORT_SATE, "favChecked");
//        }
//        editor.apply();
        menuCheck();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_pop:
                if (popular.isChecked()) {
                    return false;
                } else {
                    popular.setChecked(true);
                    itemPos =0;
                    getMovies("popular");
                    savedinSharedPref("popular");
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
                break;
            case R.id.action_high:
                if (topRated.isChecked()) {
                    return false;
                } else {
                    topRated.setChecked(true);
                    itemPos =0;
                    if (adapter != null) {
                        getMovies("top_rated");
                        savedinSharedPref("top_rated");
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                }
                break;
            case R.id.action_fav:
                if (favorite.isChecked()) {
                    return false;
                } else {
                    favorite.setChecked(true);
                    itemPos =0;
                    savedinSharedPref("favChecked");
                    Uri todoUri = Uri.parse(MyFavMovContentProvider.CONTENT_URI+"");

                    adapter.updateMovies(getFavMovies(todoUri).getmovies());

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isBortOrientation() {
        int rotation = getResources().getConfiguration().orientation;
        boolean value = true ;


        switch (rotation) {
            case Configuration.ORIENTATION_PORTRAIT: value = true;
                break;
            case Configuration.ORIENTATION_LANDSCAPE: value =false;
                break;
        }
        return value;
    }
    public  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.PosterHolder> {
        private   List<Movie> movies;
        private Context context;

        MoviesGridAdapter(List<Movie> movies, Context context) {
            this.movies = movies;
            this.context = context;


        }

        @Override
        public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);

            return new PosterHolder(v);
        }

        @Override
        public void onBindViewHolder(PosterHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.fav.setVisibility(VISIBLE);
            holder.unFav.setVisibility(GONE);
            holder.fav.invalidate();
            holder.unFav.invalidate();
            Movie movie=movies.get(position);
            String posterUrl = "http://image.tmdb.org/t/p/w185/" + movie.getPosterPath();
            if (getActivity() == null) {
                Toast.makeText(context, "activity is null ", Toast.LENGTH_SHORT).show();
                return;
            }
            Glide.with(context).load(posterUrl)
                    .listener(GlidePalette.with(posterUrl)
                            .use(GlidePalette.Profile.MUTED_DARK)
                            .intoBackground(holder.posterTitleBackground)
                            .intoTextColor(holder.posterTitle)

                            .use(GlidePalette.Profile.VIBRANT)
                            .intoBackground(holder.posterTitleBackground, GlidePalette.Swatch.RGB)
                            .intoTextColor(holder.posterTitle, GlidePalette.Swatch.BODY_TEXT_COLOR)
                            .crossfade(true)
                    )
                    .into(holder.poster);


            final String date, year;
            date = movie.getReleaseDate();
            if (date.equals("")) {
                year = "Unknown";
            } else {
                year = date.substring(0, 4);
            }
            holder.posterTitle.setText(movie.getTitle());
            holder.posterTitle.setSelected(true);
            holder.posterYear.setText(year);
            if (position >= 14) {
                up.setVisible(true);
            } else {try {
                up.setVisible(false);
            }catch (Exception ignored){}

            }

            try {
                up.setOnMenuItemClickListener(item -> {
                    recyclerView.smoothScrollToPosition(0);
                    return true;
                });
            }catch (Exception ignored){}


            final ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(165);

            holder.fav.setOnClickListener(v -> {
                holder.fav.setVisibility(GONE);
                holder.unFav.startAnimation(animation);
                holder.unFav.setVisibility(VISIBLE);
                if (isTablet(context)) {
                    favoritefra.setImageResource(R.drawable.favorite);
                }
                putFav(movie);
            });

            holder.unFav.setOnClickListener(v -> {
                holder.unFav.setVisibility(GONE);
                holder.fav.startAnimation(animation);
                holder.fav.setVisibility(VISIBLE);
                if (isTablet(context)){
                    favoritefra.setImageResource(R.drawable.un_favorite);
                }



                deleteFav(movie);
                if (favorite.isChecked()) {
                    movies.remove(position);
                    setAdapter(adapter);
                    recyclerView.scrollToPosition(position - 1);
                    adapter.notifyItemRemoved(position - 1);
                    adapter.notifyDataSetChanged();
                }


            });

           boolean isExisted = isExistedMovie(Uri.parse(MyFavMovContentProvider.CONTENT_URI+""),movie.getId().toString());
            if (isExisted) {
                holder.unFav.setVisibility(VISIBLE);
                holder.fav.setVisibility(GONE);
            } else {
                holder.unFav.setVisibility(GONE);
                holder.fav.setVisibility(VISIBLE);
            }

            holder.gridCard.setOnClickListener(v -> {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                //Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();

                if (isTablet(context)){
                    DetailsFra detailsFra=new DetailsFra();
                    Bundle args = new Bundle();

                    args.putString("image",movie.getPosterPath());
                    args.putString("name",movie.getTitle());
                    args.putDouble("rate",movie.getVoteAverage());
                    args.putInt("count",movie.getVoteCount());
                    args.putString("overview",movie.getOverview());
                    args.putString("poster",movie.getPosterPath());
                    args.putString("date",date);

                    args.putInt("id",movie.getId());




                    detailsFra.setArguments(args);
                    manager.beginTransaction()
                            .replace(R.id.description_fragment, detailsFra).commit();
                }
                else {
                    Intent data=new Intent(context,DetailsMovieAct.class);
                    data.putExtra("poster", movie.getPosterPath());
                    data.putExtra("backdrop", movie.getBackdropPath());
                    data.putExtra("title", movie.getTitle());
                    data.putExtra("overview", movie.getOverview());
                    data.putExtra("date", year);
                    data.putExtra("vote", movie.getVoteAverage());
                    data.putExtra("id", movie.getId());
                    data.putExtra("position", position);
                    context.startActivity(data);



                }

            });



        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        class PosterHolder extends RecyclerView.ViewHolder {
            CardView gridCard;
            ImageView poster, fav, unFav;
            View posterTitleBackground, dummyView;
            TextView posterTitle, posterYear;

            PosterHolder(View itemView) {
                super(itemView);
                poster = itemView.findViewById(R.id.gridPoster);
                posterTitle = itemView.findViewById(R.id.posterTitle);
                gridCard = itemView.findViewById(R.id.gridCard);
                posterTitleBackground = itemView.findViewById(R.id.posterTitleBackground);
                fav = itemView.findViewById(R.id.posterFav);
                unFav = itemView.findViewById(R.id.posterUnFav);
                posterYear = itemView.findViewById(R.id.posterYear);
                dummyView = itemView.findViewById(R.id.dummyView);
            }
        }
        private void putFav(Movie movie){


            ContentValues values = new ContentValues();
            values.put(KEY_NAME2, movie.getTitle());
            values.put(Constants.KEY_MOV_IMAGE2, movie.getPosterPath());
            values.put(Constants.KEY_MOV_OV2, movie.getOverview());

            values.put(Constants.KEY_MOV_RAT2, movie.getVoteAverage());
            values.put(Constants.KEY_V_C2, movie.getVoteCount());
            values.put(KEY_TRA_ID2,movie.getId());
            values.put(Constants.KEY_R_D2, movie.getReleaseDate());


            context.getContentResolver().insert(
                    MyFavMovContentProvider.CONTENT_URI, values);

        }
        private void deleteFav(Movie movie){
            Uri uri = Uri.parse(MyFavMovContentProvider.CONTENT_URI + "/"
                    +movie.getTitle());
            context.getContentResolver().delete(uri, null, null);


        }

       boolean isExistedMovie(Uri uri,String movieId){

           String[] projection =  {KEY_NAME2,
                   KEY_MOV_IMAGE2, KEY_MOV_OV2,KEY_MOV_RAT2,KEY_V_C2,KEY_TRA_ID2,KEY_R_D2,
           };

           @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(uri, projection, KEY_TRA_ID2 + " =?", new String[]{movieId},
                   null);
           return cursor != null && cursor.moveToFirst();

       }
        void updateMovies(List<Movie> items) {
            movies = items;
            notifyDataSetChanged();
        }

    }
    public static void setAdapter(MoviesGridAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    void  getMovies(String spec) {

        MovieDataRepository movieDataRepository=new MovieDataRepository(new MovieDataStoreFactory(getActivity(),new CacheImpl(getActivity())));
        movieDataRepository.movieListTopRatedStore(spec, String.valueOf(pageNum))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoviesList>() {
                    @Override
                    public void onCompleted() {
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                      try {
                          Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                          mRefreshLayout.setRefreshing(false);
                      }catch (Exception ignored){}
                    }

                    @Override
                    public void onNext(MoviesList moviesList) {

                        adapter = new MoviesGridAdapter(moviesList.getmovies(), getActivity());
                        final ScaleInAnimationAdapter alphaAdapter = adapterAnim(adapter);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(alphaAdapter);
                        recyclerView.smoothScrollToPosition(itemPos);
                        recyclerView.startAnimation(anim);
                        recyclerView.setVisibility(VISIBLE);


                    }
                });
    }

    MoviesList getFavMovies(Uri uri) {

         MoviesList moviesList;
         List<Movie>favoritism;

        String[] projection =  {KEY_NAME2,
                KEY_MOV_IMAGE2, KEY_MOV_OV2,KEY_MOV_RAT2,KEY_V_C2,KEY_TRA_ID2,KEY_R_D2,
        };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
                null);
        moviesList=new MoviesList();
        favoritism=new ArrayList<>();
        assert cursor != null;
        if (cursor.moveToFirst()) {

            do {
                Log.i("first line in do","nn");

                Movie movie = new Movie();
                movie.setTitle(cursor.getString(cursor.getColumnIndex(KEY_NAME2)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(Constants.KEY_MOV_IMAGE2)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(Constants.KEY_MOV_OV2)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constants.KEY_MOV_RAT2))));
                movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_V_C2))));
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TRA_ID2))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_R_D2)));
                favoritism.add(movie);
                Log.i("last in do","ss");

            } while (cursor.moveToNext());
        }
        moviesList.setmovies(favoritism);
        cursor.close();


        return moviesList;


    }
    private ScaleInAnimationAdapter adapterAnim(MoviesGridAdapter adapter) {
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setDuration(200);
        return alphaAdapter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Toast.makeText(getContext(), "item postion saved now "+manager.findLastVisibleItemPosition()+"/  "+manager.findFirstCompletelyVisibleItemPosition()
//               +"/  "+ manager.findLastCompletelyVisibleItemPosition()
//                , Toast.LENGTH_LONG).show();

//        Parcelable recylerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
//        outState.putParcelable("recycleview",recylerViewState);


        if (manager.findFirstCompletelyVisibleItemPosition()<0){


            outState.putInt("itempostion", manager.findFirstCompletelyVisibleItemPosition());


        }
        else {
            outState.putInt("itempostion", manager.findLastCompletelyVisibleItemPosition());
            outState.putInt("itempostion", manager.findLastVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private void savedinSharedPref(String sort){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SORT_SATE, sort);
        editor.apply();
    }
    private String getFromSharedPref(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(SORT_SATE, "popular");

    }
}
