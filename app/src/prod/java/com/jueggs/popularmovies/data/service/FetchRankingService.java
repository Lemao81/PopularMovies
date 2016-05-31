package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.data.MovieDbService;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Ranking;
import com.jueggs.popularmovies.ui.main.Callback;

import java.io.IOException;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

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
            int sortOrder = params[0];

            try
            {
                MovieDbService service = createMovieDbService();

                Ranking ranking;
                switch (sortOrder)
                {
                    case SORTORDER_POPULAR:
                        ranking = service.loadMostPopularMovies().execute().body();
                        break;
                    case SORTORDER_TOPRATED:
                        ranking = service.loadTopRatedMovies().execute().body();
                        break;
                    default:
                        Log.e(TAG, "unknown sortorder, return null");
                        return null;
                }
                return ranking.getResults();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            if (callback != null)
                if (movies != null)
                    callback.onMoviesLoaded(movies, sortOrder, RC_OK_NETWORK);
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
            instance = new FetchRankingService();
        return instance;
    }
}
