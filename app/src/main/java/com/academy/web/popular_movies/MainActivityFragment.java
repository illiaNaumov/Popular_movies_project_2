package com.academy.web.popular_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.academy.web.myapplication.R;

import java.util.ArrayList;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
   public MovieAdapter movieAdapter;
    public static final String EXTRA_MOVIE = "com.academy.web.myapplication.extra_movie";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        updateMovies();
        final GridView gridView = (GridView) rootView.findViewById(R.id.gridMovieView);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDetailActivityFragment detailFragment = (MovieDetailActivityFragment) getFragmentManager().findFragmentById(R.id.details_frag);
                Movie mov = (Movie) adapterView.getItemAtPosition(i);

                if (detailFragment == null) {
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra(EXTRA_MOVIE, mov);
                    startActivity(intent);
                } else {
                    detailFragment.updateDetailFragment(mov);
                }
            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(){
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(movieAdapter, getContext());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_units_popularity));
        fetchMoviesTask.execute(sortType);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


