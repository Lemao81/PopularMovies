package com.jueggs.popularmovies.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils
{
    public static final String TAG = "NetUtils";

    public static InputStream getStream(Uri uri)
    {
        InputStream is = null;
        HttpURLConnection connection = null;
        try
        {
            URL url = new URL(uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            is = connection.getInputStream();
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
        return is;
    }
}
