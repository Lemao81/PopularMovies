package com.jueggs.popularmovies.ui.favourite;

import android.database.Cursor;
import com.jueggs.popularmovies.model.Movie;

public interface Callback
{
    interface MovieSelected
    {
        void onMovieSelected(Movie movie, int position);
    }

    interface MovieSwiped
    {
        void onMovieSwiped(int position);
    }

    interface MoviesLoaded
    {
        void onMoviesLoaded(Cursor data);
    }
}
