package com.jueggs.popularmovies.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.data.favourites.FavouriteDb;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.DateTimeUtils;
import com.jueggs.popularmovies.util.Utils;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.Unique;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.data.favourites.FavouriteColumns.ProjectionCompleteIndices.*;
import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;
import static net.simonvt.schematic.annotation.DataType.Type.*;
import static org.mockito.Mockito.*;

import static com.jueggs.popularmovies.util.Utils.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UtilsTest
{
    @Test
    public void encode_decode_genreIds()
    {
        //Arrange
        int[] expected1 = new int[]{28, 12, 35, 10749};
        int[] expected2 = new int[]{18, 9648, 878, 53};
        int[] expected3 = new int[]{18, 9648, 53, 0};
        int[] expected4 = new int[]{12, 66, 0, 0};
        int[] expected5 = new int[]{99, 0, 0, 0};
        int[] expected6 = new int[]{0, 0, 0, 0};

        Movie movie1 = createWithGenreIds(expected1);
        Movie movie2 = createWithGenreIds(expected2);
        Movie movie3 = createWithGenreIds(expected3);
        Movie movie4 = createWithGenreIds(expected4);
        Movie movie5 = createWithGenreIds(expected5);
        Movie movie6 = createWithGenreIds(expected6);

        //Act
        decodeGenreIds(encodeGenreIds(movie1.getGenreIds()));
        decodeGenreIds(encodeGenreIds(movie2.getGenreIds()));
        decodeGenreIds(encodeGenreIds(movie3.getGenreIds()));
        decodeGenreIds(encodeGenreIds(movie4.getGenreIds()));
        decodeGenreIds(encodeGenreIds(movie5.getGenreIds()));
        decodeGenreIds(encodeGenreIds(movie6.getGenreIds()));
        int[] actual1 = movie1.getGenreIds();
        int[] actual2 = movie2.getGenreIds();
        int[] actual3 = movie3.getGenreIds();
        int[] actual4 = movie4.getGenreIds();
        int[] actual5 = movie5.getGenreIds();
        int[] actual6 = movie6.getGenreIds();

        //Assert
        assertArrayEquals(expected1, actual1);
        assertArrayEquals(expected2, actual2);
        assertArrayEquals(expected3, actual3);
        assertArrayEquals(expected4, actual4);
        assertArrayEquals(expected5, actual5);
        assertArrayEquals(expected6, actual6);
    }

    private Movie createWithGenreIds(int[] ids)
    {
        Movie movie = Movie.builder().build();
        movie.setGenreIds(ids);
        return movie;
    }

    @Test
    public void transformCursorToMovies()
    {
        //Arrange
        long dbId = 1L;
        String posterPath = "the poster path";
        boolean adult = true;
        String overview = "the overview";
        String releaseDate = "2014-10-23";
        int[] genreIds = new int[]{3, 8, 23, 0};
        int movieId = 20302;
        String origTitle = "the origtitle";
        String origLang = "the origlanguage";
        String title = "the title";
        String backdropPath = "backdroppath";
        float popularity = 45.2f;
        int voteCount=7;
        boolean video=true;
        float voteAverage = 5.8f;
        byte[] poster = new byte[]{-2, 5, 2, 5, -53, 34, 53};

        Movie expected = Movie.builder().dbId(dbId).posterPath(posterPath).adult(adult).overview(overview).releaseDate(releaseDate).genreIds(genreIds)
                .id(movieId).originalTitle(origTitle).originalLanguage(origLang).title(title).backdropPath(backdropPath).popularity(popularity)
                .voteCount(voteCount).video(video).voteAverage(voteAverage).poster(poster).build();

        long dbId2 = 2L;
        String posterPath2 = "the poster path2";
        boolean adult2 = false;
        String overview2 = "the overview2";
        String releaseDate2 = "2013-11-23";
        int[] genreIds2 = new int[]{1, 8, 13, 0};
        int movieId2 = 2502;
        String origTitle2 = "the origtitle2";
        String origLang2 = "the origlanguage2";
        String title2 = "the title2";
        String backdropPath2 = "backdroppath2";
        float popularity2 = 15.7f;
        int voteCount2=6;
        boolean video2=false;
        float voteAverage2 = 7.8f;
        byte[] poster2 = new byte[]{-2, 5, 1, 5, -53, 84, 53};

        Movie expected2 = Movie.builder().dbId(dbId2).posterPath(posterPath2).adult(adult2).overview(overview2).releaseDate(releaseDate2).genreIds(genreIds2)
                .id(movieId2).originalTitle(origTitle2).originalLanguage(origLang2).title(title2).backdropPath(backdropPath2).popularity(popularity2)
                .voteCount(voteCount2).video(video2).voteAverage(voteAverage2).poster(poster2).build();

        Cursor cursor = mock(Cursor.class);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext()).thenReturn(true, false);
        when(cursor.getLong(_ID)).thenReturn(dbId, dbId2);
        when(cursor.getInt(MOVIE_ID)).thenReturn(movieId, movieId2);
        when(cursor.getString(TITLE)).thenReturn(title, title2);
        when(cursor.getString(REL_DATE)).thenReturn(releaseDate, releaseDate2);
        when(cursor.getString(POSTER_PATH)).thenReturn(posterPath, posterPath2);
        when(cursor.getFloat(VOTE_AVERAGE)).thenReturn(voteAverage, voteAverage2);
        when(cursor.getString(OVERVIEW)).thenReturn(overview, overview2);
        when(cursor.getLong(GENRE_IDS)).thenReturn(Utils.encodeGenreIds(genreIds), Utils.encodeGenreIds(genreIds2));
        when(cursor.getInt(ADULT)).thenReturn(adult ? 1 : 0, adult2 ? 1 : 0);
        when(cursor.getString(ORIG_TITLE)).thenReturn(origTitle, origTitle2);
        when(cursor.getString(ORIG_LANG)).thenReturn(origLang, origLang2);
        when(cursor.getBlob(POSTER)).thenReturn(poster, poster2);

        //Act
        List<Movie> actual = Utils.transformCursorToMovies(cursor);

        //Assert
        assertThat(actual.get(0), is(equalTo(expected)));
        assertThat(actual.get(1), is(equalTo(expected2)));
    }

    @Test
    public void transformCurrentCursorPositionToMovie()
    {
        //Arrange
        long dbId = 1L;
        String posterPath = "the poster path";
        boolean adult = true;
        String overview = "the overview";
        String releaseDate = "2014-10-23";
        int[] genreIds = new int[]{3, 8, 23, 0};
        int movieId = 20302;
        String origTitle = "the origtitle";
        String origLang = "the origlanguage";
        String title = "the title";
        String backdropPath = "backdroppath";
        float popularity = 45.2f;
        int voteCount=7;
        boolean video=true;
        float voteAverage = 5.8f;
        byte[] poster = new byte[]{-2, 5, 2, 5, -53, 34, 53};

        Movie expected = Movie.builder().dbId(dbId).posterPath(posterPath).adult(adult).overview(overview).releaseDate(releaseDate).genreIds(genreIds)
                .id(movieId).originalTitle(origTitle).originalLanguage(origLang).title(title).backdropPath(backdropPath).popularity(popularity)
                .voteCount(voteCount).video(video).voteAverage(voteAverage).poster(poster).build();

        Cursor cursor = mock(Cursor.class);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext()).thenReturn(true, false);
        when(cursor.getLong(_ID)).thenReturn(dbId);
        when(cursor.getInt(MOVIE_ID)).thenReturn(movieId);
        when(cursor.getString(TITLE)).thenReturn(title);
        when(cursor.getLong(REL_DATE)).thenReturn(relaseDate.getTime());
        when(cursor.getString(POSTER_PATH)).thenReturn(posterPath);
        when(cursor.getFloat(VOTE_AVERAGE)).thenReturn(voteAverage);
        when(cursor.getString(OVERVIEW)).thenReturn(overview);
        when(cursor.getLong(GENRE_IDS)).thenReturn(Utils.encodeGenreIds(genreIds));
        when(cursor.getInt(ADULT)).thenReturn(adult ? 1 : 0);
        when(cursor.getString(ORIG_TITLE)).thenReturn(origTitle);
        when(cursor.getString(ORIG_LANG)).thenReturn(origLang);
        when(cursor.getBlob(POSTER)).thenReturn(poster);

        //Act
        Movie actual = Utils.transformCurrentCursorPositionToMovie(cursor);

        //Assert
        assertThat(actual, is(equalTo(expected)));
    }





}