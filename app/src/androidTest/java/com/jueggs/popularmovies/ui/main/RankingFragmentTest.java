package com.jueggs.popularmovies.ui.main;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.data.service.MockRankingService;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.Utils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static com.jueggs.popularmovies.data.favourites.FavouritesProvider.*;
import static com.jueggs.popularmovies.test.util.TestUtils.*;
import static com.jueggs.popularmovies.util.Utils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.junit.Assert.assertThat;

public class RankingFragmentTest
{
    @Rule
    public ActivityTestRule<RankingActivity> activityRule = new ActivityTestRule<>(RankingActivity.class);

    private static Context context = InstrumentationRegistry.getContext();

    @BeforeClass
    public static void init()
    {
        context.getContentResolver().delete(Favourite.BASE_URI, null, null);

        context.getContentResolver().insert(Favourite.BASE_URI,
                transformMovieToContentValues(MockRankingService.mostPopularMovies.get(0)));
        context.getContentResolver().insert(Favourite.BASE_URI,
                transformMovieToContentValues(MockRankingService.mostPopularMovies.get(1)));
        context.getContentResolver().insert(Favourite.BASE_URI,
                transformMovieToContentValues(MockRankingService.topRatedMovies.get(0)));
        context.getContentResolver().insert(Favourite.BASE_URI,
                transformMovieToContentValues(MockRankingService.topRatedMovies.get(1)));
    }

    @Test
    public void posterClickOpensDetailsView()
    {
        //Act
        onData(is(instanceOf(Movie.class))).inAdapterView(withId(R.id.gridView)).atPosition(1).perform(click());

        //Assert
        onView(withText("Deadpool")).check(matches(isDisplayed()));
    }

    @Test
    public void topRatedMenuItemShowsMovies()
    {
        //Act
        clickOptionsMenuItem(context, MENU_TOPRATED);

        //Assert
        onData(equalTo(MockRankingService.topRatedMovies.get(0))).inAdapterView(withId(R.id.gridView)).check(matches(isDisplayed()));
    }

    @Test
    public void mostPopularMenuItemShowsMovies()
    {
        //Arrange
        clickOptionsMenuItem(context, MENU_TOPRATED);

        //Pre-Assert
        onData(equalTo(MockRankingService.topRatedMovies.get(0))).inAdapterView(withId(R.id.gridView)).check(matches(isDisplayed()));

        //Act
        clickOptionsMenuItem(context, MENU_MOSTPOPULAR);

        //Assert
        onData(equalTo(MockRankingService.mostPopularMovies.get(0))).inAdapterView(withId(R.id.gridView)).check(matches(isDisplayed()));
    }

    @Test
    public void favouritesMenuItemShowsFavourites()
    {
        //Act
        clickOptionsMenuItem(context, MENU_FAVOURITES);

        //Assert
        onView(withText("Captain America: Civil War")).check(matches(isDisplayed()));
        onView(withText("Deadpool")).check(matches(isDisplayed()));
        onView(withText("Whiplash")).check(matches(isDisplayed()));
        onView(withText("The Shawshank Redemption")).check(matches(isDisplayed()));
    }
}
