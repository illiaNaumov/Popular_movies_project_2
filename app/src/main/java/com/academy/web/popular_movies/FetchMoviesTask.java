package com.academy.web.popular_movies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import com.academy.web.myapplication.BuildConfig;
import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
    public MovieAdapter movieAdapter;
    public Context context;
    final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    final String BASE_TRAILER_URL = "http://api.themoviedb.org/3/movie/"; //118340/videos?";
    final String BASE_REVIEWS_URL = "http://api.themoviedb.org/3/movie/"; //118340/reviews?";
    final String SORT_PARAM = "sort_by";

    private static final  String [] projection = {
            MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME + "." + MovieContract.FavoriteMovieEntry._ID,
            MovieContract.FavoriteMovieEntry.MOVIE_ID,
            MovieContract.FavoriteMovieEntry.TITLE,
            MovieContract.FavoriteMovieEntry.POSTER,
            MovieContract.FavoriteMovieEntry.RELEASE_DATE,
            MovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS,
            MovieContract.FavoriteMovieEntry.VOTE_AVERAGE
    };

    static final int ID = 0;
    static final int MOVIE_ID = 1;
    static final int TITLE = 2;
    static final int POSTER = 3;
    static final int RELEASE_DATE = 4;
    static final int PLOT_SYNOPSIS = 5;
    static final int VOTE_AVERAGE = 6;

    public FetchMoviesTask(MovieAdapter movieAdapter, Context context) {
        this.movieAdapter = movieAdapter;
        this.context = context;
    }

    @Override
    protected Movie[] doInBackground(String... input) {
        Movie [] movies = null;
        String favoriteSortTypeValue = context.getString(R.string.pref_units_favorite);
        String sortType = input[0];

            if (sortType.equals(favoriteSortTypeValue)) {
                movies = getFavoriteMoviesViaContentProvider();

            } else {
                movies = fetchMovies(input[0]);

            }
        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if(movies != null){
            movieAdapter.clear();
            for(Movie movie: movies){
                movieAdapter.add(movie);
            }
        }
        super.onPostExecute(movies);
    }

    private Movie [] getFavoriteMoviesViaContentProvider() {
        Cursor cursor = context.getContentResolver().query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        Movie [] movies = new Movie[cursor.getCount()];
        for(int i = 0; cursor.moveToNext(); i++){
            Movie movie = new Movie();
            movie.movieID = cursor.getLong(MOVIE_ID);
            movie.movieTitle = cursor.getString(TITLE) ;
            movie.posterImageLink = cursor.getString(POSTER);
            movie.releaseDate = cursor.getString(RELEASE_DATE);
            movie.plotSynopsis = cursor.getString(PLOT_SYNOPSIS);
            movie.userRating = cursor.getLong(VOTE_AVERAGE);
            movies[i] = movie;
        }
        return movies;
    }

    private Movie [] fetchMovies(String sortType){
        String moviesJSONString = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        Movie [] movies = null;

        try{
            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortType)
                    .appendQueryParameter("api_key", BuildConfig.POPULAR_MOVIE_API_KEY)
                    .build();

            url = new URL(buildUri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if(inputStream == null){
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            if(stringBuffer.length() == 0){
                moviesJSONString = null;
            }

            moviesJSONString = stringBuffer.toString();
            movies = getMoviesFromJSON(moviesJSONString);



        }catch(Exception e){
            e.printStackTrace();
        }

        return movies;
    }


    private Movie [] getMoviesFromJSON(String inputJSONString) throws JSONException {
        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String PLOT_SYNOPSYS = "overview";

        JSONObject jsonObject = new JSONObject(inputJSONString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

        Movie movies [] = new Movie[jsonArray.length()];
        long movieID = 0;
        String movieTitle = "";
        String posterImageLink = "";
        String plotSynopsis = "";
        double userRating = 0;
        String releaseDate = "";
        Trailer [] trailers = null;
        for(int i = 0; i < movies.length; i++){
            JSONObject movieJSON = jsonArray.getJSONObject(i);
            movieID = movieJSON.getLong(ID);
            movieTitle = movieJSON.getString(ORIGINAL_TITLE);
            posterImageLink = movieJSON.getString(POSTER_PATH);
            plotSynopsis = movieJSON.getString(PLOT_SYNOPSYS);
            userRating = movieJSON.getDouble(RATING);
            releaseDate = movieJSON.getString(RELEASE_DATE);
            movies[i] = new Movie(movieID, movieTitle, posterImageLink, plotSynopsis, userRating, releaseDate);
        }

        return movies;


    }


}

