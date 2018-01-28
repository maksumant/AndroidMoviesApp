package com.mymovies.android.popularmovies.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by makrandsumant on 20/01/18.
 */

public class ReviewsDataLoader implements LoaderManager.LoaderCallbacks<List<String>> {

    Context currentContext = null;

    public ReviewsDataLoader(Context context) {
        this.currentContext = context;
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<String>>(this.currentContext) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                
            }

            @Override
            public List<String> loadInBackground() {
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }
}
