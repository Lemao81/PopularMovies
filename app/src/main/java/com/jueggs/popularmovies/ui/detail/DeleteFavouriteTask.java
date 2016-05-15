package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

public class DeleteFavouriteTask extends AsyncTask<Uri,Void,Void>
{
    private ContentResolver contentResolver;
    private Callback.FavouriteCRUDstarted startedCallback;
    private Callback.FavouriteCRUDcompleted completedCallback;

    public DeleteFavouriteTask(Callback.FavouriteCRUDstarted startedCallback, Callback.FavouriteCRUDcompleted completedCallback,
                               ContentResolver contentResolver)
    {
        this.startedCallback = startedCallback;
        this.completedCallback = completedCallback;
        this.contentResolver = contentResolver;
    }

    @Override
    protected void onPreExecute()
    {
        startedCallback.onFavouriteCRUDstarted();
    }

    @Override
    protected Void doInBackground(Uri... params)
    {
        contentResolver.delete(params[0], null, null);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        completedCallback.onFavouriteCRUDcompleted();
    }
}
