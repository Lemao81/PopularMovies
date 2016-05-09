package com.jueggs.popularmovies.data.repo;

import android.content.Context;
import android.util.SparseArray;
import com.jueggs.popularmovies.data.service.FetchRankingService;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.DateTimeUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.DateTimeUtils.*;
import static com.jueggs.popularmovies.util.NetUtils.*;
import static com.jueggs.popularmovies.util.Utils.*;

public class RankingRepository
{
    public static final String TAG = RankingRepository.class.getSimpleName();

    private static RankingRepository instance;

    private SparseArray<List<Movie>> cache = new SparseArray<>(NUM_SORTORDER);
    private FetchRankingService service = FetchRankingService.getInstance();

    private Date lastUpdate = new Date();
    private MovieLoadedCallback callback;
    private Context context;

    private RankingRepository(Context context)
    {
        assertNotNull(context, String.format("%s: provided context may not be null", TAG));
        this.context = context;
    }

    public void loadMovies(int sortOrder, MovieLoadedCallback callback)
    {
        if (!isExpired() && !isEmpty(cache.get(sortOrder)))
        {
            callback.onMoviesLoaded(sortOrder, RC_OK_CACHE, Collections.unmodifiableList(cache.get(sortOrder)));
        }
        else
        {
            if (isNetworkAvailable(context))
            {
                this.callback = callback;
                service.fetchMovies(sortOrder, moviesLoadedCallback);
            }
            else
            {
                callback.onMoviesLoaded(sortOrder, RC_NO_NETWORK, null);
            }
        }
    }

    private MovieLoadedCallback moviesLoadedCallback = new MovieLoadedCallback()
    {
        @Override
        public void onMoviesLoaded(int sortOrder, int resultCode, List<Movie> movies)
        {
            cache.put(sortOrder, movies);
            lastUpdate = new Date();
            if (callback != null)
                callback.onMoviesLoaded(sortOrder, resultCode, movies);
        }
    };

    public void clear(int sortOrder)
    {
        List<Movie> list = cache.get(sortOrder);

        if (list != null)
            list.clear();
    }

    public boolean isExpired()
    {
        return !isSameDay(this.lastUpdate, new Date());
    }

    public static RankingRepository getInstance(Context context)
    {
        if (instance == null)
            instance = new RankingRepository(context);
        return instance;
    }


}
