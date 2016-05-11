package com.jueggs.popularmovies.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
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
import com.jueggs.popularmovies.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.data.favourites.schematic.FavouriteColumns.*;
import static com.jueggs.popularmovies.data.favourites.schematic.FavouritesProvider.*;
import static com.jueggs.popularmovies.util.Utils.*;


public class RankingFragment extends Fragment
{
    public static final String TAG = RankingFragment.class.getSimpleName();
    public static final String STATE_SORTORDER = "STATE_SORTORDER";

    @Bind(R.id.gridView) GridView gridView;

    private RankingAdapter rankingAdapter;
    private RankingRepository repository;
    private int sortOrder = SORTORDER_INVALID;
    private SparseArray<String> titles = new SparseArray<>(NUM_SORTORDER);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null)
            sortOrder = savedInstanceState.getInt(STATE_SORTORDER);

        titles.put(SORTORDER_POPULAR, getString(R.string.title_popular));
        titles.put(SORTORDER_TOPRATED, getString(R.string.title_toprated));
        titles.put(SORTORDER_FAVOURITE, getString(R.string.title_favourites));

        repository = RankingRepository.getInstance(getActivity().getApplicationContext());
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

        switch (sortOrder)
        {
            case SORTORDER_POPULAR:
            case SORTORDER_TOPRATED:
                repository.loadMovies(sortOrder, moviesLoadedCallback);
                break;
            case SORTORDER_FAVOURITE:
                loadFavourites();
        }

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
                case RC_OK_NETWORK:
                    Toast.makeText(getContext(), "Updated movie list", Toast.LENGTH_SHORT).show();
                case RC_OK_CACHE:
                    updateMovies(movies, sortOrder);
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
        getActivity().setTitle(String.format(getString(R.string.format_title), titles.get(sortOrder)));
        RankingFragment.this.sortOrder = sortOrder;
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
    public void onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.menu_refresh).setEnabled((sortOrder & MASK_SORTORDER_REFRESHABLE) != 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_popular:
                repository.loadMovies(SORTORDER_POPULAR, moviesLoadedCallback);
                break;
            case R.id.menu_toprated:
                repository.loadMovies(SORTORDER_TOPRATED, moviesLoadedCallback);
                break;
            case R.id.menu_refresh:
                repository.clear(sortOrder);
                repository.loadMovies(sortOrder, moviesLoadedCallback);
                break;
            case R.id.menu_favourites:
                loadFavourites();
                break;
        }
        return true;
    }

    private void loadFavourites()
    {
        Cursor cursor = getContext().getContentResolver().query(Favourite.BASE_URI, PROJECTION_COMPLETE, null, null, null);
        updateMovies(transformCursorToMovies(cursor), SORTORDER_FAVOURITE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_SORTORDER, sortOrder);
    }
}
