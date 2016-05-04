package com.jueggs.popularmovies.util;

import com.jueggs.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class Utils
{
    public static void assertNotNull(Object object, String msg)
    {
        if (object == null)
        {
            throw new NullPointerException(msg);
        }
    }

    //TODO remove
    public static List<Movie> getSampleData()
    {
        List<Movie> list = new ArrayList<>();
        Movie movie;
        for (int i = 0; i < 10; i++)
        {
            movie = new Movie();
            movie.setPosterPath("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
            list.add(movie);
        }
        return list;
    }
}
