package com.jueggs.popularmovies.data.favourites.schematic;

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
}
