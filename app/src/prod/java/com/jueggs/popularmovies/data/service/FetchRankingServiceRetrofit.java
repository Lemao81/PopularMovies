package com.jueggs.popularmovies.data.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jueggs.popularmovies.data.MovieDbService;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.model.Ranking;
import com.jueggs.popularmovies.ui.main.Callback;
import com.jueggs.popularmovies.util.Utils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

import static android.text.TextUtils.*;
import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.ParseUtils.*;
import static com.jueggs.popularmovies.util.NetUtils.*;

public class FetchRankingServiceRetrofit implements RankingService
{
    public static final String TAG = FetchRankingServiceRetrofit.class.getSimpleName();

    private static FetchRankingServiceRetrofit instance;

    private Callback.MoviesLoaded callback;
    private int sortOrder;

    private FetchRankingServiceRetrofit()
    {
    }

    public void fetchMovies(int sortOrder, Callback.MoviesLoaded callback)
    {
        this.callback = callback;
        this.sortOrder = sortOrder;
        new FetchAsyncTask().execute(sortOrder);
    }

    class FetchAsyncTask extends AsyncTask<Integer, Void, List<Movie>>
    {
        @Override
        protected List<Movie> doInBackground(Integer... params)
        {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URI_STRING)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
            MovieDbService service = retrofit.create(MovieDbService.class);
            try
            {
                Ranking ranking = service.loadMostPopularMovies().execute().body();
                return ranking.getResults();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            if (callback != null)
                if (movies != null)
                    callback.onMoviesLoaded(movies, sortOrder, RC_OK_NETWORK);
                else
                {
                    Log.e(TAG, "something went wrong during fetching, returned null");
                    callback.onMoviesLoaded(null, sortOrder, RC_ERROR);
                }
        }
    }

    public static FetchRankingServiceRetrofit getInstance()
    {
        if (instance == null)
            instance = new FetchRankingServiceRetrofit();
        return instance;
    }
}
