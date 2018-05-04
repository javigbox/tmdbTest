package com.gboxapps.tmdbtest.services;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieApiInterface {

    /**
     * Get popular movies
     * @param api_key The api-key of the application
     * @param page Page to query
     * @param lang ISO 639-1 value to display translated data.
     * @param region ISO 3166-1 code to filter release dates.
     * @param callback
     */
    @GET("/movie/popular")
    void getPopularMovies(@Query("api_key") String api_key, @Query("page") String page, @Query("language") String lang, @Query("region") String region, Callback<Response> callback);

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
