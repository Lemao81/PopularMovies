package com.jueggs.popularmovies.data.favourites;

import net.simonvt.schematic.annotation.*;

import static net.simonvt.schematic.annotation.ConflictResolutionType.*;
import static net.simonvt.schematic.annotation.DataType.Type.*;

public interface FavouriteColumns
{
    @DataType(INTEGER) @NotNull @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(INTEGER) @NotNull @Unique(onConflict = REPLACE) String MOVIE_ID = "movie_id";
    @DataType(TEXT) @NotNull String TITLE = "title";
    @DataType(INTEGER) @NotNull String REL_DATE = "rel_date";
    @DataType(TEXT) @NotNull String POSTER_PATH = "poster_path";
    @DataType(REAL) @NotNull String VOTE_AVERAGE = "vote_average";
    @DataType(TEXT) @NotNull String OVERVIEW = "overview";
    @DataType(INTEGER) @NotNull String GENRE_IDS = "genre_ids";
    @DataType(INTEGER) @NotNull String ADULT = "adult";
    @DataType(TEXT) @NotNull String ORIG_TITLE = "orig_title";
    @DataType(TEXT) @NotNull String ORIG_LANG = "orig_lang";
    @DataType(BLOB) @NotNull String POSTER = "poster";

    String[] PROJECTION_COMPLETE = new String[]{
            FavouriteColumns._ID,
            FavouriteColumns.MOVIE_ID,
            FavouriteColumns.TITLE,
            FavouriteColumns.REL_DATE,
            FavouriteColumns.POSTER_PATH,
            FavouriteColumns.VOTE_AVERAGE,
            FavouriteColumns.OVERVIEW,
            FavouriteColumns.GENRE_IDS,
            FavouriteColumns.ADULT,
            FavouriteColumns.ORIG_TITLE,
            FavouriteColumns.ORIG_LANG,
            FavouriteColumns.POSTER};

    interface ProjectionCompleteIndices
    {
        int _ID = 0;
        int MOVIE_ID = 1;
        int TITLE = 2;
        int REL_DATE = 3;
        int POSTER_PATH = 4;
        int VOTE_AVERAGE = 5;
        int OVERVIEW = 6;
        int GENRE_IDS = 7;
        int ADULT = 8;
        int ORIG_TITLE = 9;
        int ORIG_LANG = 10;
        int POSTER = 11;
    }
}
