package com.jueggs.popularmovies.ui.detailview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity
{
    public static final String EXTRA_MOVIE = "com.jueggs.popularmovies.extra_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(String.format(getString(R.string.format_title), getString(R.string.title_detail)));

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            Movie movie = extras.getParcelable(EXTRA_MOVIE);
            if (movie != null)
            {
                getSupportFragmentManager().beginTransaction().add(R.id.container, DetailFragment.createInstance(movie)).commit();
            }
        }
    }
}
