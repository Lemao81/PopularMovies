package com.jueggs.popularmovies.data;

import android.net.Uri;
import android.util.SparseArray;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jueggs.popularmovies.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class MovieDbContract
{
    public static final int SORTORDER_INVALID = -1;
    public static final int SORTORDER_POPULAR = 1;
    public static final int SORTORDER_TOPRATED = 2;

    public static final int MASK_SORTORDER_REFRESHABLE = SORTORDER_POPULAR | SORTORDER_TOPRATED;
    public static final int NUM_SORTORDER = 2;

    public static final int RC_OK_CACHE = 0;
    public static final int RC_OK_NETWORK = 1;
    public static final int RC_NO_NETWORK = 2;
    public static final int RC_ERROR = 3;

    public static final String BASE_URI_STRING = "http://api.themoviedb.org/3/";
    public static final Uri BASE_URI_IMAGES = Uri.parse("http://image.tmdb.org/t/p");
    public static final Uri BASE_URI_YOUTUBE = Uri.parse("http://www.youtube.com/watch");

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOPRATED = "top_rated";

    public static final String QUERY_KEY_YOUTUBE_VIDEO = "v";

    public static final int NUM_SORTORDER_PATHS = 2;
    public static final SparseArray<String> SORTORDER_PATHS = new SparseArray<>(NUM_SORTORDER_PATHS);

    public static final String IMG_WIDTH_92 = "w92";
    public static final String IMG_WIDTH_154 = "w154";
    public static final String IMG_WIDTH_185 = "w185";
    public static final String IMG_WIDTH_342 = "w342";
    public static final String IMG_WIDTH_500 = "w500";
    public static final String IMG_WIDTH_780 = "w780";
    public static final String IMG_WIDTH_ORIG = "original";

    public static final String DATE_PATTERN_REL_DATE = "yyyy-MM-dd";
    public static final String DATE_PATTERN_EXPIRATION = "yyyy-MM-dd HH:mm:ss z";

    public static final SparseArray<String> GENRES = new SparseArray<>();

    static
    {
        SORTORDER_PATHS.put(SORTORDER_POPULAR, PATH_POPULAR);
        SORTORDER_PATHS.put(SORTORDER_TOPRATED, PATH_TOPRATED);

        GENRES.put(28, "Action");
        GENRES.put(12, "Adventure");
        GENRES.put(16, "Animation");
        GENRES.put(35, "Comedy");
        GENRES.put(80, "Crime");
        GENRES.put(99, "Documentary");
        GENRES.put(18, "Drama");
        GENRES.put(10751, "Family");
        GENRES.put(14, "Fantasy");
        GENRES.put(10769, "Foreign");
        GENRES.put(36, "History");
        GENRES.put(27, "Horror");
        GENRES.put(10402, "Music");
        GENRES.put(9648, "Mystery");
        GENRES.put(10749, "Romance");
        GENRES.put(878, "Science Fiction");
        GENRES.put(10770, "TV Movie");
        GENRES.put(53, "Thriller");
        GENRES.put(10752, "War");
        GENRES.put(37, "Western");
    }

    public static MovieDbService createMovieDbService()
    {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URI_STRING)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit.create(MovieDbService.class);
    }

    public static Uri createImageUri(String width, String posterPath)
    {
        return BASE_URI_IMAGES.buildUpon().appendPath(width).appendPath(posterPath).build();
    }

    public static Uri createYoutubeUri(String key)
    {
        return BASE_URI_YOUTUBE.buildUpon()
                .appendQueryParameter(QUERY_KEY_YOUTUBE_VIDEO, key).build();
    }
}
