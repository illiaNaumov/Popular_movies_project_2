package com.academy.web.popular_movies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.academy.web.popular_movies.Data.MovieContract.FavoriteMovieEntry;

/**
 * Created by ilyua on 19.04.2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovieEntry.MOVIES_TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovieEntry.MOVIE_ID + " INTEGER, " +
                FavoriteMovieEntry.TITLE + " TEXT, " +
                FavoriteMovieEntry.POSTER + " TEXT, " +
                FavoriteMovieEntry.RELEASE_DATE + " REAL, " +
                FavoriteMovieEntry.PLOT_SYNOPSIS + " TEXT, " +
                FavoriteMovieEntry.POPULARITY + " REAL, " +
                FavoriteMovieEntry.VOTE_AVERAGE + " REAL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.MOVIES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}