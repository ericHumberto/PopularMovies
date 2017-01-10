package com.udacity.erics.popularmovies.service;

import android.content.Context;
import android.os.AsyncTask;

import com.udacity.erics.popularmovies.integration.MovieDB;
import com.udacity.erics.popularmovies.util.ApiOperation;
import com.udacity.erics.popularmovies.util.AsyncTaskDelegate;

public class MovieService extends AsyncTask<String, String, Object> {

    private AsyncTaskDelegate delegate = null;

    public MovieService(Context context, AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected Object doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        MovieDB movieDB = new MovieDB();

        switch (params[0]) {
            case ApiOperation.GET_POPULAR:
                return movieDB.getPopularMovies();
            case ApiOperation.GET_TOP_RATED:
                return movieDB.getTopRatedMovies();
            case ApiOperation.GET_MOVIE:
                return movieDB.getMovieById(Integer.parseInt(params[1]));
        }

        return movieDB.getPopularMovies();
    }

    @Override
    protected void onPostExecute(Object movies) {
        if (delegate != null)
            delegate.processFinish(movies);
    }
}
