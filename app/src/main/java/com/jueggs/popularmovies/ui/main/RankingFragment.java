package com.jueggs.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.repo.RankingRepository;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;


public class RankingFragment extends Fragment
{
    public static final String TAG = RankingFragment.class.getSimpleName();
    public static final String STATE_SORTORDER = "STATE_SORTORDER";

    @Bind(R.id.gridView) GridView gridView;

    private RankingAdapter rankingAdapter;
    private RankingRepository repository;
    private int sortOrder = SORTORDER_INVALID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null)
        {
            sortOrder = savedInstanceState.getInt(STATE_SORTORDER);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        rankingAdapter = new RankingAdapter(getContext(), 0, new ArrayList<Movie>());
        gridView.setAdapter(rankingAdapter);
        gridView.setOnItemClickListener(posterClickListener);

        repository = RankingRepository.getInstance(getActivity().getApplicationContext());
        repository.loadMovies(sortOrder != SORTORDER_INVALID ? sortOrder : SORTORDER_POPULAR, moviesLoadedCallback);

        return view;
    }

    private AdapterView.OnItemClickListener posterClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Movie movie = rankingAdapter.getItem(position);

            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    };

    private MovieLoadedCallback moviesLoadedCallback = new MovieLoadedCallback()
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
        rankingAdapter.clear();
        rankingAdapter.addAll(movies);
        setTitle(sortOrder);
        RankingFragment.this.sortOrder = sortOrder;
    }

    private void setTitle(int sortOrder)
    {
        String title = getString(sortOrder == SORTORDER_POPULAR ? R.string.title_popular : R.string.title_toprated);
        getActivity().setTitle(String.format(getString(R.string.format_title), title));
    }

    private void handleFailedMovieUpdate(String msg)
    {
        RankingFragment.this.sortOrder = SORTORDER_INVALID;
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

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_SORTORDER, sortOrder);
    }
}
