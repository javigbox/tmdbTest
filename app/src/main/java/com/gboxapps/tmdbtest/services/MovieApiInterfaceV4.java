package com.gboxapps.tmdbtest.services;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieApiInterfaceV4 {

    /**
     * Get a paginated list of movies
     * @param api_key The api-key of the application
     * @param page page requested
     * @param lang language of request
     * @param callback callback to get the response
     *                 1?page=1&api_key=93aea0c77bc168d8bbce3918cefefa45&language=es
     */
    @GET("/list/1")
    void getListMovies(@Query("api_key") String api_key, @Query("page") String page, @Query("language") String lang, Callback<Response> callback);
}
