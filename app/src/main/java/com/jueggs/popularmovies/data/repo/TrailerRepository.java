package com.jueggs.popularmovies.data.repo;

import android.content.Context;
import android.util.SparseArray;
import com.jueggs.popularmovies.data.service.FetchTrailerService;
import com.jueggs.popularmovies.model.Trailer;
import com.jueggs.popularmovies.ui.detail.Callback;
import com.jueggs.popularmovies.util.NetUtils;

import java.util.Collections;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.NetUtils.*;

public class TrailerRepository
{
    private static TrailerRepository instance;

    private SparseArray<List<Trailer>> cache = new SparseArray<>();
    private FetchTrailerService service = FetchTrailerService.getInstance();

    private Context context;
    private Callback.TrailerLoaded callback;
    private int movieId;

    public void loadTrailers(int movieId, Callback.TrailerLoaded callback)
    {
        if (cache.get(movieId) != null)
        {
            callback.onTrailerLoaded(Collections.unmodifiableList(cache.get(movieId)), RC_OK_CACHE);
        }
        else
        {
            if (isNetworkAvailable(context))
            {
                this.callback = callback;
                this.movieId = movieId;
                service.fetchTrailers(movieId, trailerLoadedCallback);
            }
            else
            {
                callback.onTrailerLoaded(null, RC_NO_NETWORK);
            }
        }
    }

    private Callback.TrailerLoaded trailerLoadedCallback = new Callback.TrailerLoaded()
    {
        @Override
        public void onTrailerLoaded(List<Trailer> trailers, int resultCode)
        {
            cache.put(movieId, trailers);
            if (callback != null)
                callback.onTrailerLoaded(trailers, resultCode);
        }
    };

    private TrailerRepository(Context context)
    {
        this.context = context;
    }

    public static TrailerRepository getInstance(Context context)
    {
        if (instance == null)
            instance = new TrailerRepository(context);
        return instance;
    }
}
