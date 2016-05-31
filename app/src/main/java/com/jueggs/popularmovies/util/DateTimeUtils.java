package com.jueggs.popularmovies.util;

import com.jueggs.popularmovies.data.MovieDbContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils
{
    public static SimpleDateFormat RELEASE_DATE_FORMATER = new SimpleDateFormat(MovieDbContract.DATE_PATTERN_REL_DATE);

    public static boolean isSameDay(Date one, Date other)
    {
        return one.getDay() == other.getDay() && one.getMonth() == other.getMonth() && one.getYear() == other.getYear();
    }
}
