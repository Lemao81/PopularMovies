package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

public class DeleteFavouriteTask extends AsyncTask<Uri,Void,Void>
{
    private ContentResolver contentResolver;
    private Callback.FavouriteCRUD callback;

    public DeleteFavouriteTask(Callback.FavouriteCRUD callback, ContentResolver contentResolver)
    {
        this.callback = callback;
        this.contentResolver = contentResolver;
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
        callback.onFavouriteCRUDcompleted();
    }
}
