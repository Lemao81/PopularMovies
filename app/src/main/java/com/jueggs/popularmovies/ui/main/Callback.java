package com.jueggs.popularmovies.ui.main;

import android.view.View;
import com.jueggs.popularmovies.model.Movie;

import java.util.List;

public interface Callback
{
    interface MovieSelected
    {
        void onMovieSelected(Movie movie, View view);
    }

    interface MoviesLoaded
    {
        void onMoviesLoaded(List<Movie> movies, int sortOrder, int resultCode);
    }

    interface StartLoadingMovies
    {
        void onLoadingMoviesStarted();
    }
}
