package com.jueggs.popularmovies.util;

import android.util.Log;
import com.jueggs.popularmovies.Movie;
import com.jueggs.popularmovies.data.MovieContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieContract.*;

public class IOUtils
{
    public static final String TAG = "IOUtils";

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
        List<Movie> movies = new ArrayList<>();
        try
        {
            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray(PROP_RESULTS);

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject item = (JSONObject) results.get(i);

                Movie movie = new Movie();

            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }
}
