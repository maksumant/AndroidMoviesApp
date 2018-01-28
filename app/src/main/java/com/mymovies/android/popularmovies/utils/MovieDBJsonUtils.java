package com.mymovies.android.popularmovies.utils;

import android.util.Log;

import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.domain.MovieReview;
import com.mymovies.android.popularmovies.domain.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makrandsumant on 16/12/17.
 */

public class MovieDBJsonUtils {
    private static final String TAG = MovieDBJsonUtils.class.getSimpleName();

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

    /**
     * Utility method which parses response received from movieDB api and returns array of movie video objects.
     *
     * @param jsonResponse
     * @return
     */
    public static List<MovieVideo> parseResponseJsonForVideos(String jsonResponse) {

        try {
            JSONObject moviesResponse = new JSONObject(jsonResponse);
            JSONArray results = moviesResponse.getJSONArray("results");
            String movieId = moviesResponse.getString("id");


            List<MovieVideo> movieVideos = new ArrayList<>();
            MovieVideo movieVideo = null;
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonMovie = results.getJSONObject(i);
                movieVideo = new MovieVideo();
                movieVideo.setKey(jsonMovie.getString("key"));
                movieVideo.setSite(jsonMovie.getString("site"));
                movieVideo.setType(jsonMovie.getString("type"));
                movieVideo.setName(jsonMovie.getString("name"));
                movieVideo.setMovieId(Integer.parseInt( movieId));
                movieVideos.add(movieVideo);
            }

            Log.v(TAG, "Videos for the movie: " + movieVideos );
            return movieVideos;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    /**
     * Utility method which parses response received from movieDB api and returns array of movie video objects.
     *
     * @param jsonResponse
     * @return
     */
    public static List<MovieReview> parseResponseJsonForReviews(String jsonResponse) {

        try {
            JSONObject moviesResponse = new JSONObject(jsonResponse);
            JSONArray results = moviesResponse.getJSONArray("results");
            String movieId = moviesResponse.getString("id");


            List<MovieReview> movieVideos = new ArrayList<>();
            MovieReview movieReview = null;
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonMovie = results.getJSONObject(i);
                movieReview = new MovieReview().setMovieId(movieId)
                        .setReviewId(jsonMovie.getString("id"))
                        .setAuthor(jsonMovie.getString("author"))
                        .setContent(jsonMovie.getString("content"))
                        .setUrl(jsonMovie.getString("url"));
                movieVideos.add(movieReview);
            }
            return movieVideos;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Utility method which parses response received from movieDB api and returns movie video object.
     *
     * @param jsonResponse
     * @return
     */
    public static Movie parseResponseJsonForIndividualMovie(String jsonResponse) {
        try {
            JSONObject jsonMovie = new JSONObject(jsonResponse);
            Movie movieObj = new Movie();
            movieObj.setId(jsonMovie.getString("id"));
            movieObj.setTitle(jsonMovie.getString("title"));
            movieObj.setOriginalTitle(jsonMovie.getString("original_title"));
            movieObj.setRelativePosterPath(jsonMovie.getString("poster_path"));
            movieObj.setOverview(jsonMovie.getString("overview"));
            movieObj.setUserRating(jsonMovie.getString("vote_average"));
            movieObj.setReleaseDate(jsonMovie.getString("release_date"));
            return movieObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Movie();
    }
}
