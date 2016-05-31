package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbService;
import com.jueggs.popularmovies.model.Authentication;
import com.jueggs.popularmovies.model.RequestToken;
import com.jueggs.popularmovies.model.Session;

import java.io.IOException;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public final class LoginService
{
    public static class FetchRequestTokenTask extends AsyncTask<Void, Void, RequestToken>
    {
        public static final String TAG = FetchRequestTokenTask.class.getSimpleName();

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
        protected RequestToken doInBackground(Void... params)
        {
            MovieDbService service = createMovieDbService();
            try
            {
                RequestToken token = service.requestRequestToken().execute().body();
                return token;
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(RequestToken token)
        {
            finishCallback.onRequestTokenRetrieved(token);
        }
    }


    public static class AuthenticateTask extends AsyncTask<String, Void, Boolean>
    {
        public static final String TAG = AuthenticateTask.class.getSimpleName();

        private Callback.Authentication finishCallback;

        public AuthenticateTask(Callback.Authentication finishCallback)
        {
            this.finishCallback = finishCallback;
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            MovieDbService service = createMovieDbService();
            try
            {
                Authentication authentication = service.requestAuthentication(params[0], params[1], params[2]).execute().body();
                return authentication.isSuccess();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean authenticated)
        {
            finishCallback.onAuthenticationCompleted(authenticated);
        }
    }


    public static class FetchSessionIdTask extends AsyncTask<String, Void, String>
    {
        public static final String TAG = FetchSessionIdTask.class.getSimpleName();

        private Callback.RetrieveSessionId finishCallback;

        public FetchSessionIdTask(Callback.RetrieveSessionId finishCallback)
        {
            this.finishCallback = finishCallback;
        }

        @Override
        protected String doInBackground(String... params)
        {
            MovieDbService service = createMovieDbService();
            try
            {
                Session session = service.requestSession(params[0]).execute().body();
                return session.getSessionId();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String sessionId)
        {
            finishCallback.onSessionIdRetrieved(sessionId);
        }
    }
}
