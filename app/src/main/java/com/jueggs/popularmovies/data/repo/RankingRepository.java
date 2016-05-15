package com.jueggs.popularmovies.data.repo;

import android.content.Context;
import android.util.SparseArray;
import com.jueggs.popularmovies.data.service.Injection;
import com.jueggs.popularmovies.data.service.RankingService;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.main.Callback;
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
    private RankingService service = Injection.injectRankingService();

    private Date lastUpdate = new Date();
    private Callback.MoviesLoaded callback;
    private Context context;

    private RankingRepository(Context context)
    {
        assertNotNull(context, String.format("%s: provided context may not be null", TAG));
        this.context = context;
    }

    public void loadMovies(int sortOrder, Callback.MoviesLoaded moviesLoadedCallback,Callback.StartLoadingMovies startLoadingCallback)
    {
        startLoadingCallback.onLoadingMoviesStarted();

        if (!isExpired() && hasElements(cache.get(sortOrder)))
            moviesLoadedCallback.onMoviesLoaded(Collections.unmodifiableList(cache.get(sortOrder)), sortOrder, RC_OK_CACHE);
        else
        {
            if (isNetworkAvailable(context))
            {
                this.callback = moviesLoadedCallback;
                service.fetchMovies(sortOrder, this.moviesLoadedCallback);
            }
            else
                moviesLoadedCallback.onMoviesLoaded(null, sortOrder, RC_NO_NETWORK);
        }
    }

    private Callback.MoviesLoaded moviesLoadedCallback = new Callback.MoviesLoaded()
    {
        @Override
        public void onMoviesLoaded(List<Movie> movies, int sortOrder, int resultCode)
        {
            cache.put(sortOrder, movies);
            lastUpdate = new Date();
            if (callback != null)
                callback.onMoviesLoaded(movies, sortOrder, resultCode);
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
