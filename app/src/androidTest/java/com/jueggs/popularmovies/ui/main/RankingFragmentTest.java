package com.jueggs.popularmovies.ui.main;

import android.support.test.rule.ActivityTestRule;
import com.jueggs.popularmovies.R;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;

public class RankingFragmentTest
{
    @Rule
    public ActivityTestRule<RankingActivity> activityRule = new ActivityTestRule<>(RankingActivity.class);

    @Test
    public void posterClickOpensDetails()
    {
        //Act
        onData(withId(R.id.poster)).inAdapterView(withId(R.id.ranking)).atPosition(1).perform(click());

        //Assert
        onView(withText("Deadpool")).check(matches(isDisplayed()));
    }
}
