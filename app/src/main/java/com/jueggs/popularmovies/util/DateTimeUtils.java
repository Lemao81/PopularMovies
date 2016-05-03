package com.jueggs.popularmovies.util;

import java.util.Date;

public class DateTimeUtils
{
    public static boolean isSameDay(Date one, Date other)
    {
        return one.getDay() == other.getDay() && one.getMonth() == other.getMonth() && one.getYear() == other.getYear();
    }
}
