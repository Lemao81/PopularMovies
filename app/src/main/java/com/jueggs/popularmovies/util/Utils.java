package com.jueggs.popularmovies.util;

import android.content.ContentValues;
import android.database.Cursor;
import com.jueggs.popularmovies.data.favourites.schematic.FavouriteColumns;
import com.jueggs.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.data.favourites.schematic.FavouriteColumns.ProjectionCompleteIndices.*;


public class Utils
{
    public static void assertNotNull(Object object, String msg)
    {
        if (object == null)
            throw new NullPointerException(msg);
    }

    public static <T> boolean isEmpty(List<T> list)
    {
        return list == null || list.size() == 0;
    }

    public static List<Movie> transformCursorToMovies(Cursor cursor)
    {
        List<Movie> movies = null;
        if (cursor.moveToFirst())
        {
            movies = new ArrayList<>();

            do
            {
                Movie movie = new Movie();
                movie.setDbId(cursor.getLong(_ID));
                movie.setMovieId(cursor.getInt(MOVIE_ID));
                movie.setTitle(cursor.getString(TITLE));
                movie.setReleaseDate(new Date(cursor.getLong(REL_DATE)));
                movie.setPosterPath(cursor.getString(POSTER_PATH));
                movie.setVoteAverage(cursor.getFloat(VOTE_AVERAGE));
                movie.setOverview(cursor.getString(OVERVIEW));
                movie.decodeGenreIds(cursor.getLong(GENRE_IDS));
                movie.setAdult(cursor.getInt(ADULT) > 0);
                movie.setOriginalTitle(cursor.getString(ORIG_TITLE));
                movie.setOriginalLanguage(cursor.getString(ORIG_LANG));

                movies.add(movie);
            } while (cursor.moveToNext());
        }
        return movies;
    }

    public static ContentValues transformMovieToContentValues(Movie movie)
    {
        ContentValues values = new ContentValues();
        values.put(FavouriteColumns.ADULT, movie.isAdult());
        values.put(FavouriteColumns.GENRE_IDS, movie.encodeGenreIds());
        values.put(FavouriteColumns.MOVIE_ID, movie.getMovieId());
        values.put(FavouriteColumns.ORIG_LANG, movie.getOriginalLanguage());
        values.put(FavouriteColumns.ORIG_TITLE, movie.getOriginalTitle());
        values.put(FavouriteColumns.OVERVIEW, movie.getOverview());
        values.put(FavouriteColumns.POSTER_PATH, movie.getPosterPath());
        values.put(FavouriteColumns.TITLE, movie.getTitle());
        values.put(FavouriteColumns.REL_DATE, movie.getReleaseDate().getTime());
        values.put(FavouriteColumns.VOTE_AVERAGE, movie.getVoteAverage());
        return values;
    }
}
