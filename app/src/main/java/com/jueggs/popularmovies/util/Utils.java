package com.jueggs.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.data.favourites.FavouriteColumns.ProjectionCompleteIndices.*;


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

    public static <T> boolean hasElements(List<T> list)
    {
        return !isEmpty(list);
    }

    public static boolean isEmpty(byte[] array)
    {
        return array == null || array.length == 0;
    }

    public static boolean hasElements(byte[] array)
    {
        return !isEmpty(array);
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
                movie.setGenreIds(decodeGenreIds(cursor.getLong(GENRE_IDS)));
                movie.setAdult(cursor.getInt(ADULT) > 0);
                movie.setOriginalTitle(cursor.getString(ORIG_TITLE));
                movie.setOriginalLanguage(cursor.getString(ORIG_LANG));
                movie.setPoster(cursor.getBlob(POSTER));

                movies.add(movie);
            } while (cursor.moveToNext());
        }
        return movies;
    }

    public static Movie transformCurrentCursorPositionToMovie(Cursor cursor)
    {
        Movie movie = new Movie();
        movie.setDbId(cursor.getLong(_ID));
        movie.setMovieId(cursor.getInt(MOVIE_ID));
        movie.setTitle(cursor.getString(TITLE));
        movie.setReleaseDate(new Date(cursor.getLong(REL_DATE)));
        movie.setPosterPath(cursor.getString(POSTER_PATH));
        movie.setVoteAverage(cursor.getFloat(VOTE_AVERAGE));
        movie.setOverview(cursor.getString(OVERVIEW));
        movie.setGenreIds(decodeGenreIds(cursor.getLong(GENRE_IDS)));
        movie.setAdult(cursor.getInt(ADULT) > 0);
        movie.setOriginalTitle(cursor.getString(ORIG_TITLE));
        movie.setOriginalLanguage(cursor.getString(ORIG_LANG));
        movie.setPoster(cursor.getBlob(POSTER));

        return movie;
    }

    public static ContentValues transformMovieToContentValues(Movie movie)
    {
        ContentValues values = new ContentValues();
        values.put(FavouriteColumns.ADULT, movie.isAdult());
        values.put(FavouriteColumns.GENRE_IDS, encodeGenreIds(movie.getGenreIds()));
        values.put(FavouriteColumns.MOVIE_ID, movie.getMovieId());
        values.put(FavouriteColumns.ORIG_LANG, movie.getOriginalLanguage());
        values.put(FavouriteColumns.ORIG_TITLE, movie.getOriginalTitle());
        values.put(FavouriteColumns.OVERVIEW, movie.getOverview());
        values.put(FavouriteColumns.POSTER_PATH, movie.getPosterPath());
        values.put(FavouriteColumns.TITLE, movie.getTitle());
        values.put(FavouriteColumns.REL_DATE, movie.getReleaseDate().getTime());
        values.put(FavouriteColumns.VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavouriteColumns.POSTER, movie.getPoster());
        return values;
    }

    public static String createGenreString(int[] genreIds)
    {
        StringBuilder sb = new StringBuilder();
        int length = genreIds.length;
        for (int i = 0; i < length && genreIds[i] != 0; i++)
        {
            if (i != 0) sb.append(", ");
            String genre = MovieDbContract.GENRES.get(genreIds[i]);
            sb.append(genre != null ? genre : "Unknown");
        }
        return sb.toString();
    }

    //save up to 4 genre ids as 16 bit chunks in a long value for db storage
    public static long encodeGenreIds(int[] ids)
    {
        long encoded = 0;

        for (int i = Movie.MAX_GENRE_IDS - 1; i >= 0; i--)
        {
            int shift = i * Movie.ENCODE_BIT_SHIFT;
            int id = ids[i];
            encoded = ((encoded >> shift) | (0xffff & id)) << shift;
        }
        return encoded;
    }

    public static int[] decodeGenreIds(long encoded)
    {
        int[] ids = new int[Movie.MAX_GENRE_IDS];

        for (int i = 0; i < Movie.MAX_GENRE_IDS; i++)
        {
            int shift = i * Movie.ENCODE_BIT_SHIFT;
            ids[i] = (int) ((encoded >> shift) & 0xffff);
        }
        return ids;
    }

    public static void loadImage(Context context, String width, String posterPath, ImageView view)
    {
        loadImage(context, width, posterPath, view, null);
    }

    public static void loadImage(Context context, String width, String posterPath, ImageView view, Callback callback)
    {
        Uri uri = createImageUri(width, posterPath);
        Picasso.with(context).load(uri).placeholder(R.drawable.picasso_placeholder)
                .error(R.drawable.picasso_error).into(view, callback);
    }

    public static byte[] convertDrawableToByteArray(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Drawable convertByteArrayToDrawable(Resources resources, byte[] bytes)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return new BitmapDrawable(resources, bitmap);
    }

    public static void sleepSafely(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
