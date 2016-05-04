package com.jueggs.popularmovies.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.IOUtils;
import com.jueggs.popularmovies.util.NetUtils;

import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class FetchService
{
    public static final String TAG = FetchService.class.getSimpleName();

    private static FetchService instance;

    private Callback callback;
    private int sortOrder;

    private FetchService()
    {
    }

    public void fetchMovies(int sortOrder, Callback callback)
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
            int sortOrder = params[0];

            Uri uri = Uri.parse(BASE_URL_MOVIES).buildUpon()
                    .appendEncodedPath(PATHS.get(sortOrder))
                    .appendQueryParameter(QUERY_KEY_APIKEY, API_KEY).build();

            String jsonString = NetUtils.getData(uri);

            if (TextUtils.isEmpty(jsonString))
            {
                Log.e(TAG, "no useful json string retrieved");
                return null;
            }

            return IOUtils.getMovieListFromJSON(jsonString);
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            if (callback != null)
            {
                if (movies != null)
                {
                    callback.onMoviesLoaded(sortOrder, RC_OK_NETWORK, movies);
                }
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onMoviesLoaded(sortOrder, RC_ERROR, null);
                }
            }
        }

    }

    public static FetchService getInstance()
    {
        if (instance == null)
        {
            instance = new FetchService();
        }
        return instance;
    }
}
