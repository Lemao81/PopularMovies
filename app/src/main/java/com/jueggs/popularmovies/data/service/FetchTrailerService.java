package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbService;
import com.jueggs.popularmovies.model.Trailer;
import com.jueggs.popularmovies.model.TrailerRoot;
import com.jueggs.popularmovies.ui.detail.Callback;

import java.io.IOException;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class FetchTrailerService
{
    public static final String TAG = FetchTrailerService.class.getSimpleName();

    private static FetchTrailerService instance;

    private Callback.TrailerLoaded callback;

    private FetchTrailerService()
    {
    }

    public void fetchTrailers(int movieId, Callback.TrailerLoaded callback)
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
            MovieDbService service = createMovieDbService();

            try
            {
                TrailerRoot root = service.loadTrailer(params[0]).execute().body();
                return root.getResults();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers)
        {
            if (callback != null)
                if (trailers != null)
                    callback.onTrailerLoaded(trailers, RC_OK_NETWORK);
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onTrailerLoaded(trailers, RC_ERROR);
                }

        }
    }
}
