package com.academy.web.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.web.myapplication.R;
import com.squareup.picasso.Picasso;

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


    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
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
        movieTitle.setText(movie.movieTitle);
        movieYear.setText(movie.releaseDate.substring(0,4));
        movieRating.setText(String.valueOf(movie.userRating).concat("/10"));
        movieSynopsis.setText(String.valueOf(movie.plotSynopsis));

        String imageURL = MovieAdapter.FORECAST_BASE_URL + movie.posterImageLink;
        Picasso.with(getContext()).load(imageURL).into(imageView);
    }
}
