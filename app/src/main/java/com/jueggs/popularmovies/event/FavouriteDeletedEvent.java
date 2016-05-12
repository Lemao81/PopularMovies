package com.jueggs.popularmovies.event;

import com.jueggs.popularmovies.model.Movie;

public class FavouriteDeletedEvent
{
    public int movieId;

    public FavouriteDeletedEvent(int movieId)
    {
        this.movieId = movieId;
    }
}
