package com.nextbit.yassin.movieappudacity1.infrastructure.repository.datasource;

import android.content.Context;
import android.util.Log;


import com.nextbit.yassin.movieappudacity1.infrastructure.cache.EntityCache;

import java.io.IOException;

/**
 * Created by yassin on 10/8/17.
 */

//retrieve from disk if 1- offline 2- not empty db
    //otherwise retrieve from cloud

public class MovieDataStoreFactory {
    private final Context context;
    private final EntityCache entityCache;

    public EntityCache getEntityCache() {
        return entityCache;
    }

    public MovieDataStoreFactory(Context context, EntityCache entityCache) {
        this.context = context.getApplicationContext();
        this.entityCache = entityCache;
    }

    public MovieDataStore create() {
        MovieDataStore movieDataStore;


        if (!isOnline()&& entityCache.isCached() ) {

            movieDataStore = new DiskDataStore(this.entityCache);
        } else {
            entityCache.evictAll();
            movieDataStore = createCloudDataStore();
        }

        return movieDataStore;
    }
    public MovieDataStore createReview(String movId){
        MovieDataStore movieDataStore;
        if(!isOnline()&&this.entityCache.isCashedReview(movId)){
           movieDataStore= new DiskDataStore(this.entityCache);

        }else {
            entityCache.evictAllReviews();
            movieDataStore = createCloudDataStore();

        }
        return movieDataStore;

    }
    public MovieDataStore createTrailer(String movId){
        MovieDataStore movieDataStore;
        if(!isOnline()&&this.entityCache.isCashedTrailer(movId)){
            Log.i("disk trailer",""+entityCache.isCashedTrailer(movId));
            movieDataStore= new DiskDataStore(this.entityCache);

        }else {
            entityCache.evictAllTrailers();
            Log.i("cloud trailer","");
//            entityCache.evictAll();
            movieDataStore = createCloudDataStore();

        }
        return movieDataStore;

    }
    public MovieDataStore createFavMovies(){
        return new DiskDataStore(entityCache);
    }


    public MovieDataStore createCloudDataStore() {



        return new CloudDataStore(this.entityCache);
    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


}
