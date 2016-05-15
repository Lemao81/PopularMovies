package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.os.AsyncTask;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.model.Movie;

import static com.jueggs.popularmovies.util.Utils.transformMovieToContentValues;

public class InsertFavouriteTask extends AsyncTask<Movie,Void,Void>
{
    private ContentResolver contentResolver;
    private Callback.FavouriteCRUDstarted startedCallback;
    private Callback.FavouriteCRUDcompleted completedCallback;

    public InsertFavouriteTask(Callback.FavouriteCRUDstarted startedCallback, Callback.FavouriteCRUDcompleted completedCallback, ContentResolver contentResolver)
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
    protected Void doInBackground(Movie... params)
    {
        contentResolver.insert(FavouritesProvider.Favourite.BASE_URI, transformMovieToContentValues(params[0]));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        completedCallback.onFavouriteCRUDcompleted();
    }
}
