package com.academy.web.popular_movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.academy.web.myapplication.BuildConfig;
import com.academy.web.myapplication.R;
import com.academy.web.popular_movies.Data.FetchMovieTrailers;
import com.academy.web.popular_movies.Data.MovieContract;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    @Bind(R.id.detail_fragment_text_movie_title) TextView movieTitle;
    @Bind(R.id.detail_fragment_text_release_date) TextView movieYear;
    @Bind(R.id.detail_fragment_text_rating) TextView movieRating;
    @Bind(R.id.detail_fragment_text_synopsis) TextView movieSynopsis;
    @Bind(R.id.detail_fragment_poster_image) ImageView imageView;
    @Bind(R.id.detail_fragment_favorite_movie_button) FloatingActionButton favoriteMovieButton;
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    Context mContext;
    private long movieApiID;
    private String imageURL;
    public MovieTrailerAdapter movieTrailerAdapter;

    private static final  String [] projection = {
            MovieContract.FavoriteMovieEntry.MOVIES_TABLE_NAME + "." + MovieContract.FavoriteMovieEntry._ID,
            MovieContract.FavoriteMovieEntry.MOVIE_ID,
            MovieContract.FavoriteMovieEntry.TITLE,
            MovieContract.FavoriteMovieEntry.POSTER,
            MovieContract.FavoriteMovieEntry.RELEASE_DATE,
            MovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS,
            MovieContract.FavoriteMovieEntry.VOTE_AVERAGE

    };


    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        View headerView = inflater.inflate(R.layout.header_for_fragment_movie_detail, container, false);
        ListView listView = (ListView) view.findViewById(R.id.fragment_movie_detail_trailers_list_view);
        listView.addHeaderView(headerView);
        ButterKnife.bind(this, view);
        favoriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFilmFavorite()){
                    deleteFilmFromFavorites();
                    favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_white_48dp);
                    Toast.makeText(getContext(), R.string.detail_fragment_film_deleted_from_favorites, Toast.LENGTH_SHORT).show();
                }else{
                    markFilmAsFavorite();
                    favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_black_48dp);
                    Toast.makeText(getContext(), R.string.detail_fragment_text_marked_as_favorite, Toast.LENGTH_SHORT).show();
                }

            }
        });

        movieTrailerAdapter = new MovieTrailerAdapter(getActivity(), new ArrayList<Trailer>());
        final ListView trailersListView = (ListView) view.findViewById(R.id.fragment_movie_detail_trailers_list_view);
        trailersListView.setAdapter(movieTrailerAdapter);
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trailer trailer = (Trailer) adapterView.getItemAtPosition(i);

                Intent intent = null;
                    intent = YouTubeStandalonePlayer.createVideoIntent(
                            getActivity(), BuildConfig.YOUTUBE_API_KEY, trailer.key, 0, false, false);


                if (intent != null) {
                        startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                    } else {
                        // Could not resolve the intent - must need to install or update the YouTube API service.
                        YouTubeInitializationResult.SERVICE_MISSING
                                .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                    }
                }
        });

        return view;
    }


    @Override
    public void onStart() {
        Intent intent =  getActivity().getIntent();
        if(intent.hasExtra(MainActivityFragment.EXTRA_MOVIE)){
            Movie movie = intent.getParcelableExtra(MainActivityFragment.EXTRA_MOVIE);
            updateDetailFragment(movie);
        }

        super.onStart();
    }

    public void updateDetailFragment(Movie movie){
        movieApiID = movie.movieID;
        movieTitle.setText(movie.movieTitle);
        movieYear.setText(movie.releaseDate.substring(0,4));
        movieRating.setText(String.valueOf(movie.userRating).concat("/10"));
        movieSynopsis.setText(String.valueOf(movie.plotSynopsis));


        imageURL = MovieAdapter.FORECAST_BASE_URL + movie.posterImageLink;
        Picasso.with(getContext()).load(imageURL).into(imageView);
        if(isFilmFavorite()){
            favoriteMovieButton.setImageResource(R.mipmap.ic_favorite_black_48dp);
        }

        FetchMovieTrailers fetchMovieTrailers = new FetchMovieTrailers(movieTrailerAdapter, getContext());
        fetchMovieTrailers.execute(movie.movieID);

    }

    public boolean isFilmFavorite(){
        boolean isFavorite = false;
        Cursor cursor = getContext().getContentResolver().query(MovieContract.FavoriteMovieEntry.buildMovieApiIDUri(movieApiID),
                projection,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            isFavorite = true;
        }
        return isFavorite;
    }

    public void markFilmAsFavorite(){
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.FavoriteMovieEntry.MOVIE_ID, movieApiID);
        movieValues.put(MovieContract.FavoriteMovieEntry.TITLE, String.valueOf(movieTitle.getText()));
        movieValues.put(MovieContract.FavoriteMovieEntry.POSTER, imageURL);
        movieValues.put(MovieContract.FavoriteMovieEntry.RELEASE_DATE, String.valueOf(movieYear.getText()));
        movieValues.put(MovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS, String.valueOf(movieSynopsis.getText()));
        movieValues.put(MovieContract.FavoriteMovieEntry.VOTE_AVERAGE, Utils.parsePopularity(movieRating.getText().toString()));

        getContext().getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, movieValues);
    }

    public void deleteFilmFromFavorites(){
        String [] selection = {String.valueOf(movieApiID)};
        getContext().getContentResolver().delete(MovieContract.FavoriteMovieEntry.buildMovieApiIDUri(movieApiID), null, selection);
    }


}
