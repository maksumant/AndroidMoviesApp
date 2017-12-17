package com.mymovies.android.popularmovies.domain;

import java.io.Serializable;

/**
 * Domain object to hold movie details.
 * Created by makrandsumant on 16/12/17.
 */

public class Movie implements Serializable {

    private String id;
    private String title;
    private String originalTitle;
    private String relativePosterPath;
    private String overview;
    private String releaseDate;
    private String userRating;

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public Movie() {
    }

    public Movie(String id, String name, String relativePosterPath) {
        this.id = id;
        this.title = name;
        this.relativePosterPath = relativePosterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelativePosterPath() {
        return relativePosterPath;
    }

    public void setRelativePosterPath(String relativePosterPath) {
        this.relativePosterPath = relativePosterPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != null ? !id.equals(movie.id) : movie.id != null) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (originalTitle != null ? !originalTitle.equals(movie.originalTitle) : movie.originalTitle != null)
            return false;
        if (relativePosterPath != null ? !relativePosterPath.equals(movie.relativePosterPath) : movie.relativePosterPath != null)
            return false;
        if (overview != null ? !overview.equals(movie.overview) : movie.overview != null)
            return false;
        if (releaseDate != null ? !releaseDate.equals(movie.releaseDate) : movie.releaseDate != null)
            return false;
        return userRating != null ? userRating.equals(movie.userRating) : movie.userRating == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (originalTitle != null ? originalTitle.hashCode() : 0);
        result = 31 * result + (relativePosterPath != null ? relativePosterPath.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (userRating != null ? userRating.hashCode() : 0);
        return result;
    }
}
