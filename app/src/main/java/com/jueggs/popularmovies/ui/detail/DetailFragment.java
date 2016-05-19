package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.text.TextUtils.*;
import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_185;
import static com.jueggs.popularmovies.data.favourites.FavouritesProvider.*;
import static com.jueggs.popularmovies.util.UIUtils.*;
import static com.jueggs.popularmovies.util.Utils.*;

public class DetailFragment extends Fragment
{
    public static final String ARG_MOVIE = "ARG_MOVIE";
    public static final String RELEASE_DATE_PATTERN = "MM/yyyy";
    public static final String TAG = DetailFragment.class.getSimpleName();
    public static final String STATE_MOVIE = "movie";

    @Bind(R.id.thumbnail) ImageView thumbnail;
    @Bind(R.id.title) TextView title;
    @Bind(R.id.release_date) TextView releaseDate;
    @Bind(R.id.vote_average) TextView voteAverage;
    @Bind(R.id.overview) TextView overview;
    @Bind(R.id.trailerReviewContainer) LinearLayout trailerReviewContainer;
    @Bind(R.id.favourite) ImageButton favourite;
    @Bind(R.id.genre) TextView genre;
    @Bind(R.id.coverLoading) FrameLayout coverLoading;

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

        if (savedInstanceState != null)
            movie = savedInstanceState.getParcelable(STATE_MOVIE);
        else
            movie = getArguments().getParcelable(ARG_MOVIE);
        if (movie == null)
            getActivity().finish();

        movieIdUri = Favourite.withMovieId(movie.getMovieId());
        contentResolver = getContext().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        bindView(movie);

        return view;
    }

    private void bindView(Movie movie)
    {
        title.setText(movie.getTitle());
        releaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        voteAverage.setText(String.format(getString(R.string.format_vote_average), movie.getVoteAverage()));
        overview.setText(movie.getOverview());
        genre.setText(createGenreString(movie.getGenreIds()));

        if (isEmpty(movie.getPoster()))
            loadImage(getContext(), IMG_WIDTH_185, movie.getPosterPath(), thumbnail, picassoCallback);
        else
            thumbnail.setImageDrawable(convertByteArrayToDrawable(getResources(), movie.getPoster()));

        isFavourite = contentResolver.query(movieIdUri, new String[]{FavouriteColumns._ID}, null, null, null).moveToFirst();
        setStarDrawable(isFavourite);
        favourite.setOnClickListener(this::onAddFavouriteClicked);
    }

    private void onAddFavouriteClicked(View v)
    {
        if (isFavourite)
            new DeleteFavouriteTask(this::onCRUDoperationStarted, this::onCRUDoperationCompleted, contentResolver).execute(movieIdUri);
        else
            new InsertFavouriteTask(this::onCRUDoperationStarted, this::onCRUDoperationCompleted, contentResolver).execute(movie);
    }

    private PicassoCallbackAdapter picassoCallback = new PicassoCallbackAdapter()
    {
        @Override
        public void onSuccess()
        {
            movie.setPoster(convertDrawableToByteArray(thumbnail.getDrawable()));
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        TrailerRepository.getInstance(getContext()).loadTrailers(movie.getMovieId(), this::onStartLoadingTrailer, this::onTrailerLoaded);
        ReviewRepository.getInstance(getContext()).loadReviews(movie.getMovieId(), this::onStartLoadingReviews, this::onReviewsLoaded);
    }

    private void onStartLoadingTrailer()
    {
        showLoading(true, coverLoading);
    }

    private void onTrailerLoaded(List<Trailer> trailers, int resultCode)
    {
        DetailFragment.this.trailers = trailers;
        trailerLoaded = true;
        if (reviewLoaded)
            showLoading(false, coverLoading);

        switch (resultCode)
        {
            case RC_OK_NETWORK:
            case RC_OK_CACHE:
                if (reviewLoaded)
                    updateTrailerAndReview();
                if (actionProvider != null && hasElements(trailers))
                    actionProvider.setShareIntent(createShareIntent());
                break;
            case RC_NO_NETWORK:
                showToast(R.string.msg_nonetwork);
                break;
            case RC_ERROR:
                showToast(R.string.msg_error);
                break;
            default:
                Log.e(TAG, "unknown result code");
        }
    }

    private void onStartLoadingReviews()
    {
        showLoading(true, coverLoading);
    }

    private void onReviewsLoaded(List<Review> reviews, int resultCode)
    {
        DetailFragment.this.reviews = reviews;
        reviewLoaded = true;
        if (trailerLoaded)
            showLoading(false, coverLoading);

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
                showToast(R.string.msg_error);
                break;
            default:
                Log.e(TAG, "unknown result code");
        }
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

    private void onCRUDoperationStarted()
    {
        showLoading(true, coverLoading);
    }

    private void onCRUDoperationCompleted(int result, Callback.CRUD operation)
    {
        showLoading(false, coverLoading);

        int stringId = R.string.empty;
        switch (operation)
        {
            case INSERT:
                if (result != -1)
                {
                    stringId = R.string.favourites_added_msg;
                    setStarDrawable(true);
                    isFavourite = true;
                }
                else
                    stringId = R.string.favourites_added_error;
                break;
            case DELETE:
                if (result > 0)
                {
                    stringId = R.string.favourites_removed_msg;
                    removeFavourite();
                }
                else
                    stringId = R.string.favourites_removed_error;
                break;
        }
        showToast(stringId);
    }

    private void removeFavourite()
    {
        setStarDrawable(false);
        isFavourite = false;
    }

    private void setStarDrawable(boolean checked)
    {
        favourite.setImageResource(checked ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
    }

    private void updateTrailerAndReview()
    {
        DetailAdapter adapter = new DetailAdapter(trailerReviewContainer, getContext(), trailers, reviews);
        adapter.createViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.detail_menu, menu);

        actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_share));
        if (hasElements(trailers))
            actionProvider.setShareIntent(createShareIntent());
    }

    private Intent createShareIntent()
    {
        String key = trailers.get(0).getKey();
        String send = isEmpty(key) ? "" : createYoutubeUri(key).toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, send);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType(getString(R.string.share_trailerurl_type));
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home && getActivity() instanceof DetailActivity)
        {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onFavouriteDeleted(FavouriteDeletedEvent event)
    {
        int movieId = event.movieId;
        if (movieId == movie.getMovieId())
            removeFavourite();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(STATE_MOVIE, movie);
    }

    private void showToast(int stringId)
    {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT).show();
    }
}
