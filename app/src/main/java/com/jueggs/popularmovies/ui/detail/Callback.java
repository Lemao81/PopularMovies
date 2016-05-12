package com.jueggs.popularmovies.ui.detail;

import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;

import java.util.List;

public interface Callback
{
    interface TrailerLoaded
    {
        void onTrailerLoaded(List<Trailer> trailers, int resultCode);
    }

    interface ReviewsLoaded
    {
        void onReviewsLoaded(List<Review> reviews, int resultCode);
    }
}
