package com.jueggs.popularmovies.ui.favourite;

import com.jueggs.popularmovies.model.Movie;

public interface Callback
{
    interface MovieSelected
    {
        void onMovieSelected(Movie movie);
    }

    interface MovieSwiped
    {
        void onMovieSwiped(int position);
    }
}
