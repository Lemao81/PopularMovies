package com.jueggs.popularmovies.data;

import com.jueggs.popularmovies.data.MovieDbContract;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MovieDbContractTest
{
    @Test
    public void pathAndQueryBuilder_pathAndQueries()
    {
        //Arrange
        String expected = "first/second/third?key1=value1&key2=value2";

        //Act
        String actual = MovieDbContract.paths("first", "second", "third").query("key1", "value1").query("key2", "value2").build();

        //Assert
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void pathAndQueryBuilder_pathsOnly()
    {
        //Arrange
        String expected = "first/second/third";

        //Act
        String actual = MovieDbContract.paths("first", "second", "third").build();

        //Assert
        assertThat(actual, is(equalTo(expected)));
    }
}
