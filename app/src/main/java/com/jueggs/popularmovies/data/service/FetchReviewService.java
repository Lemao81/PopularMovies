package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.data.MovieDbService;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.ReviewRoot;
import com.jueggs.popularmovies.ui.detail.Callback;

import java.io.IOException;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class FetchReviewService
{
    public static final String TAG = FetchReviewService.class.getSimpleName();

    private static FetchReviewService instance;

    private Callback.ReviewsLoaded callback;

    private FetchReviewService()
    {
    }

    public void fetchReviews(int movieId, Callback.ReviewsLoaded callback)
    {
        this.callback = callback;
        new FetchReviewTask().execute(movieId);
    }

    public static FetchReviewService getInstance()
    {
        if (instance == null)
            instance = new FetchReviewService();
        return instance;
    }

    class FetchReviewTask extends AsyncTask<Integer, Void, List<Review>>
    {
        @Override
        protected List<Review> doInBackground(Integer... params)
        {
            MovieDbService service = MovieDbContract.createMovieDbService();

            try
            {
                ReviewRoot root = service.loadReviews(params[0]).execute().body();
                return root.getResults();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Review> reviews)
        {
            if (callback != null)
                if (reviews != null)
                    callback.onReviewsLoaded(reviews, RC_OK_NETWORK);
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onReviewsLoaded(reviews, RC_ERROR);
                }

        }
    }
}
