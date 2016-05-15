package com.jueggs.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.data.service.FetchRankingService;
import com.jueggs.popularmovies.data.service.RankingService;

public final class Injection
{
    public static RankingService injectRankingService()
    {
        return FetchRankingService.getInstance();
    }

    public static Loader<Cursor> injectCursorLoader(Context context)
    {
        return new CursorLoader(context, FavouritesProvider.Favourite.BASE_URI, FavouriteColumns.PROJECTION_COMPLETE, null, null, null);
    }

    public static String injectDbName()
    {
        return "favourite_fr.db";
    }
}
