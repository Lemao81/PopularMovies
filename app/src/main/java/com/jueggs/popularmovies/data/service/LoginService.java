package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import com.jueggs.popularmovies.model.Token;

import static com.jueggs.popularmovies.data.MovieDbContract.createAuthenticationUri;
import static com.jueggs.popularmovies.data.MovieDbContract.createRequestTokenUri;
import static com.jueggs.popularmovies.data.MovieDbContract.createSessionIdUri;
import static com.jueggs.popularmovies.util.NetUtils.getJsonData;
import static com.jueggs.popularmovies.util.ParseUtils.getAuthenticationResultFromJSON;
import static com.jueggs.popularmovies.util.ParseUtils.getSessionIdFromJSON;
import static com.jueggs.popularmovies.util.ParseUtils.getTokenFromJSON;

public final class LoginService
{
    public static class FetchRequestTokenTask extends AsyncTask<Void, Void, Token>
    {
        private Callback.StartRetrievingRequestToken startCallback;
        private Callback.RetrieveRequestToken finishCallback;

        public FetchRequestTokenTask(Callback.StartRetrievingRequestToken startCallback,Callback.RetrieveRequestToken finishCallback)
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


    public static class AuthenticateTask extends AsyncTask<String, Void, Boolean>
    {
        private Callback.StartAuthentication startCallback;
        private Callback.Authentication finishCallback;

        public AuthenticateTask(Callback.StartAuthentication startCallback, Callback.Authentication finishCallback)
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


    public static class FetchSessionIdTask extends AsyncTask<String, Void, String>
    {
        private Callback.StartRetrievingSessionId startCallback;
        private Callback.RetrieveSessionId finishCallback;

        public FetchSessionIdTask(Callback.StartRetrievingSessionId startCallback, Callback.RetrieveSessionId finishCallback)
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
}
