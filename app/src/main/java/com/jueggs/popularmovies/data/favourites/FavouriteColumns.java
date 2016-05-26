package com.jueggs.popularmovies.data.favourites;

import net.simonvt.schematic.annotation.*;

import static net.simonvt.schematic.annotation.ConflictResolutionType.*;
import static net.simonvt.schematic.annotation.DataType.Type.*;

public interface FavouriteColumns
{
    @DataType(INTEGER) @NotNull @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(TEXT) @NotNull String POSTER_PATH = "poster_path";
    @DataType(INTEGER) @NotNull String ADULT = "adult";
    @DataType(TEXT) @NotNull String OVERVIEW = "overview";
    @DataType(TEXT) @NotNull String REL_DATE = "rel_date";
    @DataType(INTEGER) @NotNull String GENRE_IDS = "genre_ids";
    @DataType(INTEGER) @NotNull @Unique(onConflict = REPLACE) String MOVIE_ID = "movie_id";
    @DataType(TEXT) @NotNull String ORIG_TITLE = "orig_title";
    @DataType(TEXT) @NotNull String ORIG_LANG = "orig_lang";
    @DataType(TEXT) @NotNull String TITLE = "title";
    @DataType(TEXT) @NotNull String BACKDROP_PATH = "backdrop_path";
    @DataType(REAL) @NotNull String POPULARITY = "popularity";
    @DataType(INTEGER) @NotNull String VOTE_COUNT = "vote_count";
    @DataType(INTEGER) @NotNull String VIDEO = "video";
    @DataType(REAL) @NotNull String VOTE_AVERAGE = "vote_average";
    @DataType(BLOB) @NotNull String POSTER = "poster";

    String[] PROJECTION_COMPLETE = new String[]{
            FavouriteColumns._ID,
            FavouriteColumns.POSTER_PATH,
            FavouriteColumns.ADULT,
            FavouriteColumns.OVERVIEW,
            FavouriteColumns.REL_DATE,
            FavouriteColumns.GENRE_IDS,
            FavouriteColumns.MOVIE_ID,
            FavouriteColumns.ORIG_TITLE,
            FavouriteColumns.ORIG_LANG,
            FavouriteColumns.TITLE,
            FavouriteColumns.BACKDROP_PATH,
            FavouriteColumns.POPULARITY,
            FavouriteColumns.VOTE_COUNT,
            FavouriteColumns.VIDEO,
            FavouriteColumns.VOTE_AVERAGE,
            FavouriteColumns.POSTER};

    interface ProjectionCompleteIndices
    {
        int _ID = 0;
        int POSTER_PATH = 1;
        int ADULT = 2;
        int OVERVIEW = 3;
        int REL_DATE = 4;
        int GENRE_IDS = 5;
        int MOVIE_ID = 6;
        int ORIG_TITLE = 7;
        int ORIG_LANG = 8;
        int TITLE = 9;
        int BACKDROP_PATH = 10;
        int POPULARITY = 11;
        int VOTE_COUNT = 12;
        int VIDEO = 13;
        int VOTE_AVERAGE = 14;
        int POSTER = 15;
    }
}
