package com.jueggs.popularmovies.ui.mainview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.GridView;
import android.widget.Toast;
import com.jueggs.popularmovies.R;
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
    private int sortOrder = SORTORDER_INVALID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

    private Callback moviesLoadedCallback = new Callback()
    {
        @Override
        public void onMoviesLoaded(int sortOrder, int resultCode, List<Movie> movies)
        {
            switch (resultCode)
            {
                case RC_OK_CACHE:
                    updateMovies(movies, sortOrder);
                    break;
                case RC_OK_NETWORK:
                    updateMovies(movies, sortOrder);
                    Toast.makeText(getContext(), "Updated movie list", Toast.LENGTH_LONG).show();
                    break;
                case RC_NO_NETWORK:
                    handleFailedMovieUpdate("No network available :(");
                    break;
                case RC_ERROR:
                    handleFailedMovieUpdate("An inexplicable error occurred during network access");
                    break;
                default:
                    Log.e(TAG, "unknown result code");
            }
        }
    };

    private void updateMovies(List<Movie> movies, int sortOrder)
    {
        moviesAdapter.clear();
        moviesAdapter.addAll(movies);
        setTitle(sortOrder);
        MoviesFragment.this.sortOrder = sortOrder;
    }

    private void setTitle(int sortOrder)
    {
        String title = getString(sortOrder == SORTORDER_POPULAR ? R.string.title_popular : R.string.title_toprated);
        getActivity().setTitle(String.format(getString(R.string.format_title), title));
    }

    private void handleFailedMovieUpdate(String msg)
    {
        MoviesFragment.this.sortOrder = SORTORDER_INVALID;
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuPopular:
                repository.loadMovies(SORTORDER_POPULAR, moviesLoadedCallback);
                break;
            case R.id.menuToprated:
                repository.loadMovies(SORTORDER_TOPRATED, moviesLoadedCallback);
                break;
            case R.id.menuRefresh:
                if ((this.sortOrder & MASK_SORTORDER_VALID) != 0)
                {
                    repository.clear(this.sortOrder);
                    repository.loadMovies(this.sortOrder, moviesLoadedCallback);
                }
                break;
        }
        return true;
    }
}
