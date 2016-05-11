package com.jueggs.popularmovies.data.favourites.standard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//not used
public class DbHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "favourites.db";
    public static final int DB_VERSION = 1;

    public DbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("CREATE TABLE %s", FavouriteContract.FavouriteEntry.TABLE_NAME));
        sb.append(" (");
        sb.append(String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT,", FavouriteContract.FavouriteEntry._ID));
        sb.append(String.format("%s INTEGER NOT NULL,", FavouriteContract.FavouriteEntry.COL_MOVIE_ID));
        sb.append(String.format("%s TEXT NOT NULL,", FavouriteContract.FavouriteEntry.COL_TITLE));
        sb.append(String.format("%s INTEGER NOT NULL,", FavouriteContract.FavouriteEntry.COL_REL_DATE));
        sb.append(String.format("%s TEXT NOT NULL,", FavouriteContract.FavouriteEntry.COL_POSTER_PATH));
        sb.append(String.format("%s REAL NOT NULL,", FavouriteContract.FavouriteEntry.COL_VOTE_AVERAGE));
        sb.append(String.format("%s TEXT NOT NULL,", FavouriteContract.FavouriteEntry.COL_OVERVIEW));
        sb.append(String.format("%s INTEGER NOT NULL,", FavouriteContract.FavouriteEntry.COL_GENRE_IDS));
        sb.append(String.format("%s INTEGER NOT NULL,", FavouriteContract.FavouriteEntry.COL_ADULT));
        sb.append(String.format("%s TEXT NOT NULL,", FavouriteContract.FavouriteEntry.COL_ORIG_TITLE));
        sb.append(String.format("%s TEXT NOT NULL,", FavouriteContract.FavouriteEntry.COL_ORIG_LANG));
        sb.append(String.format("CONSTRAINT UNIQUE (%s)", FavouriteContract.FavouriteEntry.COL_MOVIE_ID));
        sb.append(");");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s ", FavouriteContract.FavouriteEntry.TABLE_NAME));
    }
}
