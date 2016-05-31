package com.jueggs.popularmovies.data;

import com.jueggs.popularmovies.BuildConfig;
import com.jueggs.popularmovies.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService
{
    @GET("movie/popular?api_key=" + BuildConfig.API_KEY)
    Call<Ranking> loadMostPopularMovies();

    @GET("movie/top_rated?api_key=" + BuildConfig.API_KEY)
    Call<Ranking> loadTopRatedMovies();

    @GET("movie/{movieId}/videos?api_key=" + BuildConfig.API_KEY)
    Call<TrailerRoot> loadTrailer(@Path("movieId") int movieId);

    @GET("movie/{movieId}/reviews?api_key=" + BuildConfig.API_KEY)
    Call<ReviewRoot> loadReviews(@Path("movieId") int movieId);

    @GET("authentication/token/new?api_key=" + BuildConfig.API_KEY)
    Call<RequestToken> requestRequestToken();

    @GET("authentication/token/validate_with_login?api_key=" + BuildConfig.API_KEY)
    Call<Authentication> requestAuthentication(
            @Query("request_token") String requestToken, @Query("username") String username, @Query("password") String password);

    @GET("authentication/session/new?api_key=" + BuildConfig.API_KEY)
    Call<Session> requestSession(@Query("request_token") String requestToken);

    @GET("account?api_key=" + BuildConfig.API_KEY)
    Call<Account> getAccount(@Query("session_id") String sessionId);
}
