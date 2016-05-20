package com.jueggs.popularmovies.data;

import android.net.Uri;
import android.util.SparseArray;
import com.jueggs.popularmovies.BuildConfig;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;

import java.util.List;

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

    public static final Uri BASE_URI = Uri.parse("http://api.themoviedb.org/3");
    public static final Uri BASE_URI_IMAGES = Uri.parse("http://image.tmdb.org/t/p");
    public static final Uri BASE_URI_YOUTUBE = Uri.parse("http://www.youtube.com/watch");

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOPRATED = "top_rated";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_AUTHENTICATION = "authentication";
    public static final String PATH_TOKEN = "token";
    public static final String PATH_NEW = "new";
    public static final String PATH_VALIDATE_LOGIN = "validate_with_login";
    public static final String PATH_SESSION = "session";

    public static final String QUERY_KEY_APIKEY = "api_key";
    public static final String QUERY_KEY_YOUTUBE_VIDEO = "v";
    public static final String QUERY_KEY_REQ_TOKEN = "request_token";
    public static final String QUERY_KEY_USERNAME = "username";
    public static final String QUERY_KEY_PASSWORD = "password";

    public static final int NUM_SORTORDER_PATHS = 2;
    public static final SparseArray<String> PATHS = new SparseArray<>(NUM_SORTORDER_PATHS);

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String PROP_ID = "id";
    public static final String PROP_RESULTS = "results";
    public static final String PROP_TITLE = "title";
    public static final String PROP_POSTERPATH = "poster_path";
    public static final String PROP_OVERVIEW = "overview";
    public static final String PROP_VOTEAVERAGE = "vote_average";
    public static final String PROP_RELEASEDATE = "release_date";
    public static final String PROP_LANGUAGE = "iso_639_1";
    public static final String PROP_REGION = "iso_3166_1";
    public static final String PROP_KEY = "key";
    public static final String PROP_NAME = "name";
    public static final String PROP_SIZE = "size";
    public static final String PROP_AUTHOR = "author";
    public static final String PROP_CONTENT = "content";
    public static final String PROP_ADULT = "adult";
    public static final String PROP_GENRE_IDS = "genre_ids";
    public static final String PROP_ORIG_TITLE = "original_title";
    public static final String PROP_ORIG_LANG = "original_language";
    public static final String PROP_REQUEST_TOKEN = "request_token";
    public static final String PROP_SUCCESS = "success";
    public static final String PROP_EXPIRATION = "expires_at";
    public static final String PROP_SESSION_ID = "session_id";

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
        PATHS.put(SORTORDER_POPULAR, PATH_POPULAR);
        PATHS.put(SORTORDER_TOPRATED, PATH_TOPRATED);

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

    public static Uri createRankingUri(int sortOrder)
    {
        return appendApiKeyQuery(BASE_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(PATHS.get(sortOrder)));
    }

    public static Uri createImageUri(String width, String posterPath)
    {
        return BASE_URI_IMAGES.buildUpon().appendPath(width).appendPath(posterPath).build();
    }

    public static Uri createTrailerUri(int id)
    {
        return appendApiKeyQuery(BASE_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(String.valueOf(id))
                .appendPath(PATH_VIDEOS));
    }

    public static Uri createReviewUri(int id)
    {
        return appendApiKeyQuery(BASE_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(String.valueOf(id))
                .appendPath(PATH_REVIEWS));
    }

    private static Uri appendApiKeyQuery(Uri.Builder builder)
    {
        return builder.appendQueryParameter(QUERY_KEY_APIKEY, API_KEY).build();
    }

    public static Uri createYoutubeUri(String key)
    {
        return BASE_URI_YOUTUBE.buildUpon()
                .appendQueryParameter(QUERY_KEY_YOUTUBE_VIDEO, key).build();
    }

    public static Uri createRequestTokenUri()
    {
        return appendApiKeyQuery(BASE_URI.buildUpon()
                .appendPath(PATH_AUTHENTICATION)
                .appendPath(PATH_TOKEN)
                .appendPath(PATH_NEW));
    }

    public static Uri createAuthenticationUri(String reqToken, String username, String password)
    {
        return appendApiKeyQuery(BASE_URI.buildUpon()
                .appendPath(PATH_AUTHENTICATION)
                .appendPath(PATH_TOKEN)
                .appendPath(PATH_VALIDATE_LOGIN)
                .appendQueryParameter(QUERY_KEY_REQ_TOKEN, reqToken)
                .appendQueryParameter(QUERY_KEY_USERNAME, username)
                .appendQueryParameter(QUERY_KEY_PASSWORD, password));
    }

    public static Uri createSessionIdUri(String reqToken)
    {
        return appendApiKeyQuery(BASE_URI.buildUpon()
                .appendPath(PATH_AUTHENTICATION)
                .appendPath(PATH_SESSION)
                .appendPath(PATH_NEW)
                .appendQueryParameter(QUERY_KEY_REQ_TOKEN, reqToken));
    }
}
