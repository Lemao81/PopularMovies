package com.jueggs.popularmovies.data.service;

public final class Injection
{
    public static RankingService injectRankingService()
    {
        return FetchRankingService.getInstance();
    }
}
