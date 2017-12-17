package com.mymovies.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mymovies.android.popularmovies.adapter.MovieListAdapter;
import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.utils.MovieDBJsonUtils;
import com.mymovies.android.popularmovies.utils.NetworkUtils;
import com.mymovies.android.popularmovies.utils.StringConstants;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {

    private RecyclerView mRecyclerView;

    private MovieListAdapter mMovieListAdapter;

    private ProgressBar mProgressBar;

    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_posters);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mMovieListAdapter = new MovieListAdapter(this);

        mRecyclerView.setAdapter(mMovieListAdapter);

        loadMoviesData();

    }

    /**
     * Fetches movies data from API endpoint and populates it inside the adapter.
     */
    private void loadMoviesData() {
        new FetchMovieDetailsTask().execute(StringConstants.SORT_BY_TOP_RATED_API_PATH);
    }

    @Override
    public void onClick(Movie clickedMovie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra(StringConstants.EXTRA_CONTENT_NAME, clickedMovie);
        startActivity(intent);
    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            String sortBy = params[0];

            URL moviesRequestUrl = NetworkUtils.buildUrl(sortBy);
            try {
                String jsonMoviesResonse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                return MovieDBJsonUtils.parseResponseJson(jsonMoviesResonse);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies == null) {
                showErrorMessage();
            } else {
                showMoviesDataView();
                mMovieListAdapter.setMovies(movies);
            }
        }
    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method makes the view for the movies data visible and hides error message.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
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
            new FetchMovieDetailsTask().execute(StringConstants.SORT_BY_POPULARITY_API_PATH);
        } else if (selectedMenuItem == R.id.submenu_sort_by_rating) {
            new FetchMovieDetailsTask().execute(StringConstants.SORT_BY_TOP_RATED_API_PATH);
        }
        return super.onOptionsItemSelected(item);
    }
}
