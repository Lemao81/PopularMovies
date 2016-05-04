package com.jueggs.popularmovies.util;

public class Utils
{
    public static void assertNotNull(Object object, String msg)
    {
        if (object == null)
        {
            throw new NullPointerException(msg);
        }
    }
}
