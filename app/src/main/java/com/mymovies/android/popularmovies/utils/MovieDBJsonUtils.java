package com.mymovies.android.popularmovies.utils;

import com.mymovies.android.popularmovies.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makrandsumant on 16/12/17.
 */

public class MovieDBJsonUtils {

    /**
     * Utility method which parses response received from movieDB api and returns array of movie objects.
     *
     * @param jsonResponse
     * @return
     */
    public static Movie[] parseResponseJson(String jsonResponse) {

        try {
            JSONObject moviesResponse = new JSONObject(jsonResponse);
            JSONArray results = moviesResponse.getJSONArray("results");

            List<Movie> movieList = new ArrayList<>();
            Movie movieObj = null;
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonMovie = results.getJSONObject(i);
                movieObj = new Movie();
                movieObj.setId(jsonMovie.getString("id"));
                movieObj.setTitle(jsonMovie.getString("title"));
                movieObj.setOriginalTitle(jsonMovie.getString("original_title"));
                movieObj.setRelativePosterPath(jsonMovie.getString("poster_path"));
                movieObj.setOverview(jsonMovie.getString("overview"));
                movieObj.setUserRating(jsonMovie.getString("vote_average"));
                movieObj.setReleaseDate(jsonMovie.getString("release_date"));
                movieList.add(movieObj);
            }
            return movieList.toArray(new Movie[movieList.size()]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Movie[0];
    }


}
