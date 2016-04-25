package com.academy.web.popular_movies;

import android.net.Uri;
import android.os.AsyncTask;
import com.academy.web.myapplication.BuildConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
    public MovieAdapter movieAdapter;
    final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    final String SORT_PARAM = "sort_by";

    public FetchMoviesTask(MovieAdapter movieAdapter) {
        this.movieAdapter = movieAdapter;
    }

    @Override
    protected Movie[] doInBackground(String... input) {
        String moviesJSONString = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String sortType = input[0];


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

            Movie [] movies = getMoviesFromJSON(moviesJSONString);

            return movies;

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }

        return null;
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

