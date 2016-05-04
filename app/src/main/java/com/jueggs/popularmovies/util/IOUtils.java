package com.jueggs.popularmovies.util;

import android.util.Log;
import com.jueggs.popularmovies.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.jueggs.popularmovies.data.MovieContract.*;

public class IOUtils
{
    public static final String TAG = IOUtils.class.getSimpleName();

    public static String readStream(InputStream is)
    {
        StringBuilder buffer = new StringBuilder();

        if (is != null)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    buffer.append(line);
                }
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
            }
            finally
            {
                try
                {
                    is.close();
                    br.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return buffer.toString();
    }

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
}
