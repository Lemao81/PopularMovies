package com.jueggs.popularmovies.ui.favourite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.detail.DetailActivity;
import com.jueggs.popularmovies.ui.detail.DetailFragment;
import com.jueggs.popularmovies.ui.main.StartupFragment;

public class FavouriteActivity extends AppCompatActivity implements Callback.MovieSelected
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        setTitle(String.format(getString(R.string.format_title), getString(R.string.title_favourites)));

        if (App.getInstance().isTwoPane())
        {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment == null)
            {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new StartupFragment()).commit();
            }
        }
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
            this.startActivity(intent);
        }
    }

}
