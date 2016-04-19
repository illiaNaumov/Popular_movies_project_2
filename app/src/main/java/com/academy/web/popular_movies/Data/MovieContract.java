package com.academy.web.popular_movies.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilyua on 19.04.2016.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.academy.web.popular_movies";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITES_MOVIES = "favorites";

    public static final class Movies implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String MOVIES_TABLE_NAME = "movies";
    }



}
