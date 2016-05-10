package com.jueggs.popularmovies.util;

import android.util.Log;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class ParseUtils
{
    public static final String TAG = ParseUtils.class.getSimpleName();

    public static List<Movie> getMovieListFromJSON(String jsonString)
    {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
        List<Movie> movies = new ArrayList<>();
        try
        {
            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray(PROP_RESULTS);

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject item = (JSONObject) results.get(i);

                Movie movie = new Movie();
                movie.setId(item.getInt(PROP_ID));
                movie.setTitle(item.getString(PROP_TITLE));
                movie.setOverview(item.getString(PROP_OVERVIEW));
                movie.setPosterPath(item.getString(PROP_POSTERPATH));
                try
                {
                    movie.setReleaseDate(dateFormat.parse(item.getString(PROP_RELEASEDATE)));
                }
                catch (ParseException e)
                {
                    Log.e(TAG, e.getMessage());
                }
                movie.setVoteAverage((float) item.getDouble(PROP_VOTEAVERAGE));

                movies.add(movie);
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return movies;
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
            e.printStackTrace();
        }

        return reviews;
    }
}
