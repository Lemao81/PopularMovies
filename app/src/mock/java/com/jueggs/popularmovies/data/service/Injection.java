package com.jueggs.popularmovies.data.service;

import com.jueggs.popularmovies.data.service.RankingService;

public final class Injection
{
    public static RankingService injectRankingService()
    {
        return MockRankingService.getInstance();
    }
}
