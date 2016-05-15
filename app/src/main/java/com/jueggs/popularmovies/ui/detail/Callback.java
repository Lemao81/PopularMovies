package com.jueggs.popularmovies.ui.detail;

import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;

import java.util.List;

public interface Callback
{
    interface StartLoadingTrailer
    {
        void onLoadingTrailerStarted();
    }

    interface TrailerLoaded
    {
        void onTrailerLoaded(List<Trailer> trailers, int resultCode);
    }

    interface StartLoadingReviews
    {
        void onLoadingReviewsStarted();
    }

    interface ReviewsLoaded
    {
        void onReviewsLoaded(List<Review> reviews, int resultCode);
    }

    interface FavouriteCRUDstarted
    {
        void onFavouriteCRUDstarted();
    }

    interface FavouriteCRUDcompleted
    {
        void onFavouriteCRUDcompleted();

    }
}
