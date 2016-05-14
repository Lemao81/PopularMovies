package com.jueggs.popularmovies.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.stetho.urlconnection.StethoURLConnectionManager;
import com.google.common.io.ByteStreams;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.receiver.NetworkChangeReceiver;

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

    public static void enableNetworkChangeReceiver(Context context, boolean enabled)
    {
        ComponentName receiver = new ComponentName(context, NetworkChangeReceiver.class);
        PackageManager pm = context.getPackageManager();
        int state = enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        pm.setComponentEnabledSetting(receiver, state, PackageManager.DONT_KILL_APP);
    }
}
