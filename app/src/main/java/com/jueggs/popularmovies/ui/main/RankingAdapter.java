package com.jueggs.popularmovies.ui.main;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;
import static com.jueggs.popularmovies.data.MovieDbContract.createImageUri;

public class RankingAdapter extends ArrayAdapter<Movie>
{
    public RankingAdapter(Context context, int resource, List<Movie> objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.griditem_movie, parent, false);

        Movie movie = getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.poster);

        Uri uri = createImageUri(IMG_WIDTH_185, movie.getPosterPath());
        Picasso.with(getContext()).load(uri).placeholder(R.drawable.picasso_placeholder).error(R.drawable.picasso_error).into(imageView);

        return convertView;
    }
}
