package com.jueggs.popularmovies.data.favourites.standard;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

//not used
public class FavouriteContract
{
    public static final String AUTHORITY = "com.jueggs.popularmovies";
    public static final String BASE_CONTENT_URI = "content://" + AUTHORITY;
    public static final String PATH_FAVOURITE = "favourite";
    public static final String SEP = "/";

    public static class FavouriteEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "favourite";

        public static final String COL_MOVIE_ID = "movie_id";
        public static final String COL_TITLE = "title";
        public static final String COL_REL_DATE = "rel_date";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_GENRE_IDS = "genre_ids";
        public static final String COL_ADULT = "adult";
        public static final String COL_ORIG_TITLE = "orig_title";
        public static final String COL_ORIG_LANG = "orig_lang";

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + SEP + AUTHORITY + SEP + PATH_FAVOURITE;
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + SEP + AUTHORITY + SEP + PATH_FAVOURITE;

        public static final Uri BASE_URI = Uri.parse(BASE_CONTENT_URI).buildUpon().appendEncodedPath(PATH_FAVOURITE).build();
    }
}
