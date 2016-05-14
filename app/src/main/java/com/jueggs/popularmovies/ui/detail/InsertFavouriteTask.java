package com.jueggs.popularmovies.ui.detail;

import android.content.ContentResolver;
import android.os.AsyncTask;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.model.Movie;

import static com.jueggs.popularmovies.util.Utils.transformMovieToContentValues;

public class InsertFavouriteTask extends AsyncTask<Movie,Void,Void>
{
    private ContentResolver contentResolver;
    private Callback.FavouriteCRUD callback;

    public InsertFavouriteTask(Callback.FavouriteCRUD callback, ContentResolver contentResolver)
    {
        this.callback = callback;
        this.contentResolver = contentResolver;
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
        callback.onFavouriteCRUDcompleted();
    }
}
