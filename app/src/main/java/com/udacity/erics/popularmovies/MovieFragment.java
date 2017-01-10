package com.udacity.erics.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacity.erics.popularmovies.model.Movie;
import com.udacity.erics.popularmovies.service.MovieService;
import com.udacity.erics.popularmovies.util.AsyncTaskDelegate;
import com.udacity.erics.popularmovies.util.Functions;
import com.udacity.erics.popularmovies.util.IntentParameterName;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class MovieFragment extends android.support.v4.app.Fragment implements AsyncTaskDelegate {

    private MovieAdapter movieAdapter = null;
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        this.updateMovies(this.rootView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movie, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        this.movieAdapter = new MovieAdapter(this.getActivity());

        GridView gridView = (GridView) this.rootView.findViewById(R.id.list);
        gridView.setAdapter(this.movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callDetailsIntent(position);

            }
        });

        return this.rootView;
    }

    private void callDetailsIntent(final int position) {
        if (!Functions.isOnline(getContext())) {
            Snackbar snackbar = Snackbar
                    .make(this.rootView, R.string.msg_internet_out, Snackbar.LENGTH_LONG)
                    .setAction(R.string.msg_try_again, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callDetailsIntent(position);
                        }
                    });
            snackbar.show();
        } else {
            Movie movie = movieAdapter.getItem(position);

            if (movie != null) {
                Intent movieDetailActivity = new Intent(getContext(), MovieDetailActivity.class);
                movieDetailActivity.putExtra(IntentParameterName.MOVIE_ID, movie.getId());
                startActivity(movieDetailActivity);
            }
        }
    }

    private void updateMovies(final View rootView) {
        if (!Functions.isOnline(this.getContext())) {
            Snackbar snackbar = Snackbar
                    .make(rootView, R.string.msg_internet_out, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.msg_try_again, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateMovies(rootView);
                        }
                    });
            snackbar.show();
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType = prefs.getString(getString(R.string.pref_key_list_sort_type_movie), getString(R.string.pref_entry_value_popular));
            MovieService movieService = new MovieService(getContext(), this);
            movieService.execute(sortType);
        }
    }

    @Override
    public void processFinish(Object objectOutput) {
        List<Movie> movies = (List<Movie>) objectOutput;
        movieAdapter.clear();
        if (movies != null && !movies.isEmpty()) {
            movieAdapter.addAll(movies);
        }
    }
}



