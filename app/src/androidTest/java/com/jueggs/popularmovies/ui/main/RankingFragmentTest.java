package com.jueggs.popularmovies.ui.main;

import android.support.test.rule.ActivityTestRule;
import com.jueggs.popularmovies.R;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.junit.Assert.*;

public class RankingFragmentTest
{
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void posterClickOpensDetails()
    {
        //Act
        onData(withId(R.id.poster)).inAdapterView(withId(R.id.ranking)).atPosition(1).perform(click());

        //Assert
        onView(withText("Deadpool")).check(matches(isDisplayed()));
    }
}
