package com.jueggs.popularmovies.util;

import com.jueggs.popularmovies.model.Movie;

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

    public static <T> boolean isEmpty(List<T> list)
    {
        return list == null || list.size() == 0;
    }
}
