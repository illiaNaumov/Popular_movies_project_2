package com.academy.web.popular_movies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by ilyua on 19.04.2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper movieDBHelper;

    static final int FAVORITE_MOVIES = 100;
    static final int FAVORITE_MOVIES_BY_ID = 101;


    private static final String sFavoriteMovieByIDSelection = MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME +
            "." + MovieContract.FavoriteMovieEntry.MOVIE_ID + " = ?";


    private Cursor getFavoriteMovies(Uri uri, String[] projection, String sortOrder){
        final SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        return db.query(MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFavoriteMovieByID(Uri uri, String[] projection, String sortOrder){
        final SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        long movieID = MovieContract.FavoriteMovieEntry.getMovieIdFromUri(uri);
        String [] selectionArgs = new String[]{Long.toString(movieID)};
        return db.query(MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME,
                projection,
                sFavoriteMovieByIDSelection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                retCursor = getFavoriteMovies(uri, projection, sortOrder);
                break;
            case FAVORITE_MOVIES_BY_ID:
                retCursor = getFavoriteMovieByID(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match){
            case FAVORITE_MOVIES:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            case FAVORITE_MOVIES_BY_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case FAVORITE_MOVIES:
                long _id = db.insert(MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = MovieContract.FavoriteMovieEntry.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if(selection == null) {selection = "1";}
        switch(match){
            case FAVORITE_MOVIES_BY_ID:
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME, sFavoriteMovieByIDSelection, selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch(match){
            case FAVORITE_MOVIES_BY_ID:
                rowsUpdated = db.update(MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIES_BY_ID);

        return matcher;
    }
}