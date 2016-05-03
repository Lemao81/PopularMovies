package com.jueggs.popularmovies.data;

import android.util.SparseArray;
import com.jueggs.popularmovies.BuildConfig;
import com.jueggs.popularmovies.Movie;

import java.util.List;

public class MovieContract
{
    public static final int SORTORDER_POPULAR = 0;
    public static final int SORTORDER_TOPRATED = 1;
    public static final int NUM_SORTORDER = 2;

    public static final int RC_OK = 0;
    public static final int RC_NONETWORK = 0;
    public static final int RC_ERROR = 0;

    public static final String BASE_URL = "http://api.themoviedb.org/3";
    public static final String PATH_POPULAR = "/movie/popular";
    public static final String PATH_TOPRATED = "/movie/top_rated";
    public static final String QUERY_KEY_APIKEY = "api_key";

    public static final int NUM_PATHS = 2;
    public static final SparseArray<String> PATHS = new SparseArray<>(NUM_PATHS);

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String PROP_RESULTS = "results";

    static {
        PATHS.put(SORTORDER_POPULAR, PATH_POPULAR);
        PATHS.put(SORTORDER_TOPRATED, PATH_TOPRATED);
    }

    interface Callback
    {
        void onMoviesLoaded(int sortorder, int resultCode, List<Movie> movies);
    }
}
