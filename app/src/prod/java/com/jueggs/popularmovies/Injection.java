package com.jueggs.popularmovies;

import com.jueggs.popularmovies.data.service.FetchRankingService;
import com.jueggs.popularmovies.data.service.RankingService;

public final class Injection
{
    public static RankingService injectRankingService()
    {
        return FetchRankingService.getInstance();
    }
}
