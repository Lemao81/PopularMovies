package com.jueggs.popularmovies.data;

import android.content.Context;
import android.util.SparseArray;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.DateTimeUtils;
import com.jueggs.popularmovies.util.NetUtils;
import com.jueggs.popularmovies.util.Utils;

import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieContract.*;

public class CachedRepository
{
    public static final String TAG = CachedRepository.class.getSimpleName();

    private static CachedRepository instance;

    private SparseArray<List<Movie>> movieLists = new SparseArray<>(NUM_SORTORDER);
    private Date day = new Date();
    private FetchService fetchService = FetchService.getInstance();
    private Callback callback;
    private Context context;

    private CachedRepository(Context context)
    {
        Utils.assertNotNull(context, String.format("%s: provided context may not be null", TAG));
        this.context = context;
    }

    public void loadMovies(int sortorder, Callback callback)
    {
        List<Movie> movies = movieLists.get(sortorder);

        if (!isExpired() && !isEmpty(movies))
        {
            callback.onMoviesLoaded(sortorder, RC_OK_CACHE, movies);
        }
        else
        {
            if (NetUtils.isNetworkAvailable(context))
            {
                this.callback = callback;
                fetchService.fetchMovies(sortorder, moviesLoadedCallback);
            }
            else
            {
                callback.onMoviesLoaded(sortorder, RC_NO_NETWORK, null);
            }
        }
    }

    private Callback moviesLoadedCallback = new Callback()
    {
        @Override
        public void onMoviesLoaded(int sortorder, int resultCode, List<Movie> movies)
        {
            movieLists.put(sortorder, movies);
            day = new Date();
            if (callback != null)
            {
                callback.onMoviesLoaded(sortorder, resultCode, movies);
            }
        }
    };

    public boolean isExpired()
    {
        return !DateTimeUtils.isSameDay(this.day, new Date());
    }

    private boolean isEmpty(List<Movie> list)
    {
        return list == null || list.size() == 0;
    }

    public static CachedRepository getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new CachedRepository(context);
        }
        return instance;
    }


}
