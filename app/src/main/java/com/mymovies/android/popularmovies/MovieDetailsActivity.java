package com.mymovies.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.utils.NetworkUtils;
import com.mymovies.android.popularmovies.utils.StringConstants;
import com.squareup.picasso.Picasso;

import java.net.URLDecoder;

/**
 * Activity responsible for showing/handling movie detail.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mMovieTitle;

    private ImageView mMovieThumbnail;

    private TextView mMovieOverview;

    private TextView mMovieRatings;

    private TextView mMovieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(StringConstants.EXTRA_CONTENT_NAME)) {
            Movie selectedMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(StringConstants.EXTRA_CONTENT_NAME);

            mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
            mMovieThumbnail = (ImageView) findViewById(R.id.iv_movie_thumbnail);
            mMovieOverview = (TextView) findViewById(R.id.tv_overview);
            mMovieRatings = (TextView) findViewById(R.id.tv_user_rating);
            mMovieReleaseDate = (TextView) findViewById(R.id.tv_release_date);

            mMovieTitle.setText(selectedMovie.getOriginalTitle());
            mMovieReleaseDate.setText(this.getString(R.string.releaseDate) + selectedMovie.getReleaseDate());
            mMovieRatings.setText(selectedMovie.getUserRating() + this.getString(R.string.ratingsOutOf));
            mMovieOverview.setText(selectedMovie.getOverview());

            String posterURL = NetworkUtils.buildUrlForImage(selectedMovie.getRelativePosterPath()).toString();
            Picasso.with(this).load(URLDecoder.decode(posterURL)).into(mMovieThumbnail);

        }
    }
}
