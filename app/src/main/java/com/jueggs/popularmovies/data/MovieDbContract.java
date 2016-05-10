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
    public static final int MASK_SORTORDER_VALID = SORTORDER_POPULAR | SORTORDER_TOPRATED;
    public static final int NUM_SORTORDER = 2;

    public static final int RC_OK_CACHE = 0;
    public static final int RC_OK_NETWORK = 1;
    public static final int RC_NO_NETWORK = 2;
    public static final int RC_ERROR = 3;

    public static final String BASE_URL_MOVIES = "http://api.themoviedb.org/3/movie";
    public static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p";
    public static final String BASE_URL_YOUTUBE = "http://www.youtube.com/watch";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOPRATED = "top_rated";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS = "reviews";
    public static final String QUERY_KEY_APIKEY = "api_key";
    public static final String QUERY_KEY_YOUTUBE_VIDEO = "v";

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

    public static Uri createRankingUri(int sortOrder)
    {
        return appendApiKeyQuery(Uri.parse(BASE_URL_MOVIES).buildUpon().appendEncodedPath(PATHS.get(sortOrder)));
    }

    public static Uri createImageUri(String width, String posterPath)
    {
        return Uri.parse(BASE_URL_IMAGES).buildUpon().appendEncodedPath(width).appendEncodedPath(posterPath).build();
    }

    public static Uri createTrailerUri(int id)
    {
        return appendApiKeyQuery(Uri.parse(BASE_URL_MOVIES).buildUpon().appendEncodedPath(String.valueOf(id)).appendEncodedPath(PATH_VIDEOS));
    }

    public static Uri createReviewUri(int id)
    {
        return appendApiKeyQuery(Uri.parse(BASE_URL_MOVIES).buildUpon().appendEncodedPath(String.valueOf(id)).appendEncodedPath(PATH_REVIEWS));
    }

    private static Uri appendApiKeyQuery(Uri.Builder builder)
    {
        return builder.appendQueryParameter(QUERY_KEY_APIKEY, BuildConfig.API_KEY).build();
    }

    public static Uri createYoutubeUri(String key)
    {
        return Uri.parse(BASE_URL_YOUTUBE).buildUpon().appendQueryParameter(QUERY_KEY_YOUTUBE_VIDEO, key).build();
    }

    public interface MovieLoadedCallback
    {
        void onMoviesLoaded(int sortOrder, int resultCode, List<Movie> movies);
    }

    public interface TrailerLoadedCallback
    {
        void onTrailerLoaded(List<Trailer> trailers, int resultCode);
    }

    public interface ReviewLoadedCallback
    {
        void onReviewLoaded(List<Review> reviews, int resultCode);
    }
}
