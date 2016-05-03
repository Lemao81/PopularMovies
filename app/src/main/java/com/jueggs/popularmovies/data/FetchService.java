package com.jueggs.popularmovies.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.jueggs.popularmovies.Movie;
import com.jueggs.popularmovies.util.IOUtils;
import com.jueggs.popularmovies.util.NetUtils;

import java.util.List;

import static com.jueggs.popularmovies.data.MovieContract.*;

public class FetchService
{
    public static final String TAG = "FetchService";

    private static FetchService instance;

    private Callback callback;
    private int sortorder;

    private FetchService()
    {
    }

    public void fetchMovies(int sortorder, Callback callback)
    {
        this.callback = callback;
        this.sortorder = sortorder;
        new FetchAsyncTask().execute(sortorder);
    }

    class FetchAsyncTask extends AsyncTask<Integer, Void, List<Movie>>
    {

        @Override
        protected List<Movie> doInBackground(Integer... params)
        {
            int sortorder = params[0];

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(PATHS.get(sortorder))
                    .appendQueryParameter(QUERY_KEY_APIKEY,API_KEY).build();

            String jsonString = IOUtils.readStream(NetUtils.getStream(uri));

            if (TextUtils.isEmpty(jsonString))
            {
                return null;
            }




            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            if (callback != null)
            {
                if (movies != null)
                {
                    callback.onMoviesLoaded(sortorder, RC_OK, movies);
                }
                else
                {
                    callback.onMoviesLoaded(sortorder, RC_ERROR, null);
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
