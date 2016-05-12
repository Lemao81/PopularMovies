package com.jueggs.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.detail.DetailActivity;
import com.jueggs.popularmovies.ui.detail.DetailFragment;

import java.util.List;

import static com.jueggs.popularmovies.util.Utils.*;

public class MainActivity extends AppCompatActivity implements Callback.MovieSelected, Callback.MoviesLoaded
{
    private boolean startup = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getInstance().setTwoPane(findViewById(R.id.container) != null);
    }

    @Override
    public void onMovieSelected(Movie movie)
    {
        if (App.getInstance().isTwoPane())
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, DetailFragment.createInstance(movie)).commit();
        }
        else
        {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies, int sortOrder, int resultCode)
    {
        if (App.getInstance().isTwoPane() && startup)
        {
            Fragment fragment;
            if (!isEmpty(movies))
            {
                fragment = DetailFragment.createInstance(movies.get(0));
                ((RankingFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.ranking_fragment_tag))).selectFirstItem();
            }
            else
            {
                fragment = new AlternativeFragment();
            }

            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
            startup = false;
        }
    }
}
