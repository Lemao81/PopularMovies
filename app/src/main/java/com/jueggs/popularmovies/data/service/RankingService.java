package com.jueggs.popularmovies.data.service;

import com.jueggs.popularmovies.ui.main.Callback;

public interface RankingService
{
    void fetchMovies(int sortOrder, Callback.MoviesLoaded callback);
}
