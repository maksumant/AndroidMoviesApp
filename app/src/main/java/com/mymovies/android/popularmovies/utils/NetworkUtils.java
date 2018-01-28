package com.mymovies.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie DB servers.
 * Created by makrandsumant on 16/12/17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THE_MOVIE_DB_BASE_URL =
            "https://api.themoviedb.org/3";
    private static final String API_TO_USE = "movie/";

    private static final String PATH_VIDEOS = "videos";

    private static final String PATH_REVIEWS = "reviews";

    private static final String API_KEY = "api_key";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_SIZE = "w185";


    //TODO: Enter your API KEY before running app.
    private static final String API_KEY_VALUE = "<< API KEY >>";

    /**
     * Builds the URL used to talk to the movies DB API server using a sort by parameter.
     *
     * @param sortBy
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String sortBy) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_BASE_URL).buildUpon().appendEncodedPath(API_TO_USE).appendEncodedPath(sortBy)
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movies DB API server using a movie ID parameter.
     *
     * @param movieId
     * @return The URL
     */
    public static URL buildUrlToGetMovieDetails(Long movieId) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_BASE_URL).buildUpon().appendEncodedPath(API_TO_USE).appendEncodedPath(String.valueOf(movieId))
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movies DB API server using a movie ID parameter.
     * *
     * @param movieId
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrlForTrailers(String movieId) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_BASE_URL).buildUpon().appendEncodedPath(API_TO_USE)
                .appendEncodedPath(movieId)
                .appendEncodedPath(PATH_VIDEOS)
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI for videos:" + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movies DB API server using a movie ID parameter.
     * *
     * @param movieId
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrlForReviews(String movieId) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_BASE_URL).buildUpon().appendEncodedPath(API_TO_USE)
                .appendEncodedPath(movieId)
                .appendEncodedPath(PATH_REVIEWS)
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI for videos:" + url);

        return url;
    }


    /**
     * Builds the URL used to talk to the movies DB API server using relative poster path.
     *
     * @param relativePosterPath
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrlForImage(String relativePosterPath) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendEncodedPath(IMAGE_SIZE).appendPath(relativePosterPath).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
