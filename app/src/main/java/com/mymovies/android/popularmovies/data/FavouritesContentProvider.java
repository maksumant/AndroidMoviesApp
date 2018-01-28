package com.mymovies.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;

/**
 * Created by makrandsumant on 26/01/18.
 */

public class FavouritesContentProvider extends ContentProvider {

    private FavouritesDbHelper mFavouratiesDbHelper;
    public static final int FAVOURITES = 100;
    public static final int FAVOURITES_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher () {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavouritesContract.AUTHORITY, FavouritesContract.PATH_FAVOURITES, FAVOURITES);
        uriMatcher.addURI(FavouritesContract.AUTHORITY, FavouritesContract.PATH_FAVOURITES + "/#", FAVOURITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mFavouratiesDbHelper = new FavouritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavouratiesDbHelper.getReadableDatabase();

        System.out.println("********* Using content provider");
        int match = sUriMatcher.match(uri);

        Cursor retCursor = null;

        switch (match) {
            case FAVOURITES:
                retCursor = db.query(FavouritesContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVOURITES_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = FavouritesContract.FavouriteEntry.COLUMN_MOVIE_ID  + "=?";
                String[] mSelectionArgs = new String[] {id};

                retCursor = db.query(FavouritesContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mFavouratiesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri = null;

        switch (match){
            case FAVOURITES:
                long id = db.insert(FavouritesContract.FavouriteEntry.TABLE_NAME, null, contentValues);
                if (id >0) {
                    returnUri = ContentUris.withAppendedId(FavouritesContract.FavouriteEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert data into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mFavouratiesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int rowsDeleted = 0;
        switch (match) {
            case FAVOURITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(FavouritesContract.FavouriteEntry.TABLE_NAME, FavouritesContract.FavouriteEntry._ID + "=" + id, null);
                if (rowsDeleted <= 0) {
                    throw new SQLException("Failed to delete data for " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
