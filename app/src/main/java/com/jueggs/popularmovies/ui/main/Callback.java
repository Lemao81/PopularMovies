package com.jueggs.popularmovies.ui.main;

import com.jueggs.popularmovies.model.Movie;

import java.util.List;

public interface Callback
{
    interface MovieSelected
    {
        void onMovieSelected(Movie movie);
    }

    interface MoviesLoaded
    {
        void onMoviesLoaded(List<Movie> movies, int sortOrder, int resultCode);
    }
}
