package com.jueggs.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.jueggs.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.jueggs.popularmovies.data.MovieContract.BASE_URL_IMAGES;
import static com.jueggs.popularmovies.data.MovieContract.IMG_WIDTH_185;

public class MoviesAdapter extends ArrayAdapter<Movie>
{
    public MoviesAdapter(Context context, int resource, List<Movie> objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.griditem_movie, parent, false);
        }

        Movie movie = getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.poster);

        Uri uri = Uri.parse(BASE_URL_IMAGES).buildUpon().appendEncodedPath(IMG_WIDTH_185).appendEncodedPath(movie.getPosterPath()).build();
        Picasso.with(getContext()).load(uri).into(imageView);

        return convertView;
    }
}
