package com.jueggs.popularmovies;

import com.jueggs.popularmovies.data.service.MockRankingService;
import com.jueggs.popularmovies.data.service.RankingService;

public final class Injection
{
    public static RankingService injectRankingService()
    {
        return MockRankingService.getInstance();
    }
}
