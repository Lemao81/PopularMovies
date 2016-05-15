package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

public class DeleteFavouriteTask extends AsyncTask<Uri, Void, Integer>
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
    protected Integer doInBackground(Uri... params)
    {
        return contentResolver.delete(params[0], null, null);
    }

    @Override
    protected void onPostExecute(Integer num)
    {
        completedCallback.onFavouriteCRUDcompleted(num, Callback.CRUD.DELETE);
    }
}
