package com.mymovies.android.popularmovies.domain;

/**
 * Created by makrandsumant on 21/01/18.
 */

public class MovieReview {
    private String movieId;
    private String reviewId;
    private String author;
    private String content;
    private String url;

    public String getMovieId() {
        return movieId;
    }

    public MovieReview setMovieId(String movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getReviewId() {
        return reviewId;
    }

    public MovieReview setReviewId(String reviewId) {
        this.reviewId = reviewId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public MovieReview setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MovieReview setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MovieReview setUrl(String url) {
        this.url = url;
        return this;
    }
}
