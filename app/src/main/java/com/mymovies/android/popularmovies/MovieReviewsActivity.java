package com.mymovies.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.domain.MovieReview;
import com.mymovies.android.popularmovies.domain.MovieVideo;
import com.mymovies.android.popularmovies.utils.MovieDBJsonUtils;
import com.mymovies.android.popularmovies.utils.NetworkUtils;
import com.mymovies.android.popularmovies.utils.StringConstants;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by makrandsumant on 21/01/18.
 */

public class MovieReviewsActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<List<MovieReview>>  {
    private Movie selectedMovie;
    private final static int MOVIE_REVIEWS_LOADER = 31;
    private List<MovieReview> reviews;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageDisplay;
    private TextView mNoReviewsMessageDisplay;
    private ScrollView mMovieReviewsView;
    private LinearLayout mReviewsLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(StringConstants.EXTRA_CONTENT_NAME)) {
            mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message);
            mNoReviewsMessageDisplay = (TextView) findViewById(R.id.tv_no_reviews_found);
            mMovieReviewsView = (ScrollView) findViewById(R.id.sv_movie_reviews);
            mReviewsLayout = (LinearLayout) findViewById(R.id.ll_review_items);

            this.selectedMovie = (Movie) intentThatStartedThisActivity.getParcelableExtra(StringConstants.EXTRA_CONTENT_NAME);

            this.loadMovieReviews();
        }
    }

    private void loadMovieReviews() {
        Bundle bundle = new Bundle();
        bundle.putString("MOVIE_ID", this.selectedMovie.getId());

        android.support.v4.app.LoaderManager manager = getSupportLoaderManager();
        Loader<List<MovieVideo>> movieVideoLoader = manager.getLoader(MOVIE_REVIEWS_LOADER);

        if(movieVideoLoader == null) {
            manager.initLoader(MOVIE_REVIEWS_LOADER, bundle, this);
        } else {
            manager.restartLoader(MOVIE_REVIEWS_LOADER, bundle, this);
        }
    }

    @Override
    public Loader<List<MovieReview>> onCreateLoader(int id, final Bundle bundle) {
        return new AsyncTaskLoader<List<MovieReview>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if( bundle == null) {
                    return;
                }

                if(reviews == null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<MovieReview> loadInBackground() {
                String movieId = bundle.getString("MOVIE_ID");
                if (movieId == null || movieId.isEmpty()) {
                    return null;
                }
                URL reviewsURL = NetworkUtils.buildUrlForReviews(movieId);

                try {
                    String jsonMoviesResonse = NetworkUtils.getResponseFromHttpUrl(reviewsURL);
                    return MovieDBJsonUtils.parseResponseJsonForReviews(jsonMoviesResonse);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (data == null) {
            showErrorMessage();
        } else if (data.isEmpty()) {
            shoeNoReviewsMessage();
        } else {
            this.reviews = (ArrayList<MovieReview>) data;
            this.fillReviewsOnUI(data);
            showMovieDetailsView();
        }
    }



    private void fillReviewsOnUI(List<MovieReview> data) {
        if (data.isEmpty()) {
            return;
        }

        for (final MovieReview review : data) {
            TextView reviewText = new TextView(this);
            reviewText.setText(review.getContent());

            TextView authorText = new TextView(this);
            authorText.setText(review.getAuthor());
            authorText.setGravity(Gravity.RIGHT);

            View view = new View(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    5
            ));
            view.setBackgroundColor(Color.parseColor("#B3B3B3"));

            mReviewsLayout.addView(reviewText);
            mReviewsLayout.addView(authorText);
            mReviewsLayout.addView(view);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<MovieReview>> loader) {

    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showMovieDetailsView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMovieReviewsView.setVisibility(View.VISIBLE);
        mNoReviewsMessageDisplay.setVisibility(View.INVISIBLE);
    }


    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showErrorMessage() {
        mMovieReviewsView.setVisibility(View.INVISIBLE);
        mNoReviewsMessageDisplay.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void shoeNoReviewsMessage() {
        mMovieReviewsView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mNoReviewsMessageDisplay.setVisibility(View.VISIBLE);
    }
}
