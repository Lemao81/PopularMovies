package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Token;
import com.jueggs.popularmovies.util.NetUtils;
import com.jueggs.popularmovies.util.ParseUtils;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.NetUtils.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;

public class FetchRequestTokenService extends AsyncTask<Void, Void, Token>
{
    private Callback.StartRetrievingRequestToken startCallback;
    private Callback.RetrieveRequestToken finishCallback;

    public FetchRequestTokenService(Callback.StartRetrievingRequestToken startCallback,Callback.RetrieveRequestToken finishCallback)
    {
        this.finishCallback = finishCallback;
        this.startCallback = startCallback;
    }

    @Override
    protected void onPreExecute()
    {
        startCallback.onRetrievingRequestTokenStarted();
    }

    @Override
    protected Token doInBackground(Void... params)
    {
        return getTokenFromJSON(getJsonData(createRequestTokenUri()));
    }

    @Override
    protected void onPostExecute(Token token)
    {
        finishCallback.onRequestTokenRetrieved(token);
    }
}
