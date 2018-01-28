package com.mymovies.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mymovies.android.popularmovies.data.FavouritesContract;
import com.mymovies.android.popularmovies.data.FavouritesDbHelper;
import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.domain.MovieVideo;
import com.mymovies.android.popularmovies.utils.MovieDBJsonUtils;
import com.mymovies.android.popularmovies.utils.NetworkUtils;
import com.mymovies.android.popularmovies.utils.StringConstants;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity responsible for showing/handling movie detail.
 */
public class MovieDetailsActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<List<MovieVideo>> {

    private TextView mMovieTitle;

    private ImageView mMovieThumbnail;

    private TextView mMovieOverview;

    private TextView mMovieRatings;

    private TextView mMovieReleaseDate;

    private ToggleButton mFavouriteButton;

    private TextView mErrorMessageDisplay;

    private ProgressBar mProgressBar;

    private ScrollView mMovieDetailsView;

    private LinearLayout mTrailersLayout;

    private final static int MOVIE_VIDEOS_LOADER = 24;

    private final static String MOVIE_VIDEOS_DATA_KEY = "videosData";

    private final static String SELECTED_MOVIE_DATA_KEY = "selectedMovie";

    private ArrayList<MovieVideo> videosData;

    private Movie selectedMovie;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intentThatStartedThisActivity = getIntent();

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SELECTED_MOVIE_DATA_KEY)) {
            this.selectedMovie = (Movie) savedInstanceState.get(SELECTED_MOVIE_DATA_KEY);
        }

        if (this.selectedMovie != null || intentThatStartedThisActivity.hasExtra(StringConstants.EXTRA_CONTENT_NAME)) {
            if (intentThatStartedThisActivity.hasExtra(StringConstants.EXTRA_CONTENT_NAME)) {
                this.selectedMovie = (Movie) intentThatStartedThisActivity.getParcelableExtra(StringConstants.EXTRA_CONTENT_NAME);
            }

            mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
            mMovieThumbnail = (ImageView) findViewById(R.id.iv_movie_thumbnail);
            mMovieOverview = (TextView) findViewById(R.id.tv_overview);
            mMovieRatings = (TextView) findViewById(R.id.tv_user_rating);
            mMovieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
            mFavouriteButton = (ToggleButton) findViewById(R.id.b_is_favourite);
            mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message);
            mMovieDetailsView = (ScrollView) findViewById(R.id.sv_movie_details);
            mTrailersLayout = (LinearLayout) findViewById(R.id.ll_trailer_items);


            mMovieTitle.setText(selectedMovie.getOriginalTitle());
            mMovieReleaseDate.setText(this.getString(R.string.releaseDate) + selectedMovie.getReleaseDate());
            mMovieRatings.setText(selectedMovie.getUserRating() + this.getString(R.string.ratingsOutOf));
            mMovieOverview.setText(selectedMovie.getOverview());



            String posterURL = NetworkUtils.buildUrlForImage(selectedMovie.getRelativePosterPath()).toString();
            Picasso.with(this).load(URLDecoder.decode(posterURL)).into(mMovieThumbnail);

            mDb = new FavouritesDbHelper(this).getWritableDatabase();
            this.checkIfMovieIsFavourite(this.selectedMovie);
            this.initializeFavouritesButtonOnUI(selectedMovie.isFavourite());

            if (savedInstanceState != null &&
                    savedInstanceState.containsKey(MOVIE_VIDEOS_DATA_KEY)) {
                this.videosData = (ArrayList<MovieVideo>) savedInstanceState.get(MOVIE_VIDEOS_DATA_KEY);
                this.fillTrailerButtonsOnUI(this.videosData);
            } else {
                if (this.videosData == null) {
                    this.loadVideoDetailsInBackgroud(selectedMovie.getId());
                }
            }
        }
    }

    private void checkIfMovieIsFavourite(Movie selectedMovie) {
        Long movieDbId = fetchMovieFromDb(selectedMovie.getId());
        if(movieDbId!= null) {
            selectedMovie.setFavourite(true);
            selectedMovie.setFavouritesDbId(movieDbId);
        } else {
            selectedMovie.setFavouritesDbId(null);
            selectedMovie.setFavourite(false);
        }
    }

    private Long fetchMovieFromDb(String id) {
        Cursor movieCursor = null;
        try {
            movieCursor = getContentResolver().query(FavouritesContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(id).build(),
                    new String[]{FavouritesContract.FavouriteEntry.COLUMN_MOVIE_ID, FavouritesContract.FavouriteEntry._ID},
                    null,
                    null,
                    null
            );
            if (movieCursor != null && movieCursor.getCount() > 0) {
                movieCursor.moveToFirst();
                return movieCursor.getLong(movieCursor.getColumnIndex(FavouritesContract.FavouriteEntry._ID));
            }
        } finally {
            if (movieCursor != null) {
                movieCursor.close();
            }
        }
        return null;
    }

    private void loadVideoDetailsInBackgroud(String movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("MOVIE_ID", movieId);

        android.support.v4.app.LoaderManager manager = getSupportLoaderManager();
        Loader<List<MovieVideo>> movieVideoLoader = manager.getLoader(MOVIE_VIDEOS_LOADER);

        if(movieVideoLoader == null) {
            manager.initLoader(MOVIE_VIDEOS_LOADER, bundle, this);
        } else {
            manager.restartLoader(MOVIE_VIDEOS_LOADER, bundle, this);
        }

    }

    public void initializeFavouritesButtonOnUI(final boolean isFavourite) {
        if(mFavouriteButton == null) {
            return;
        }
        if (isFavourite) {
            mFavouriteButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
        } else {
            mFavouriteButton.setBackgroundResource(android.R.drawable.btn_star_big_off);
        }
        mFavouriteButton.setChecked(isFavourite);
        mFavouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    long favouritesDbId = addMovieToFavourites(selectedMovie.getId(), selectedMovie.getTitle());
                    selectedMovie.setFavouritesDbId(favouritesDbId);
                    mFavouriteButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
                } else {
                    if(removeMovieFromFavourites(selectedMovie.getFavouritesDbId())) {
                        selectedMovie.setFavouritesDbId(null);
                        mFavouriteButton.setBackgroundResource(android.R.drawable.btn_star_big_off);
                    }
                }
            }
        });
    }

    private boolean removeMovieFromFavourites(Long id) {
        if(id != null && id != 0) {
            return getContentResolver().delete(FavouritesContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(), null, null) > 0;
        }
        return false;
    }

    private long addMovieToFavourites(String id, String title) {
        ContentValues cv = new ContentValues();
        cv.put(FavouritesContract.FavouriteEntry.COLUMN_MOVIE_ID, id);
        cv.put(FavouritesContract.FavouriteEntry.COLUMN_MOVIE_TITLE, title);
        Uri uri = getContentResolver().insert(FavouritesContract.FavouriteEntry.CONTENT_URI, cv);
        if (uri != null) {
            return Long.parseLong(uri.getLastPathSegment());
        }
        return 0l;
    }

    @Override
    public Loader<List<MovieVideo>> onCreateLoader(int id, final Bundle bundle) {
        return new AsyncTaskLoader<List<MovieVideo>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if( bundle == null) {
                    return;
                }

                if(videosData == null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<MovieVideo> loadInBackground() {
                String movieId = bundle.getString("MOVIE_ID");
                if (movieId == null || movieId.isEmpty()) {
                    return null;
                }
                URL trailersURL = NetworkUtils.buildUrlForTrailers(movieId);

            try {
                String jsonMoviesResonse = NetworkUtils.getResponseFromHttpUrl(trailersURL);
                return MovieDBJsonUtils.parseResponseJsonForVideos(jsonMoviesResonse);

            } catch (IOException e) {
                e.printStackTrace();
            }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MovieVideo>> loader, List<MovieVideo> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (data == null) {
            showErrorMessage();
        } else {
            this.videosData = (ArrayList<MovieVideo>) data;
            this.fillTrailerButtonsOnUI(data);
            showMovieDetailsView();
        }
    }

    private void fillTrailerButtonsOnUI(List<MovieVideo> data) {
        if(data.isEmpty()) {
            return;
        }

        for (final MovieVideo video : data) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            Button videoButton = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 41, getResources().getDisplayMetrics()));
            layoutParams.setMargins(40, 0, 0, 0);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = video.getKey();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    try {
                        view.getContext().startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        view.getContext().startActivity(webIntent);
                    }
                }
            };
            videoButton.setLayoutParams(layoutParams);
            videoButton.setBackgroundResource(android.R.drawable.ic_media_play);
            videoButton.setOnClickListener(onClickListener);
            row.addView(videoButton);

            TextView videoName = new TextView(this);
            videoName.setText(video.getName());
            LinearLayout.LayoutParams lParamsForVideoName = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            lParamsForVideoName.setMargins(40, 0, 8, 0);
            videoName.setLayoutParams(lParamsForVideoName);

            row.addView(videoName);

            mTrailersLayout.addView(row);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<MovieVideo>> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_VIDEOS_DATA_KEY, this.videosData);
        outState.putParcelable(SELECTED_MOVIE_DATA_KEY, this.selectedMovie);
        super.onSaveInstanceState(outState);
    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showMovieDetailsView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMovieDetailsView.setVisibility(View.VISIBLE);
    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showErrorMessage() {
        mMovieDetailsView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();
        Context context = MovieDetailsActivity.this;
        if (selectedMenuItem == R.id.menu_reviews) {
            Intent intent = new Intent(MovieDetailsActivity.this, MovieReviewsActivity.class);
            intent.putExtra(StringConstants.EXTRA_CONTENT_NAME, this.selectedMovie);
            startActivity(intent);
        } else if (selectedMenuItem == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
