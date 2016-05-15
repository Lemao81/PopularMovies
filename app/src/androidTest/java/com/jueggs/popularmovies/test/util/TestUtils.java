package com.jueggs.popularmovies.test.util;

import android.content.ContentValues;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TestUtils
{
    public static final String MENU_TOPRATED = "Top rated";
    public static final String MENU_MOSTPOPULAR = "Most popular";
    public static final String MENU_FAVOURITES = "Favourites";
    public static final String MENU_REFRESH = "Refresh";

    public static void clickOptionsMenuItem(Context context,String name)
    {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getContext());
        onView(withText(name)).perform(click());
    }
}
