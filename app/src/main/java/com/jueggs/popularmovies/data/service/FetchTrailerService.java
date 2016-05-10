package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Trailer;

import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;
import static com.jueggs.popularmovies.util.NetUtils.*;

public class FetchTrailerService
{
    public static final String TAG = FetchTrailerService.class.getSimpleName();

    private static FetchTrailerService instance;

    private TrailerLoadedCallback callback;

    private FetchTrailerService()
    {
    }

    public void fetchTrailers(int movieId, TrailerLoadedCallback callback)
    {
        this.callback = callback;
        new FetchTrailerTask().execute(movieId);
    }

    public static FetchTrailerService getInstance()
    {
        if (instance == null)
            instance = new FetchTrailerService();
        return instance;
    }

    class FetchTrailerTask extends AsyncTask<Integer, Void, List<Trailer>>
    {
        @Override
        protected List<Trailer> doInBackground(Integer... params)
        {
            String jsonString = getJsonData(createTrailerUri(params[0]));

            if (TextUtils.isEmpty(jsonString))
            {
                Log.e(TAG, "no useful json string retrieved");
                return null;
            }

            return getTrailerListFromJSON(jsonString);
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers)
        {
            if (callback != null)
                if (trailers != null)
                {
                    callback.onTrailerLoaded(trailers, RC_OK_NETWORK);
                }
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onTrailerLoaded(trailers, RC_ERROR);
                }

        }
    }
}
