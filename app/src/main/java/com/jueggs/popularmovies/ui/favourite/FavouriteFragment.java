package com.jueggs.popularmovies.ui.favourite;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.model.Movie;

public class FavouriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Callback.MovieSelected
{
    public static final int LOADER_ID = 0;
    public static final String STATE_SELECTEDPOSITION = "selectedposition";

    @Bind(R.id.recycler) RecyclerView recycler;

    private FavouriteAdapter adapter;
    private boolean startup;
    private int selectedPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startup = savedInstanceState == null;
        if (savedInstanceState != null)
            selectedPosition = savedInstanceState.getInt(STATE_SELECTEDPOSITION);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter = new FavouriteAdapter(getContext(), this, selectedPosition, recycler));
        new ItemTouchHelper(new ItemTouchHelperCallback(adapter)).attachToRecyclerView(recycler);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(getContext(), FavouritesProvider.Favourite.BASE_URI, FavouriteColumns.PROJECTION_COMPLETE, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data)
    {
        adapter.swapCursor(data);
        if (startup && App.getInstance().isTwoPane())
        {
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    ((Callback.MoviesLoaded) getActivity()).onMoviesLoaded(data);
                }
            });
        }
    }

    @Override
    public void onMovieSelected(Movie movie, int position)
    {
        ((Callback.MovieSelected) getActivity()).onMovieSelected(movie, position);
        selectedPosition = position;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_SELECTEDPOSITION, selectedPosition);
    }
}
