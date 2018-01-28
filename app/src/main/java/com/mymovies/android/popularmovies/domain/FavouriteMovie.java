package com.mymovies.android.popularmovies.domain;

/**
 * Created by makrandsumant on 26/01/18.
 */

public class FavouriteMovie {
    private long dbId;
    private long movieId;
    private String movieTitle;

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}
