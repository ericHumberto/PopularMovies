package com.udacity.erics.popularmovies.integration;

import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.udacity.erics.popularmovies.model.Movie;
import com.udacity.erics.popularmovies.util.ApiKey;
import com.udacity.erics.popularmovies.util.BaseURL;
import com.udacity.erics.popularmovies.util.MovieFieldName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDB {

    private final Uri apiBaseURI;
    private final String LOG_TAG = this.getClass().getSimpleName();

    public MovieDB() {
        this.apiBaseURI = Uri.parse(BaseURL.API_URL_MOVIE_BASE);
    }

    public List<Movie> getPopularMovies() {
        ArrayList<Movie> moviesResult = new ArrayList<>();

        try {
            OkHttpClient urlConnection = new OkHttpClient();
            URL url;
            Uri builtUri = this.apiBaseURI.buildUpon().appendEncodedPath(BaseURL.API_MOVIE_POPULAR_URL_PART)
                    .appendQueryParameter("api_key", ApiKey.getKey())
                    .build();

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            Request request = new Request.Builder().url(builtUri.toString()).build();
            Response response = urlConnection.newCall(request).execute();

            String apiResult = response.body().string();
            Log.v(LOG_TAG, "Return from api / popular " + apiResult);

            JSONObject moviesJson = new JSONObject(apiResult);
            JSONArray movieArray = moviesJson.getJSONArray("results");

            for (int i = 0; i < movieArray.length(); i++) {
                Movie movieResult = this.mapJsonToMovie(movieArray.getJSONObject(i));

                if (movieResult != null)
                    moviesResult.add(movieResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (moviesResult.isEmpty())
            return null;

        return moviesResult;
    }

    public List<Movie> getTopRatedMovies() {
        ArrayList<Movie> moviesResult = new ArrayList<>();

        try {
            OkHttpClient urlConnection = new OkHttpClient();
            URL url;
            Uri builtUri = this.apiBaseURI.buildUpon().appendEncodedPath(BaseURL.API_MOVIE_TOP_RATED_URL_PART)
                    .appendQueryParameter("api_key", ApiKey.getKey())
                    .build();

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            Request request = new Request.Builder().url(builtUri.toString()).build();
            Response response = urlConnection.newCall(request).execute();

            String apiResult = response.body().string();
            Log.v(LOG_TAG, "Return from api / top rated" + apiResult);

            JSONObject moviesJson = new JSONObject(apiResult);
            JSONArray movieArray = moviesJson.getJSONArray("results");

            for (int i = 0; i < movieArray.length(); i++) {
                Movie movieResult = this.mapJsonToMovie(movieArray.getJSONObject(i));

                if (movieResult != null)
                    moviesResult.add(movieResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (moviesResult.isEmpty())
            return null;

        return moviesResult;
    }

    public Movie getMovieById(int id) {
        try {
            OkHttpClient urlConnection = new OkHttpClient();
            URL url;

            Uri builtUri = this.apiBaseURI.buildUpon()
                    .appendEncodedPath(String.valueOf(id))
                    .appendQueryParameter("api_key", ApiKey.getKey())
                    .build();

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            Request request = new Request.Builder().url(builtUri.toString()).build();
            Response response = urlConnection.newCall(request).execute();

            String apiResult = response.body().string();
            Log.v(LOG_TAG, "Return from api / top rated" + apiResult);

            JSONObject moviesJson = new JSONObject(apiResult);
            return this.mapJsonToMovie(moviesJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Movie mapJsonToMovie(JSONObject movieJsonObj) {
        Movie movieResult = new Movie();

        try {
            movieResult.setAdult(movieJsonObj.getBoolean(MovieFieldName.ADULT));
            movieResult.setBackdropPath(movieJsonObj.getString(MovieFieldName.BACKDROP_PATH));
            movieResult.setPosterPath(movieJsonObj.getString(MovieFieldName.POSTER_PATH));
            movieResult.setOverview(movieJsonObj.getString(MovieFieldName.OVERVIEW));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            movieResult.setReleaseDate(dateFormat.parse(movieJsonObj.getString(MovieFieldName.RELEASE_DATE)));

            movieResult.setId(movieJsonObj.getInt(MovieFieldName.ID));
            movieResult.setOriginalTitle(movieJsonObj.getString(MovieFieldName.ORIGINAL_TITLE));
            movieResult.setOriginalLanguage(movieJsonObj.getString(MovieFieldName.ORIGINAL_LANGUAGE));
            movieResult.setTitle(movieJsonObj.getString(MovieFieldName.TITLE));
            movieResult.setPopularity(movieJsonObj.getDouble(MovieFieldName.POPULARITY));
            movieResult.setVoteCount(movieJsonObj.getInt(MovieFieldName.VOTE_COUNT));
            movieResult.setVideo(movieJsonObj.getBoolean(MovieFieldName.VIDEO));
            movieResult.setVoteAverage(movieJsonObj.getDouble(MovieFieldName.VOTE_AVERAGE));

            if (movieJsonObj.has(MovieFieldName.RUNTIME)) {
                movieResult.setRuntime(movieJsonObj.getInt(MovieFieldName.RUNTIME));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error parsing json to movie " + e.getMessage());
            return null;
        }

        return movieResult;
    }
}
