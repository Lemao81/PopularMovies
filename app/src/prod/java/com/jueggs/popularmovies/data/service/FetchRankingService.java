package com.jueggs.popularmovies.data.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.main.Callback;

import java.util.List;

import static android.text.TextUtils.*;
import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;
import static com.jueggs.popularmovies.util.NetUtils.*;

public class FetchRankingService implements RankingService
{
    public static final String TAG = FetchRankingService.class.getSimpleName();

    private static FetchRankingService instance;

    private Callback.MoviesLoaded callback;
    private int sortOrder;

    private FetchRankingService()
    {
    }

    public void fetchMovies(int sortOrder, Callback.MoviesLoaded callback)
    {
        this.callback = callback;
        this.sortOrder = sortOrder;
        new FetchAsyncTask().execute(sortOrder);
    }

    class FetchAsyncTask extends AsyncTask<Integer, Void, List<Movie>>
    {

        @Override
        protected List<Movie> doInBackground(Integer... params)
        {
            String jsonString = getJsonData(createRankingUri(params[0]));

            if (isEmpty(jsonString))
            {
                Log.e(TAG, "no useful json string retrieved");
                return null;
            }

            return getMovieListFromJSON(jsonString);
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            if (callback != null)
                if (movies != null)
                {
                    callback.onMoviesLoaded(movies, sortOrder, RC_OK_NETWORK);
                }
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onMoviesLoaded(null, sortOrder, RC_ERROR);
                }
        }

    }

    public static FetchRankingService getInstance()
    {
        if (instance == null)
        {
            instance = new FetchRankingService();
        }
        return instance;
    }
}
