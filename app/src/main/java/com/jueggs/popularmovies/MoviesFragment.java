package com.jueggs.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jueggs.popularmovies.MovieDbAPI.*;

public class MoviesFragment extends Fragment
{
    private MoviesAdapter moviesAdapter;
    private Cache cache = Cache.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        moviesAdapter = new MoviesAdapter(getContext(), 0, new ArrayList<Movie>());
        gridView.setAdapter(moviesAdapter);

        Date today = new Date();
        if (cache.hasPopularMoviesUpToDate(today))
        {
            moviesAdapter.addAll(cache.getMoviesPopular());
        }
        else if (cache.hasPopularMoviesExpired(today))
        {
            boolean success = tryFetchMovies(SortType.POPULAR);
            moviesAdapter.addAll(cache.getMoviesPopular());
        }

        return view;
    }

    private boolean networkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class FetchMoviesTask extends AsyncTask<SortType, Void, List<Movie>>
    {

        @Override
        protected List<Movie> doInBackground(SortType... params)
        {

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            super.onPostExecute(movies);
        }
    }
}
