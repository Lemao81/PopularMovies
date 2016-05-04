package com.jueggs.popularmovies.ui.detailview;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import static com.jueggs.popularmovies.data.MovieDbContract.BASE_URL_IMAGES;
import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;

public class DetailFragment extends Fragment
{
    public static final String ARG_MOVIE = "ARG_MOVIE";
    public static final String RELEASE_DATE_PATTERN = "MM/yyyy";

    public static DetailFragment createInstance(Movie movie)
    {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Movie movie = getArguments().getParcelable(ARG_MOVIE);
        if (movie == null)
        {
            getActivity().finish();
        }

        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        Uri uri = Uri.parse(BASE_URL_IMAGES).buildUpon().appendEncodedPath(IMG_WIDTH_185).appendEncodedPath(movie.getPosterPath()).build();

        Picasso.with(getContext()).load(uri).into(thumbnail);

        SimpleDateFormat dateFormat = new SimpleDateFormat(RELEASE_DATE_PATTERN);
        ((TextView) view.findViewById(R.id.title)).setText(movie.getTitle());
        ((TextView) view.findViewById(R.id.release_date)).setText(dateFormat.format(movie.getReleaseDate()));
        ((TextView) view.findViewById(R.id.vote_average)).setText(String.format("%.1f/10", movie.getVoteAverage()));
        ((TextView) view.findViewById(R.id.overview)).setText(movie.getOverview());

        return view;
    }
}
