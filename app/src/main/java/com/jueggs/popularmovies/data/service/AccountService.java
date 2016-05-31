package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import android.util.Log;
import com.jueggs.popularmovies.data.MovieDbService;
import com.jueggs.popularmovies.model.Account;

import java.io.IOException;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class AccountService
{

    public static class FetchAccountDataTask extends AsyncTask<String, Void, Account>
    {
        public static final String TAG = FetchAccountDataTask.class.getSimpleName();

        private Callback.RetrieveAccount finishCallback;

        public FetchAccountDataTask(Callback.RetrieveAccount finishCallback)
        {
            this.finishCallback = finishCallback;
        }

        @Override
        protected Account doInBackground(String... params)
        {
            MovieDbService service = createMovieDbService();
            try
            {
                Account account = service.getAccount(params[0]).execute().body();
                return account;
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Account account)
        {
            finishCallback.onAccountRetrieved(account);
        }
    }
}
