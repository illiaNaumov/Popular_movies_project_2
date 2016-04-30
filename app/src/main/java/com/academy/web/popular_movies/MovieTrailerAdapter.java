package com.academy.web.popular_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.academy.web.myapplication.R;
import java.util.ArrayList;

/**
 * Created by ilyua on 30.04.2016.
 */
public class MovieTrailerAdapter extends ArrayAdapter<Trailer> {
    public MovieTrailerAdapter(Context context, ArrayList<Trailer> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_trailer, parent, false);
        }

        TextView trailerName = (TextView) convertView.findViewById(R.id.item_movie_trailer_name);
        trailerName.setText(trailer.trailerName);

        return convertView;
    }

    @Override
    public Trailer getItem(int position) {
        return super.getItem(position);
    }
}
