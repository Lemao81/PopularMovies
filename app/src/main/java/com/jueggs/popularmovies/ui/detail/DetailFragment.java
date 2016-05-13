package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.adapter.PicassoCallbackAdapter;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.data.repo.ReviewRepository;
import com.jueggs.popularmovies.data.repo.TrailerRepository;
import com.jueggs.popularmovies.event.FavouriteDeletedEvent;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;
import static com.jueggs.popularmovies.data.favourites.FavouritesProvider.*;
import static com.jueggs.popularmovies.util.Utils.*;

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
    @Bind(R.id.genre) TextView genre;

    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private boolean trailerLoaded;
    private boolean reviewLoaded;
    private boolean isFavourite;
    private Uri movieIdUri;
    private ContentResolver contentResolver;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(RELEASE_DATE_PATTERN);
    private ShareActionProvider actionProvider;

    public static DetailFragment createInstance(Movie movie)
    {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        movieIdUri = Favourite.withMovieId(movie.getMovieId());
        contentResolver = getContext().getContentResolver();

        bindView(movie);

        TrailerRepository.getInstance(getContext()).loadTrailers(movie.getMovieId(), trailerLoadedCallback);
        ReviewRepository.getInstance(getContext()).loadReviews(movie.getMovieId(), reviewLoadedCallback);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (App.getInstance().isTwoPane())
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (App.getInstance().isTwoPane())
            EventBus.getDefault().unregister(this);
    }

    private void bindView(Movie movie)
    {
        title.setText(movie.getTitle());
        releaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        voteAverage.setText(String.format(getString(R.string.format_vote_average), movie.getVoteAverage()));
        overview.setText(movie.getOverview());
        genre.setText(createGenreString(movie.getGenreIds()));

        if (movie.getPoster() == null)
        {
            Uri uri = createImageUri(IMG_WIDTH_185, movie.getPosterPath());
            Picasso.with(getContext()).load(uri).placeholder(R.drawable.picasso_placeholder)
                    .error(R.drawable.picasso_error).into(thumbnail, picassoCallback);
        }
        else
        {
            thumbnail.setImageDrawable(convertByteArrayToDrawable(getResources(), movie.getPoster()));
        }

        isFavourite = contentResolver.query(movieIdUri, new String[]{FavouriteColumns._ID}, null, null, null).moveToFirst();
        setStarDrawable(isFavourite);
        favourite.setOnClickListener(addFavouriteClickListener);
    }

    private PicassoCallbackAdapter picassoCallback = new PicassoCallbackAdapter()
    {
        @Override
        public void onSuccess()
        {
            movie.setPoster(convertDrawableToByteArray(thumbnail.getDrawable()));
        }
    };

    private View.OnClickListener addFavouriteClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (isFavourite)
            {
                contentResolver.delete(movieIdUri, null, null);
                removeFavourite();
                Toast.makeText(getContext(), R.string.favourites_removed_msg, Toast.LENGTH_LONG).show();
            }
            else
            {
                contentResolver.insert(Favourite.BASE_URI, transformMovieToContentValues(movie));
                setStarDrawable(true);
                isFavourite = true;
                Toast.makeText(getContext(), R.string.favourites_added_msg, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void removeFavourite()
    {
        setStarDrawable(false);
        isFavourite = false;
    }

    private void setStarDrawable(boolean checked)
    {
        favourite.setImageResource(checked ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
    }

    private Callback.TrailerLoaded trailerLoadedCallback = new Callback.TrailerLoaded()
    {
        @Override
        public void onTrailerLoaded(List<Trailer> trailers, int resultCode)
        {
            DetailFragment.this.trailers = trailers;
            trailerLoaded = true;

            switch (resultCode)
            {
                case RC_OK_NETWORK:
                case RC_OK_CACHE:
                    if (reviewLoaded)
                        updateTrailerAndReview();
                    if (actionProvider != null && !isEmpty(trailers))
                        actionProvider.setShareIntent(createShareIntent());
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

    private Callback.ReviewsLoaded reviewLoadedCallback = new Callback.ReviewsLoaded()
    {
        @Override
        public void onReviewsLoaded(List<Review> reviews, int resultCode)
        {
            DetailFragment.this.reviews = reviews;
            reviewLoaded = true;

            switch (resultCode)
            {
                case RC_OK_NETWORK:
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.detail_menu, menu);

        actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_share));
        if (!isEmpty(trailers))
            actionProvider.setShareIntent(createShareIntent());
    }

    private Intent createShareIntent()
    {
        String key = trailers.get(0).getKey();
        String send = TextUtils.isEmpty(key) ? "" : createYoutubeUri(key).toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, send);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType(getString(R.string.share_trailerurl_type));
        return shareIntent;
    }

    @Subscribe
    public void onFavouriteDeleted(FavouriteDeletedEvent event)
    {
        int movieId = event.movieId;
        if (movieId == movie.getMovieId())
            removeFavourite();
    }

}
