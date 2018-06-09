package com.nextbit.yassin.movieappudacity1.infrastructure.service;


import com.nextbit.yassin.movieappudacity1.domain.service.RetrofitClient;
import com.nextbit.yassin.movieappudacity1.domain.service.SOService;

/**
 *Created by Elhadedy on 4/9/2017.
 */

public class ApiUtils {

    private static final String BASE_URL = "http://api.themoviedb.org";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}