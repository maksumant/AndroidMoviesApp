package com.mymovies.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by makrandsumant on 24/01/18.
 */

public class FavouritesContract {

    public static final String AUTHORITY = "com.mymovies.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVOURITES = "favourites";

    public static final class FavouriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String TABLE_NAME = "favouriteMovies";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE  = "movieTitle";
    }
}
