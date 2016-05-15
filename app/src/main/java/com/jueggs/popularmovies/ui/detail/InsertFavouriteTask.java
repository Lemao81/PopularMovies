package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.AsyncTask;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.model.Movie;

import static com.jueggs.popularmovies.util.Utils.transformMovieToContentValues;

public class InsertFavouriteTask extends AsyncTask<Movie, Void, Integer>
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
    protected Integer doInBackground(Movie... params)
    {
        Uri returnUri = contentResolver.insert(FavouritesProvider.Favourite.BASE_URI, transformMovieToContentValues(params[0]));
        return (int) ContentUris.parseId(returnUri);
    }

    @Override
    protected void onPostExecute(Integer rowId)
    {
        completedCallback.onFavouriteCRUDcompleted(rowId, Callback.CRUD.INSERT);
    }
}
