package com.jueggs.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.stetho.urlconnection.StethoURLConnectionManager;
import com.google.common.io.ByteStreams;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Movie;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class NetUtils
{
    public static final String TAG = NetUtils.class.getSimpleName();

    public static String getJsonData(Uri uri)
    {
        String result = null;
        HttpURLConnection connection = null;
        StethoURLConnectionManager stethoManager = new StethoURLConnectionManager(StethoURLConnectionManager.class.getSimpleName());

        try
        {
            URL url = new URL(uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            InputStream is = connection.getInputStream();
            is = stethoManager.interpretResponseStream(is);

            result = IOUtils.readStream(is);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        return result;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void loadImages(List<Movie> movies, String width)
    {
        for (Movie movie : movies)
        {
            if (!TextUtils.isEmpty(movie.getPosterPath()))
            {
                String url = createImageUri(width, movie.getPosterPath()).toString();
                movie.setPoster(loadImage(url));
            }
        }
    }

    private static byte[] loadImage(String url)
    {
        try
        {
            return ByteStreams.toByteArray(new BufferedInputStream(new URL(url).openStream()));
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
            return new byte[0];
        }
    }
}
