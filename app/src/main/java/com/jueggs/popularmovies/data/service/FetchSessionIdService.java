package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.util.NetUtils;
import com.jueggs.popularmovies.util.ParseUtils;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.NetUtils.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;

public class FetchSessionIdService extends AsyncTask<String, Void, String>
{
    private Callback.StartRetrievingSessionId startCallback;
    private Callback.RetrieveSessionId finishCallback;

    public FetchSessionIdService(Callback.StartRetrievingSessionId startCallback, Callback.RetrieveSessionId finishCallback)
    {
        this.finishCallback = finishCallback;
        this.startCallback = startCallback;
    }

    @Override
    protected void onPreExecute()
    {
        startCallback.onRetrievingSessionIdStarted();
    }

    @Override
    protected String doInBackground(String... params)
    {
        return getSessionIdFromJSON(getJsonData(createSessionIdUri(params[0])));
    }

    @Override
    protected void onPostExecute(String sessionId)
    {
        finishCallback.onSessionIdRetrieved(sessionId);
    }
}
