package com.jueggs.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import com.jueggs.popularmovies.data.CachedRepository;
import com.jueggs.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieContract.*;


public class MoviesFragment extends Fragment
{
    public static final String TAG = MoviesFragment.class.getSimpleName();

    private MoviesAdapter moviesAdapter;
    private CachedRepository repository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        moviesAdapter = new MoviesAdapter(getContext(), 0, new ArrayList<Movie>());
        gridView.setAdapter(moviesAdapter);

        repository = CachedRepository.getInstance(getActivity().getApplicationContext());
        repository.loadMovies(SORTORDER_POPULAR, moviesLoadedCallback);

        return view;
    }

    private Callback moviesLoadedCallback=new Callback()
    {
        @Override
        public void onMoviesLoaded(int sortorder, int resultCode, List<Movie> movies)
        {
            switch (resultCode)
            {
                case RC_OK_CACHE:
                    moviesAdapter.clear();
                    moviesAdapter.addAll(movies);
                    break;
                case RC_OK_NETWORK:
                    moviesAdapter.clear();
                    moviesAdapter.addAll(movies);
                    Toast.makeText(getContext(), "Updated movie list", Toast.LENGTH_LONG).show();
                    break;
                case RC_NO_NETWORK:
                    Toast.makeText(getContext(), "No network available :(", Toast.LENGTH_LONG).show();
                    break;
                case RC_ERROR:
                    Toast.makeText(getContext(), "An inexplicable error occurred during network access", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.e(TAG, "unknown result code");
            }
        }
    };

}
