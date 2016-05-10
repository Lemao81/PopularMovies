package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;

import java.util.List;

import static android.text.TextUtils.*;
import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;
import static com.jueggs.popularmovies.util.NetUtils.getJsonData;

public class FetchReviewService
{
    public static final String TAG = FetchReviewService.class.getSimpleName();

    private static FetchReviewService instance;

    private MovieDbContract.ReviewLoadedCallback callback;

    private FetchReviewService()
    {
    }

    public void fetchReviews(int movieId, MovieDbContract.ReviewLoadedCallback callback)
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
            String jsonString = getJsonData(createReviewUri(params[0]));

            if (isEmpty(jsonString))
            {
                Log.e(TAG, "no useful json string retrieved");
                return null;
            }

            return getReviewListFromJSON(jsonString);
        }

        @Override
        protected void onPostExecute(List<Review> reviews)
        {
            if (callback != null)
                if (reviews != null)
                {
                    callback.onReviewLoaded(reviews, RC_OK_NETWORK);
                }
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onReviewLoaded(reviews, RC_ERROR);
                }

        }
    }
}
