package com.jueggs.popularmovies.ui.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.repo.TrailerRepository;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;
import static com.jueggs.popularmovies.data.MovieDbContract.createImageUri;

public class DetailFragment extends Fragment
{
    public static final String ARG_MOVIE = "ARG_MOVIE";
    public static final String RELEASE_DATE_PATTERN = "MM/yyyy";
    public static final String TAG = DetailFragment.class.getSimpleName();

    @Bind(R.id.thumbnail) ImageView thumbnail;
    @Bind(R.id.title) TextView title;
    @Bind(R.id.release_date) TextView releaseDate;
    @Bind(R.id.vote_average) TextView voteAverage;
    @Bind(R.id.overview) TextView overview;
    @Bind(R.id.trailerList) ListView trailerList;

    private TrailerAdapter trailerAdapter;

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
        ButterKnife.bind(this, view);

        Movie movie = getArguments().getParcelable(ARG_MOVIE);
        if (movie == null)
            getActivity().finish();

        Uri uri = createImageUri(IMG_WIDTH_185, movie.getPosterPath());
        Picasso.with(getContext()).load(uri).placeholder(R.drawable.picasso_placeholder).error(R.drawable.picasso_error).into(thumbnail);

        bindView(movie);

        trailerAdapter = new TrailerAdapter(getContext(), 0, new ArrayList<Trailer>());
        trailerList.setAdapter(trailerAdapter);
//        trailerList.addHeaderView(inflater.inflate(R.layout.trailerlist_header, container, false));

        TrailerRepository.getInstance(getContext()).loadTrailers(movie.getId(), trailerLoadedCallback);

        return view;
    }

    private void bindView(Movie movie)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(RELEASE_DATE_PATTERN);
        title.setText(movie.getTitle());
        releaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        voteAverage.setText(String.format(getString(R.string.format_vote_average), movie.getVoteAverage()));
        overview.setText(movie.getOverview());
    }

    private TrailerLoadedCallback trailerLoadedCallback=new TrailerLoadedCallback()
    {
        @Override
        public void onTrailerLoaded(List<Trailer> trailers, int resultCode)
        {
            switch (resultCode)
            {
                case RC_OK_CACHE:
                    updateTrailers(trailers);
                    break;
                case RC_OK_NETWORK:
                    updateTrailers(trailers);
                    Toast.makeText(getContext(), "Updated trailer list", Toast.LENGTH_LONG).show();
                    break;
                case RC_NO_NETWORK:
                    handleFailedTrailerUpdate("No network available :(");
                    break;
                case RC_ERROR:
                    handleFailedTrailerUpdate("An inexplicable error occurred during network access");
                    break;
                default:
                    Log.e(TAG, "unknown result code");
            }
        }
    };

    private void updateTrailers(List<Trailer> trailers)
    {
        trailerAdapter.clear();
        trailerAdapter.addAll(trailers);
    }

    private void handleFailedTrailerUpdate(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}
