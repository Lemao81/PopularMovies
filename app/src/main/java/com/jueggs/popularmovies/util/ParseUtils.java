package com.jueggs.popularmovies.util;

import android.util.Log;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Token;
import com.jueggs.popularmovies.model.Trailer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class ParseUtils
{
    public static final String TAG = ParseUtils.class.getSimpleName();

    public static List<Movie> getMovieListFromJSON(String jsonString)
    {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_REL_DATE, Locale.ENGLISH);
        List<Movie> movies = new ArrayList<>();

        try
        {
            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray(PROP_RESULTS);

            for (int i = 0; i < results.length(); i++)
            {
                Movie movie = new Movie();
                JSONObject item = (JSONObject) results.get(i);
                JSONArray genreIds = item.getJSONArray(PROP_GENRE_IDS);

                movie.setMovieId(item.getInt(PROP_ID));
                movie.setTitle(item.getString(PROP_TITLE));
                movie.setOverview(item.getString(PROP_OVERVIEW));
                movie.setPosterPath(item.getString(PROP_POSTERPATH));
                movie.setVoteAverage((float) item.getDouble(PROP_VOTEAVERAGE));
                movie.setReleaseDate(dateFormat.parse(item.getString(PROP_RELEASEDATE)));
                int[] unaligned = toIntArray(genreIds);
                movie.setGenreIds(Arrays.copyOf(unaligned, 4));
                movie.setAdult(item.getBoolean(PROP_ADULT));
                movie.setOriginalTitle(item.getString(PROP_ORIG_TITLE));
                movie.setOriginalLanguage(item.getString(PROP_ORIG_LANG));

                movies.add(movie);
            }
        }
        catch (JSONException | ParseException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return movies;
    }

    public static int[] toIntArray(JSONArray jsonArray) throws JSONException
    {
        int length = jsonArray.length();
        int[] ints = new int[length];
        for (int i = 0; i < length; i++)
        {
            ints[i] = jsonArray.getInt(i);
        }
        return ints;
    }

    public static List<Trailer> getTrailerListFromJSON(String jsonString)
    {
        List<Trailer> trailers = new ArrayList<>();

        try
        {
            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray(PROP_RESULTS);

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject item = (JSONObject) results.get(i);

                Trailer trailer = new Trailer();
                trailer.setMovieId(root.getInt(PROP_ID));
                trailer.setLanguage(item.getString(PROP_LANGUAGE));
                trailer.setRegion(item.getString(PROP_REGION));
                trailer.setKey(item.getString(PROP_KEY));
                trailer.setName(item.getString(PROP_NAME));
                trailer.setSize(item.getInt(PROP_SIZE));

                trailers.add(trailer);
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return trailers;
    }

    public static List<Review> getReviewListFromJSON(String jsonString)
    {
        List<Review> reviews = new ArrayList<>();

        try
        {
            JSONObject root = new JSONObject(jsonString);

            JSONArray results = root.getJSONArray(PROP_RESULTS);
            for (int i = 0; i < results.length(); i++)
            {
                JSONObject item = (JSONObject) results.get(i);

                Review review = new Review();
                review.setMovieId(root.getInt(PROP_ID));
                review.setAuthor(item.getString(PROP_AUTHOR));
                review.setReview(item.getString(PROP_CONTENT));

                reviews.add(review);
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return reviews;
    }

    public static Token getTokenFromJSON(String jsonString)
    {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_EXPIRATION, Locale.ENGLISH);

        Token token = new Token();
        try
        {
            JSONObject root = new JSONObject(jsonString);

            token.setToken(root.getString(PROP_REQUEST_TOKEN));
            token.setSuccess(root.getBoolean(PROP_SUCCESS));
            try
            {
                token.setExpiration(dateFormat.parse(root.getString(PROP_EXPIRATION)));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return token;
    }

    public static boolean getAuthenticationResultFromJSON(String jsonString)
    {
        try
        {
            JSONObject root = new JSONObject(jsonString);

            return root.getBoolean(PROP_SUCCESS);
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static String getSessionIdFromJSON(String jsonString)
    {
        try
        {
            JSONObject root = new JSONObject(jsonString);

            if (root.getBoolean(PROP_SUCCESS))
                return root.getString(PROP_SESSION_ID);
            else
                return null;
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
            return null;
        }


    }
}
