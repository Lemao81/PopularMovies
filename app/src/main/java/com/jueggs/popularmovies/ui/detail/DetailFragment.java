package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.favourites.schematic.FavouriteColumns;
import com.jueggs.popularmovies.data.repo.ReviewRepository;
import com.jueggs.popularmovies.data.repo.TrailerRepository;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;
import static com.jueggs.popularmovies.data.MovieDbContract.createImageUri;
import static com.jueggs.popularmovies.data.favourites.schematic.FavouritesProvider.*;

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
    @Bind(R.id.trailerReviewContainer) LinearLayout trailerReviewContainer;
    @Bind(R.id.favourite) ImageButton favourite;

    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private boolean trailerLoaded;
    private boolean reviewLoaded;
    private boolean isFavourite;
    private Uri favouriteIdUri;
    private ContentResolver contentResolver;

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

        movie = getArguments().getParcelable(ARG_MOVIE);
        if (movie == null)
            getActivity().finish();

        Uri uri = createImageUri(IMG_WIDTH_185, movie.getPosterPath());
        Picasso.with(getContext()).load(uri).placeholder(R.drawable.picasso_placeholder).error(R.drawable.picasso_error).into(thumbnail);

        favouriteIdUri = Favourite.withMovieId(movie.getMovieId());
        contentResolver = getContext().getContentResolver();

        bindView(movie);

        TrailerRepository.getInstance(getContext()).loadTrailers(movie.getMovieId(), trailerLoadedCallback);
        ReviewRepository.getInstance(getContext()).loadReviews(movie.getMovieId(), reviewLoadedCallback);

        return view;
    }

    private void bindView(Movie movie)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(RELEASE_DATE_PATTERN);
        title.setText(movie.getTitle());
        releaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        voteAverage.setText(String.format(getString(R.string.format_vote_average), movie.getVoteAverage()));
        overview.setText(movie.getOverview());

        isFavourite = contentResolver.query(favouriteIdUri, new String[]{FavouriteColumns._ID}, null, null, null).moveToFirst();
        changeStarDrawable(isFavourite);
        favourite.setOnClickListener(addFavouriteClickListener);
    }

    private View.OnClickListener addFavouriteClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (isFavourite)
            {
                contentResolver.delete(favouriteIdUri, null, null);
                changeStarDrawable(false);
                Toast.makeText(getContext(), R.string.favourites_removed_msg, Toast.LENGTH_LONG).show();
            }
            else
            {
                contentResolver.insert(Favourite.BASE_URI, movie.toContentValues());
                changeStarDrawable(true);
                isFavourite = true;
                Toast.makeText(getContext(), R.string.favourites_added_msg, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void changeStarDrawable(boolean checked)
    {
        favourite.setImageResource(checked ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
    }

    private TrailerLoadedCallback trailerLoadedCallback = new TrailerLoadedCallback()
    {
        @Override
        public void onTrailerLoaded(List<Trailer> trailers, int resultCode)
        {
            DetailFragment.this.trailers = trailers;
            trailerLoaded = true;

            switch (resultCode)
            {
                case RC_OK_NETWORK:
                    if (reviewLoaded)
                        Toast.makeText(getContext(), "Updated trailer and reviews", Toast.LENGTH_SHORT).show();
                case RC_OK_CACHE:
                    if (reviewLoaded)
                        updateTrailerAndReview();
                    break;
                case RC_NO_NETWORK:
                    handleFailedUpdate("No network available :(");
                    break;
                case RC_ERROR:
                    handleFailedUpdate("An inexplicable error occurred during network access to fetch trailer");
                    break;
                default:
                    Log.e(TAG, "unknown result code");
            }
        }
    };

    private ReviewLoadedCallback reviewLoadedCallback = new ReviewLoadedCallback()
    {
        @Override
        public void onReviewLoaded(List<Review> reviews, int resultCode)
        {
            DetailFragment.this.reviews = reviews;
            reviewLoaded = true;

            switch (resultCode)
            {
                case RC_OK_NETWORK:
                    if (trailerLoaded)
                        Toast.makeText(getContext(), "Updated trailer and reviews", Toast.LENGTH_SHORT).show();
                case RC_OK_CACHE:
                    if (trailerLoaded)
                        updateTrailerAndReview();
                    break;
                case RC_NO_NETWORK:
                    break;
                case RC_ERROR:
                    handleFailedUpdate("An inexplicable error occurred during network access to fetch reviews");
                    break;
                default:
                    Log.e(TAG, "unknown result code");
            }
        }
    };

    private void updateTrailerAndReview()
    {
        DetailAdapter adapter = new DetailAdapter(trailerReviewContainer, getContext(), trailers, reviews);
        adapter.createViews();
    }

    private void handleFailedUpdate(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}
