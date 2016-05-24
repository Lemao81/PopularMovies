package com.jueggs.popularmovies.data.service;

import com.jueggs.popularmovies.data.MovieDbContract;
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

    public static List<Movie> mostPopularMovies;
    public static List<Movie> topRatedMovies;
    private static DateFormat dateFormat;

    static
    {
        String overviewPopular1 = "Following the events of Age of Ultron, the collective governments of the world pass an act designed to regulate all superhuman activity. This polarizes opinion amongst the Avengers, causing two factions to side with Iron Man or Captain America, which causes an epic battle between former allies.";
        String overviewPopular2 = "Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.";
        String overviewTopRated1 = "Under the direction of a ruthless instructor, a talented young drummer begins to pursue perfection at any cost, even his humanity.";
        String overviewTopRated2 = "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.";

        dateFormat = new SimpleDateFormat(DATE_PATTERN_REL_DATE, Locale.ENGLISH);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(false, new int[]{28, 53, 878, 0}, 271110, "en", "Captain America: Civil War", overviewPopular1, new byte[0],
                "/5N20rQURev5CNDcMjHVUZhpoCNC.jpg", parseToDate("2016-04-27"), "Captain America: Civil War", 6.95f));
        movies.add(new Movie(false, new int[]{28, 12, 35, 10749}, 293660, "en", "Deadpool", overviewPopular2, new byte[0],
                "/k1QUCjNAkfRpWfm1dVJGUmVHzGv.jpg", parseToDate("2016-02-09"), "Deadpool", 7.21f));
        mostPopularMovies = movies;

        movies = new ArrayList<>();
        movies.add(new Movie(false, new int[]{18, 10402, 0, 0}, 244786, "en", "Whiplash", overviewTopRated1, new byte[0],
                "/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg", parseToDate("2014-10-10"), "Whiplash", 8.35f));
        movies.add(new Movie(false, new int[]{18, 80, 0, 0}, 278, "en", "The Shawshank Redemption", overviewTopRated2, new byte[0],
                "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", parseToDate("1994-09-10"), "The Shawshank Redemption", 8.29f));
        topRatedMovies = movies;
    }

    private MockRankingService()
    {
    }

    @Override
    public void fetchMovies(int sortOrder, Callback.MoviesLoaded callback)
    {
        switch (sortOrder)
        {
            case SORTORDER_POPULAR:
                callback.onMoviesLoaded(mostPopularMovies, sortOrder, RC_OK_NETWORK);
                break;
            case SORTORDER_TOPRATED:
                callback.onMoviesLoaded(topRatedMovies, sortOrder, RC_OK_NETWORK);
                break;
        }
    }

    public static MockRankingService getInstance()
    {
        if (instance == null)
            instance = new MockRankingService();
        return instance;
    }

    private static Date parseToDate(String string)
    {
        try
        {
            return dateFormat.parse(string);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }


}
