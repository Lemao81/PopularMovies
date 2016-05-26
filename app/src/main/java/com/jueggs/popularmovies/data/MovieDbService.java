package com.jueggs.popularmovies.data;

import com.jueggs.popularmovies.BuildConfig;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Ranking;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;

public interface MovieDbService
{
    @GET("movie/popular?api_key=" + BuildConfig.API_KEY)
    Call<Ranking> loadMostPopularMovies();


}
