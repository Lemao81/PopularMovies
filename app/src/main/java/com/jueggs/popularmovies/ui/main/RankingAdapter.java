package com.jueggs.popularmovies.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;
import com.squareup.picasso.*;
import com.squareup.picasso.Callback;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;
import static com.jueggs.popularmovies.data.MovieDbContract.createImageUri;
import static com.jueggs.popularmovies.util.Utils.*;

public class RankingAdapter extends ArrayAdapter<Movie>
{
    public RankingAdapter(Context context, List<Movie> objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.griditem_movie, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Movie movie = getItem(position);

        loadImage(getContext(), IMG_WIDTH_185, movie.getPosterPath(), holder.image);

        return convertView;
    }

    class ViewHolder
    {
        ImageView image;

        public ViewHolder(View view)
        {
            image = (ImageView) view.findViewById(R.id.poster);
        }
    }
}
