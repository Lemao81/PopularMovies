package com.jueggs.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.data.service.MockRankingService;
import com.jueggs.popularmovies.data.service.RankingService;
import com.jueggs.popularmovies.ui.favourite.MockCursorLoader;

public final class Injection
{
    public static RankingService injectRankingService()
    {
        return MockRankingService.getInstance();
    }

    public static Loader<Cursor> injectCursorLoader(Context context)
    {
        return new CursorLoader(context, FavouritesProvider.Favourite.BASE_URI, FavouriteColumns.PROJECTION_COMPLETE, null, null, null);
    }
}
