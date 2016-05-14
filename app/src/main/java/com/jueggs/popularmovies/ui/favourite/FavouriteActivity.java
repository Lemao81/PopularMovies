package com.jueggs.popularmovies.ui.favourite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.detail.DetailActivity;
import com.jueggs.popularmovies.ui.detail.DetailFragment;
import com.jueggs.popularmovies.ui.main.AlternativeFragment;

import static com.jueggs.popularmovies.util.Utils.*;

public class FavouriteActivity extends AppCompatActivity implements Callback.MovieSelected, Callback.MoviesLoaded
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        setTitle(String.format(getString(R.string.format_title), getString(R.string.title_favourites)));
    }

    @Override
    public void onMovieSelected(Movie movie, int position)
    {
        if (App.getInstance().isTwoPane())
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, DetailFragment.createInstance(movie)).commit();
        }
        else
        {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onMoviesLoaded(Cursor data)
    {
        Fragment fragment;
        if (data.moveToFirst())
            fragment = DetailFragment.createInstance(transformCurrentCursorPositionToMovie(data));
        else
            fragment = new AlternativeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragment).commit();
    }
}
