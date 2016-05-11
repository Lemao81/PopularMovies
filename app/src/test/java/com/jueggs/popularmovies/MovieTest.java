package com.jueggs.popularmovies;

import com.jueggs.popularmovies.model.Movie;
import org.junit.Test;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MovieTest
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
        int[] actual1 = movie1.decodeGenreIds(movie1.encodeGenreIds());
        int[] actual2 = movie2.decodeGenreIds(movie2.encodeGenreIds());
        int[] actual3 = movie3.decodeGenreIds(movie3.encodeGenreIds());
        int[] actual4 = movie4.decodeGenreIds(movie4.encodeGenreIds());
        int[] actual5 = movie5.decodeGenreIds(movie5.encodeGenreIds());
        int[] actual6 = movie6.decodeGenreIds(movie6.encodeGenreIds());

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
        Movie movie = new Movie();
        movie.setGenreIds(ids);
        return movie;
    }
}