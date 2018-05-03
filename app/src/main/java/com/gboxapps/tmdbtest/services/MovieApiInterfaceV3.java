package com.gboxapps.tmdbtest.services;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MovieApiInterfaceV3 {

    /**
     * Get a movie list filtered by name
     * @param api_key The api-key of the application
     * @param page page requested
     * @param query query to search
     * @param lang language of request
     * @param callback callback to get the response
     */
    @GET("/search/movie?api_key={api_key}&language={lang}&query={query}&page={page}&include_adult=false")
    void searchMovies(@Path("api_key") String api_key, @Path("page") String page, @Path("query") String query, @Path("lang") String lang, Callback<Response> callback);

}
