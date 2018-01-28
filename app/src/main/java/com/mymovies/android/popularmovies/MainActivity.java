package com.mymovies.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.LoaderManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mymovies.android.popularmovies.adapter.MovieListAdapter;
import com.mymovies.android.popularmovies.data.FavouritesContract;
import com.mymovies.android.popularmovies.data.FavouritesDbHelper;
import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.utils.MovieDBJsonUtils;
import com.mymovies.android.popularmovies.utils.NetworkUtils;
import com.mymovies.android.popularmovies.utils.StringConstants;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Movie[]> {

    private RecyclerView mRecyclerView;

    private MovieListAdapter mMovieListAdapter;

    private ProgressBar mProgressBar;

    private TextView mErrorMessageDisplay;

    private TextView mNoFavouritesMessageDisplay;

    private SQLiteDatabase mDb;

    private static final int MOVIE_DATA_LOADER = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.rv_movie_posters);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message);
        mNoFavouritesMessageDisplay = findViewById(R.id.tv_no_favourites_found);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mMovieListAdapter = new MovieListAdapter(this);

        mDb = new FavouritesDbHelper(this).getReadableDatabase();

        mRecyclerView.setAdapter(mMovieListAdapter);

        loadMoviesData(StringConstants.SORT_BY_TOP_RATED_API_PATH);
    }

    /**
     * Fetches movies data from API endpoint and populates it inside the adapter.
     */
    private void loadMoviesData(String sortBy) {
        Bundle bundle = new Bundle();
        bundle.putString("SORT_BY", sortBy);

        android.support.v4.app.LoaderManager manager = getSupportLoaderManager();
        Loader<Movie[]> moviesDataLoader = manager.getLoader(MOVIE_DATA_LOADER);

        if(moviesDataLoader == null) {
            manager.initLoader(MOVIE_DATA_LOADER, bundle, this);
        } else {
            manager.restartLoader(MOVIE_DATA_LOADER, bundle, this);
        }

    }

    @Override
    public void onClick(Movie clickedMovie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra(StringConstants.EXTRA_CONTENT_NAME, clickedMovie);
        startActivity(intent);
    }

    @Override
    public Loader<Movie[]> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<Movie[]>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if( bundle == null) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public Movie[] loadInBackground() {
                String sortBy = bundle.getString("SORT_BY");
                if (sortBy == null || sortBy.isEmpty()) {
                    return null;
                }
                if (!sortBy.equals(StringConstants.SORT_BY_FAVOURITES)) {
                    URL moviesRequestUrl = NetworkUtils.buildUrl(sortBy);
                    try {
                        String jsonMoviesResonse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                        return MovieDBJsonUtils.parseResponseJson(jsonMoviesResonse);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // fetch ids from DB
                    Cursor favouritesCursor = null;
                    try {
                        favouritesCursor = fetchAllFavouriteMoviesFromDB();
                        List<Movie> favouriteMovies = new ArrayList<>();

                        if(favouriteMovies  != null) {
                            while (favouritesCursor.moveToNext()) {
                                Long movieId = favouritesCursor.getLong(favouritesCursor.getColumnIndex(FavouritesContract.FavouriteEntry.COLUMN_MOVIE_ID));
                                if (movieId != null) {
                                    URL individualMovieUrl = NetworkUtils.buildUrlToGetMovieDetails(movieId);
                                    try {
                                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(individualMovieUrl);
                                        favouriteMovies.add(MovieDBJsonUtils.parseResponseJsonForIndividualMovie(jsonMovieResponse));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        return favouriteMovies.toArray(new Movie[favouriteMovies.size()]);
                    } finally {
                        if (favouritesCursor != null) {
                            favouritesCursor.close();
                        }
                    }
                }
                return null;
            }

            private Cursor fetchAllFavouriteMoviesFromDB() {
                try {
                    return getContentResolver().query(FavouritesContract.FavouriteEntry.CONTENT_URI, null, null,null,FavouritesContract.FavouriteEntry._ID);
                } catch (Exception e ) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (movies == null) {
            showErrorMessage();
        } else if (movies.length == 0) {
            shoeNoFavouritesMessage();
        } else {
            showMoviesDataView();
            mMovieListAdapter.setMovies(movies);
        }

    }


    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {

    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoFavouritesMessageDisplay.setVisibility(View.INVISIBLE);
    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mNoFavouritesMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void shoeNoFavouritesMessage() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoFavouritesMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();
        Context context = MainActivity.this;
        if (selectedMenuItem == R.id.submenu_sort_b_popularity) {
            loadMoviesData(StringConstants.SORT_BY_POPULARITY_API_PATH);
        } else if (selectedMenuItem == R.id.submenu_sort_by_rating) {
            loadMoviesData(StringConstants.SORT_BY_TOP_RATED_API_PATH);
        } else if (selectedMenuItem == R.id.menu_favourites) {
            loadMoviesData(StringConstants.SORT_BY_FAVOURITES);
        }
        return super.onOptionsItemSelected(item);
    }
}
