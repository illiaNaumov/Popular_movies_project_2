package com.academy.web.popular_movies.Data;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.academy.web.myapplication.BuildConfig;
import com.academy.web.popular_movies.MovieAdapter;
import com.academy.web.popular_movies.MovieTrailerAdapter;
import com.academy.web.popular_movies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ilyua on 30.04.2016.
 */
public class FetchMovieTrailers extends AsyncTask<Long, Void, Trailer[]>{
    public MovieTrailerAdapter movieTrailerAdapter;
    public Context context;
    final String BASE_TRAILER_URL = "http://api.themoviedb.org/3/movie/";

    public FetchMovieTrailers(MovieTrailerAdapter movieTrailerAdapter, Context context) {
        this.movieTrailerAdapter = movieTrailerAdapter;
        this.context = context;
    }

    @Override
    protected Trailer[] doInBackground(Long... params) {
        long movieID = params[0];
        String trailersJSONString = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        Trailer [] trailers = null;

        try{
            Uri buildUri = Uri.parse(BASE_TRAILER_URL).buildUpon().appendPath(String.valueOf(movieID))
                    .appendPath("videos")
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
                trailersJSONString = null;
            }

            trailersJSONString = stringBuffer.toString();
            trailers = getTrailersFromJSON(trailersJSONString);


        }catch(Exception e){
            e.printStackTrace();
        }

        return trailers;
    }



    private Trailer [] getTrailersFromJSON(String trailersJSONString) throws JSONException {
        final String RESULTS = "results";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String YOUTUBE = "YouTube";

        JSONObject jsonObject = new JSONObject(trailersJSONString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
        Trailer [] trailers = new Trailer[jsonArray.length()];

        for(int i = 0; i < trailers.length; i++){
            JSONObject jsonTrailerObject = jsonArray.getJSONObject(i);
            if(jsonTrailerObject.getString(SITE).equals(YOUTUBE)){
                Trailer trailer = new Trailer();
                trailer.key = jsonTrailerObject.getString(KEY);
                trailer.trailerName = jsonTrailerObject.getString(NAME);
                trailers[i] = trailer;
            }
        }
        return trailers;


    }

    @Override
    protected void onPostExecute(Trailer[] trailers) {
        if(movieTrailerAdapter != null){
            movieTrailerAdapter.clear();
            for(Trailer trailer: trailers){
                movieTrailerAdapter.add(trailer);
            }
        }
    }
}
