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
     */
    @GET("/list/{page}")
    void getListMovies(@Query("api_key") String api_key, @Path("page") String page, @Query("lang") String lang, Callback<Response> callback);
}
