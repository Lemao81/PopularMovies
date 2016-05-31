package com.jueggs.popularmovies;

import android.content.ContentValues;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.Utils;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UtilsAndroidTest
{
    @Test
    public void createGenreString()
    {
        //Arrange
        String expected1 = "Action, Adventure, Animation, Comedy";
        String expected2 = "Action, Unknown, Animation, Comedy";
        String expected3 = "Action, Comedy";

        //Act
        String actual1 = Utils.createGenreString(new int[]{28, 12, 16, 35});
        String actual2 = Utils.createGenreString(new int[]{28, 999, 16, 35});
        String actual3 = Utils.createGenreString(new int[]{28, 35, 0, 0});

        //Assert
        assertThat(actual1, is(equalTo(expected1)));
        assertThat(actual2, is(equalTo(expected2)));
        assertThat(actual3, is(equalTo(expected3)));
    }

    @Test
    public void transformMovieToContentValues()
    {
        //Arrange
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

        Movie expected = Movie.builder().posterPath(posterPath).adult(adult).overview(overview).releaseDate(releaseDate).genreIds(genreIds)
                .id(movieId).originalTitle(origTitle).originalLanguage(origLang).title(title).backdropPath(backdropPath).popularity(popularity)
                .voteCount(voteCount).video(video).voteAverage(voteAverage).poster(poster).build();

        //Act
        ContentValues values = Utils.transformMovieToContentValues(expected);

        Movie actual = Movie.builder().build();
        actual.setPosterPath(values.getAsString(FavouriteColumns.POSTER_PATH));
        actual.setAdult(values.getAsBoolean(FavouriteColumns.ADULT));
        actual.setOverview(values.getAsString(FavouriteColumns.OVERVIEW));
        actual.setReleaseDate(values.getAsString(FavouriteColumns.REL_DATE));
        actual.setGenreIds(Utils.decodeGenreIds(values.getAsLong(FavouriteColumns.GENRE_IDS)));
        actual.setId(values.getAsInteger(FavouriteColumns.MOVIE_ID));
        actual.setOriginalTitle(values.getAsString(FavouriteColumns.ORIG_TITLE));
        actual.setOriginalLanguage(values.getAsString(FavouriteColumns.ORIG_LANG));
        actual.setTitle(values.getAsString(FavouriteColumns.TITLE));
        actual.setBackdropPath(values.getAsString(FavouriteColumns.BACKDROP_PATH));
        actual.setPopularity(values.getAsFloat(FavouriteColumns.POPULARITY));
        actual.setVoteCount(values.getAsInteger(FavouriteColumns.VOTE_COUNT));
        actual.setVideo(values.getAsBoolean(FavouriteColumns.VIDEO));
        actual.setVoteAverage(values.getAsFloat(FavouriteColumns.VOTE_AVERAGE));
        actual.setPoster(values.getAsByteArray(FavouriteColumns.POSTER));

        //Assert
        assertTrue(values.containsKey(FavouriteColumns.POSTER_PATH));
        assertTrue(values.containsKey(FavouriteColumns.ADULT));
        assertTrue(values.containsKey(FavouriteColumns.OVERVIEW));
        assertTrue(values.containsKey(FavouriteColumns.REL_DATE));
        assertTrue(values.containsKey(FavouriteColumns.GENRE_IDS));
        assertTrue(values.containsKey(FavouriteColumns.MOVIE_ID));
        assertTrue(values.containsKey(FavouriteColumns.ORIG_TITLE));
        assertTrue(values.containsKey(FavouriteColumns.ORIG_LANG));
        assertTrue(values.containsKey(FavouriteColumns.TITLE));
        assertTrue(values.containsKey(FavouriteColumns.BACKDROP_PATH));
        assertTrue(values.containsKey(FavouriteColumns.POPULARITY));
        assertTrue(values.containsKey(FavouriteColumns.VOTE_COUNT));
        assertTrue(values.containsKey(FavouriteColumns.VIDEO));
        assertTrue(values.containsKey(FavouriteColumns.VOTE_AVERAGE));
        assertTrue(values.containsKey(FavouriteColumns.POSTER));

        assertThat(values.size(), is(equalTo(15)));

        assertThat(actual, is(equalTo(expected)));
    }
}
