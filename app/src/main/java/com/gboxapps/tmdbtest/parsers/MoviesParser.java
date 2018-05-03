package com.gboxapps.tmdbtest.parsers;

import com.gboxapps.tmdbtest.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesParser {

    public static List<Movie> parseMovies(String jObject){

        List<Movie> movies = new ArrayList<>();

        try{

            JSONObject jsonObject = new JSONObject(jObject);
            JSONArray jsonArrayMovies = jsonObject.getJSONArray("results");

            for(int i = 0 ; i < jsonArrayMovies.length() ; i++){
                JSONObject jMovie = (JSONObject) jsonArrayMovies.get(i);
                Movie movie = new Movie();
                movie.setId(jMovie.getString("id"));
                movie.setTitle(jMovie.getString("title"));
                movie.setRelease_date(jMovie.getString("release_date"));
                movie.setOverview(jMovie.getString("overview"));
                movie.setPoster_path(jMovie.getString("poster_path"));

                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return movies;
    }

}
