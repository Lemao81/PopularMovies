package com.jueggs.popularmovies.data;

import android.util.SparseArray;
import com.jueggs.popularmovies.Movie;
import com.jueggs.popularmovies.util.DateTimeUtils;

import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieContract.*;

public class CachedRepository
{
    private static CachedRepository instance;

    SparseArray<List<Movie>> movieLists = new SparseArray<>(NUM_SORTORDER);
    private Date day = new Date();
    private FetchService fetchService = FetchService.getInstance();

    private CachedRepository()
    {
    }

    public void getMovies(int sortorder, Callback callback)
    {
        List<Movie> movies = movieLists.get(sortorder);

        if (!isExpired() && !isEmpty(movies))
        {
            callback.onMoviesLoaded(sortorder,RC_OK, movies);
        }
        else
        {
            fetchService.fetchMovies(sortorder, moviesLoadedCallback);
        }
    }

    private Callback moviesLoadedCallback = new Callback()
    {
        @Override
        public void onMoviesLoaded(int sortorder,int resultCode, List<Movie> movies)
        {
            movieLists.put(sortorder, movies);
            day = new Date();
        }
    };

    public boolean isExpired()
    {
        return !DateTimeUtils.isSameDay(this.day, new Date());
    }

    private boolean isEmpty(List<Movie> list)
    {
        return list != null && list.size() > 0;
    }

    public static CachedRepository getInstance()
    {
        if (instance == null)
        {
            instance = new CachedRepository();
        }
        return instance;
    }


}
