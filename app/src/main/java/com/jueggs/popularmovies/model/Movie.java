package com.jueggs.popularmovies.model;

import java.util.Date;

public class Movie
{
    private String title;
    private Date releaseDate;
    private String posterPath;
    private float voteAverage;
    private String overview;

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath.substring(1);
    }

    public Date getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public float getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage)
    {
        this.voteAverage = voteAverage;
    }
}
