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
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.Injection;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.favourites.FavouriteColumns;
import com.jueggs.popularmovies.data.favourites.FavouritesProvider;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.util.UIUtils;

import static com.jueggs.popularmovies.ui.detail.Callback.*;
import static com.jueggs.popularmovies.util.UIUtils.*;

public class FavouriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Callback.MovieSelected
{
    public static final int LOADER_ID = 0;
    public static final String STATE_SELECTEDPOSITION = "selectedposition";

    @Bind(R.id.recycler) RecyclerView recycler;
    @Bind(R.id.coverLoading) FrameLayout coverLoading;

    private FavouriteAdapter adapter;
    private boolean startup;
    private int selectedPosition = FavouriteAdapter.NO_SELECTION;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startup = savedInstanceState == null;

        if (savedInstanceState != null)
            selectedPosition = savedInstanceState.getInt(STATE_SELECTEDPOSITION);
        else if (App.getInstance().isTwoPane())
            selectedPosition = 0;
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
        recycler.setAdapter(adapter = new FavouriteAdapter(getContext(), this, crudStartedCallback, crudCompletedCallback,
                selectedPosition, recycler));
//        if (selectedPosition != FavouriteAdapter.NO_SELECTION)
//            recycler.smoothScrollToPosition(selectedPosition);
        new ItemTouchHelper(new ItemTouchHelperCallback(adapter)).attachToRecyclerView(recycler);

        return view;
    }

    private FavouriteCRUDstarted crudStartedCallback = new FavouriteCRUDstarted()
    {
        @Override
        public void onFavouriteCRUDstarted()
        {
            showLoading(true, coverLoading);
        }
    };

    private FavouriteCRUDcompleted crudCompletedCallback = new FavouriteCRUDcompleted()
    {
        @Override
        public void onFavouriteCRUDcompleted(int result,CRUD operation)
        {
            showLoading(false, coverLoading);
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return Injection.injectCursorLoader(getContext());
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
        //TODO check if this is ok (loadfinished called for every configuration change?)
        if (selectedPosition != FavouriteAdapter.NO_SELECTION)
            recycler.smoothScrollToPosition(selectedPosition);
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
