package com.udacity.erics.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.erics.popularmovies.model.Movie;
import com.udacity.erics.popularmovies.util.BaseURL;

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Activity activity) {
        super(activity, 0);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_movie, parent, false);
        }

        assert movie != null;
        if (!movie.getPosterPath().isEmpty()) {
            ImageView picture = (ImageView) convertView.findViewById(R.id.list_item_movie_imageview);

            Uri uri = Uri.parse(BaseURL.IMAGE_URL_BASE);

            Picasso.with(this.getContext())
                    .load(uri.buildUpon().appendEncodedPath(movie.getPosterPath()).build().toString())
                    .resize(1080, 720)
                    .into(picture);
        }

        return convertView;
    }
}
