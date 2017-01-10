package com.udacity.erics.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.erics.popularmovies.model.Movie;
import com.udacity.erics.popularmovies.service.MovieService;
import com.udacity.erics.popularmovies.util.ApiOperation;
import com.udacity.erics.popularmovies.util.AsyncTaskDelegate;
import com.udacity.erics.popularmovies.util.BaseURL;
import com.udacity.erics.popularmovies.util.IntentParameterName;

import java.util.Calendar;

public class MovieDetailActivity extends AppCompatActivity implements AsyncTaskDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int movieId = getIntent().getIntExtra(IntentParameterName.MOVIE_ID, 0);

        MovieService movieService = new MovieService(getBaseContext(), this);
        movieService.execute(ApiOperation.GET_MOVIE, String.valueOf(movieId));
    }

    @Override
    public void processFinish(Object output) {
        Movie movie = (Movie) output;
        this.updateView(movie);
    }

    private void updateView(Movie movie) {

        if (movie != null) {
            TextView textViewMovieTitle = (TextView) findViewById(R.id.movie_title);
            textViewMovieTitle.setText(movie.getOriginalTitle());

            TextView textViewMovieDuration = (TextView) findViewById(R.id.movie_duration);
            String textMovieDuration = String.valueOf(movie.getRuntime()) + getResources().getString(R.string.movie_minutes_sufix);
            textViewMovieDuration.setText(textMovieDuration);

            TextView textViewMovieYear = (TextView) findViewById(R.id.movie_year);
            Calendar cal = Calendar.getInstance();
            cal.setTime(movie.getReleaseDate());
            int year = cal.get(Calendar.YEAR);
            textViewMovieYear.setText(String.valueOf(year));

            TextView textViewMovieVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
            String textMovieVoteAverage = String.valueOf(movie.getVoteAverage()) + getResources().getString(R.string.movie_vote_average_count_sufix);
            textViewMovieVoteAverage.setText(textMovieVoteAverage);

            TextView textViewMovieOverview = (TextView) findViewById(R.id.movie_overview);
            textViewMovieOverview.setText(movie.getOverview());

            ImageView picture = (ImageView) findViewById(R.id.imageDetailView);

            Uri uri = Uri.parse(BaseURL.IMAGE_URL_BASE);

            Picasso.with(getBaseContext())
                    .load(uri.buildUpon().appendEncodedPath(movie.getPosterPath()).build().toString())
                    .resize(342, 513)
                    .into(picture);
        }
    }
}
