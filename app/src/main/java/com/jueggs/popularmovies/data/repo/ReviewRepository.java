package com.jueggs.popularmovies.data.repo;

import android.content.Context;
import android.util.SparseArray;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.data.service.FetchReviewService;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;
import com.jueggs.popularmovies.ui.detail.Callback;

import java.util.Collections;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.RC_NO_NETWORK;
import static com.jueggs.popularmovies.data.MovieDbContract.RC_OK_CACHE;
import static com.jueggs.popularmovies.util.NetUtils.isNetworkAvailable;

public class ReviewRepository
{
    private static ReviewRepository instance;

    private SparseArray<List<Review>> cache = new SparseArray<>();
    private FetchReviewService service = FetchReviewService.getInstance();

    private Context context;
    private Callback.ReviewsLoaded callback;
    private int movieId;

    public void loadReviews(int movieId, Callback.StartLoadingReviews startLoadingCallback, Callback.ReviewsLoaded reviewsLoadedCallback)
    {
        startLoadingCallback.onLoadingReviewsStarted();

        if (cache.get(movieId) != null)
            reviewsLoadedCallback.onReviewsLoaded(Collections.unmodifiableList(cache.get(movieId)), RC_OK_CACHE);
        else
        {
            if (isNetworkAvailable(context))
            {
                this.callback = reviewsLoadedCallback;
                this.movieId = movieId;
                service.fetchReviews(movieId, this::onReviewsLoaded);
            }
            else
                reviewsLoadedCallback.onReviewsLoaded(null, RC_NO_NETWORK);
        }
    }

    private void onReviewsLoaded(List<Review> reviews, int resultCode)
    {
        cache.put(movieId, reviews);
        if (callback != null)
            callback.onReviewsLoaded(reviews, resultCode);
    }

    private ReviewRepository(Context context)
    {
        this.context = context;
    }

    public static ReviewRepository getInstance(Context context)
    {
        if (instance == null)
            instance = new ReviewRepository(context);
        return instance;
    }
}
