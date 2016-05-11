package com.jueggs.popularmovies.data.favourites.schematic;

import android.content.ContentResolver;
import android.net.Uri;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = FavouritesProvider.AUTHORITY, database = FavouriteDb.class)
public class FavouritesProvider
{
    public static final String AUTHORITY = "com.jueggs.popularmovies";
    public static final String BASE_CONTENT_URI = "content://" + AUTHORITY;
    public static final String PATH_FAVOURITE = "favourite";
    public static final String SEP = "/";
    public static final String VAR_NUMBERS = "/#";
    public static final String VAR_LETTERS = "/*";

    @TableEndpoint(table = FavouriteDb.FAVOURITE)
    public static class Favourite
    {
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + SEP + AUTHORITY + SEP + PATH_FAVOURITE;
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + SEP + AUTHORITY + SEP + PATH_FAVOURITE;

        @ContentUri(path = PATH_FAVOURITE, type = CONTENT_DIR_TYPE)
        public static final Uri BASE_URI = Uri.parse(BASE_CONTENT_URI).buildUpon().appendEncodedPath(PATH_FAVOURITE).build();

        @InexactContentUri(path = PATH_FAVOURITE + VAR_NUMBERS, type = CONTENT_ITEM_TYPE,name = FavouriteColumns.MOVIE_ID,
                whereColumn = FavouriteColumns.MOVIE_ID,pathSegment = 1)
        public static Uri withMovieId(int id)
        {
            return BASE_URI.buildUpon().appendEncodedPath(String.valueOf(id)).build();
        }
    }
}
