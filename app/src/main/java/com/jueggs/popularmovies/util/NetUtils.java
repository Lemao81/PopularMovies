package com.jueggs.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
}
