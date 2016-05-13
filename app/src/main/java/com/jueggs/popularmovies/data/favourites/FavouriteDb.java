package com.jueggs.popularmovies.data.favourites;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(fileName = FavouriteDb.DB_NAME, version = FavouriteDb.DB_VERSION)
public class FavouriteDb
{
    public static final String DB_NAME = "favourite_fr.db";
    public static final int DB_VERSION = 1;

    @Table(FavouriteColumns.class) public static final String FAVOURITE = "favourite";
}
