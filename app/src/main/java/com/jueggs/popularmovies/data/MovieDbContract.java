package com.jueggs.popularmovies.data;

import android.util.SparseArray;
import com.jueggs.popularmovies.BuildConfig;
import com.jueggs.popularmovies.model.Movie;

import java.util.List;

public class MovieDbContract
{
    public static final int SORTORDER_INVALID = -1;
    public static final int SORTORDER_POPULAR = 1;
    public static final int SORTORDER_TOPRATED = 2;
    public static final int MASK_SORTORDER_VALID = SORTORDER_POPULAR | SORTORDER_TOPRATED;
    public static final int NUM_SORTORDER = 2;

    public static final int RC_OK_CACHE = 0;
    public static final int RC_OK_NETWORK = 1;
    public static final int RC_NO_NETWORK = 2;
    public static final int RC_ERROR = 3;

    public static final String BASE_URL_MOVIES = "http://api.themoviedb.org/3";
    public static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p";
    public static final String PATH_POPULAR = "movie/popular";
    public static final String PATH_TOPRATED = "movie/top_rated";
    public static final String QUERY_KEY_APIKEY = "api_key";

    public static final int NUM_PATHS = 2;
    public static final SparseArray<String> PATHS = new SparseArray<>(NUM_PATHS);

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String PROP_RESULTS = "results";
    public static final String PROP_TITLE = "title";
    public static final String PROP_POSTERPATH = "poster_path";
    public static final String PROP_OVERVIEW = "overview";
    public static final String PROP_VOTEAVERAGE = "vote_average";
    public static final String PROP_RELEASEDATE = "release_date";

    public static final String IMG_WIDTH_92 = "w92";
    public static final String IMG_WIDTH_154 = "w154";
    public static final String IMG_WIDTH_185 = "w185";
    public static final String IMG_WIDTH_342 = "w342";
    public static final String IMG_WIDTH_500 = "w500";
    public static final String IMG_WIDTH_780 = "w780";
    public static final String IMG_WIDTH_ORIG = "original";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    static
    {
        PATHS.put(SORTORDER_POPULAR, PATH_POPULAR);
        PATHS.put(SORTORDER_TOPRATED, PATH_TOPRATED);
    }

    public interface Callback
    {
        void onMoviesLoaded(int sortOrder, int resultCode, List<Movie> movies);
    }
}
