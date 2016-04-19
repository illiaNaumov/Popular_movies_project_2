package com.academy.web.popular_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.academy.web.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 20.02.2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    public static final String FORECAST_BASE_URL = "http://image.tmdb.org/t/p/w500/";
    public MovieAdapter(Context context, ArrayList<Movie> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }

        String imageURL = FORECAST_BASE_URL + movie.posterImageLink;
        ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_view_MoviePosterView);
        Picasso.with(getContext()).load(imageURL).into(imageView);

        return convertView;
    }

    @Override
    public Movie getItem(int position) {
        return super.getItem(position);
    }
}
