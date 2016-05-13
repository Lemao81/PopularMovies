package com.jueggs.popularmovies.data.service;

import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.main.Callback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public class MockRankingService implements RankingService
{
    private static MockRankingService instance;

    DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);

    private MockRankingService()
    {
    }

    @Override
    public void fetchMovies(int sortOrder, Callback.MoviesLoaded callback)
    {
        switch (sortOrder)
        {
            case SORTORDER_POPULAR:
                callback.onMoviesLoaded(getMostPopularMovies(), sortOrder, RC_OK_NETWORK);
                break;
            case SORTORDER_TOPRATED:
                callback.onMoviesLoaded(getTopRatedMovies(), sortOrder, RC_OK_NETWORK);
                break;
        }
    }

    public static MockRankingService getInstance()
    {
        if (instance == null)
            instance = new MockRankingService();
        return instance;
    }

    private List<Movie> getMostPopularMovies()
    {
        Date date = null;
        try
        {
            date = dateFormat.parse("2016-04-27");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(false, new int[]{}, 271110, "en", "Captain America: Civil War", overview1, new byte[0],
                "/5N20rQURev5CNDcMjHVUZhpoCNC.jpg", date, "Captain America: Civil War", 6.95f));
        return movies;
    }

    private List<Movie> getTopRatedMovies()
    {
        Date date = null;
        try
        {
            date = dateFormat.parse("2014-10-10");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(false, new int[]{}, 244786, "en", "Whiplash", overview2, new byte[0],
                "/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg", date, "Whiplash", 8.35f));
        return movies;
    }

    private String overview1 = "Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.";
    private String overview2 = "Under the direction of a ruthless instructor, a talented young drummer begins to pursue perfection at any cost, even his humanity.";
}
