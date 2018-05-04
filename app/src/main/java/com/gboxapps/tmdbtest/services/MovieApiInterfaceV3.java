package com.gboxapps.tmdbtest.services;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieApiInterfaceV3 {

    /**
     * Get a movie list filtered by name
     * @param api_key The api-key of the application
     * @param page page requested
     * @param query query to search
     * @param lang language of request
     * @param callback callback to get the response
     */
    @GET("/search/movie")
    void searchMovies(@Query("api_key") String api_key, @Query("page") String page, @Query("query") String query, @Query("language") String lang, Callback<Response> callback);

}
