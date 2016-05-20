package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.util.NetUtils;
import com.jueggs.popularmovies.util.ParseUtils;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.NetUtils.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;

public class AuthenticateService extends AsyncTask<String, Void, Boolean>
{
    private Callback.StartAuthentication startCallback;
    private Callback.Authentication finishCallback;

    public AuthenticateService(Callback.StartAuthentication startCallback, Callback.Authentication finishCallback)
    {
        this.finishCallback = finishCallback;
        this.startCallback = startCallback;
    }

    @Override
    protected void onPreExecute()
    {
        startCallback.onAuthenticationStarted();
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        return getAuthenticationResultFromJSON(getJsonData(createAuthenticationUri(params[0], params[1], params[2])));
    }

    @Override
    protected void onPostExecute(Boolean authenticated)
    {
        finishCallback.onAuthenticationCompleted(authenticated);
    }
}
