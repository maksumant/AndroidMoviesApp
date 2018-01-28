package com.mymovies.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by makrandsumant on 24/01/18.
 */

public class FavouritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";

    private static final int DATABASE_VERSION = 1;

    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("**************** FavouritesDbHelper constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURTIES_TABLE = "CREATE TABLE " + FavouritesContract.FavouriteEntry.TABLE_NAME + " (" +
                FavouritesContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouritesContract.FavouriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                FavouritesContract.FavouriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURTIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + FavouritesContract.FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
