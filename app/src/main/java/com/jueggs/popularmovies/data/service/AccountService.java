package com.jueggs.popularmovies.data.service;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Account;
import com.jueggs.popularmovies.util.NetUtils;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.NetUtils.*;

public class AccountService
{

    public static class FetchAccountDataTask extends AsyncTask<String, Void, Account>
    {
        private Callback.RetrieveAccount finishCallback;

        public FetchAccountDataTask(Callback.RetrieveAccount finishCallback)
        {
            this.finishCallback = finishCallback;
        }

        @Override
        protected Account doInBackground(String... params)
        {
            String json = getJsonData(createAccountUri(params[0]));
            return new Gson().fromJson(json, Account.class);
        }

        @Override
        protected void onPostExecute(Account account)
        {
            finishCallback.onAccountRetrieved(account);
        }
    }
}
