package com.jueggs.popularmovies.util;

import android.util.Log;
import com.google.common.io.Files;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

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
}
