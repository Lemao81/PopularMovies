package com.jueggs.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.*;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.repo.RankingRepository;
import com.jueggs.popularmovies.event.NetworkStateChangeEvent;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.favourite.FavouriteActivity;
import com.jueggs.popularmovies.util.NetUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.NetUtils.*;


public class RankingFragment extends Fragment
{
    public static final String TAG = RankingFragment.class.getSimpleName();
    public static final String STATE_SORTORDER = "sortorder";
    public static final String STATE_SELECTEDPOSITION = "selectedposition";

    @Bind(R.id.gridView) GridView gridView;
    @Bind(R.id.coverNoNetwork) FrameLayout coverNoNetwork;

    private RankingAdapter rankingAdapter;
    private RankingRepository repository;
    private int sortOrder = SORTORDER_POPULAR;
    private int selectedPosition = 0;
    private boolean startup;
    private boolean connected;
    private SparseArray<String> titles = new SparseArray<>(NUM_SORTORDER);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        titles.put(SORTORDER_POPULAR, getString(R.string.title_popular));
        titles.put(SORTORDER_TOPRATED, getString(R.string.title_toprated));

        connected = isNetworkAvailable(getContext());
        if (!connected)
            enableNetworkChangeReceiver(getContext(), true);

        startup = savedInstanceState == null;

        if (savedInstanceState != null)
        {
            sortOrder = savedInstanceState.getInt(STATE_SORTORDER);
            selectedPosition = savedInstanceState.getInt(STATE_SELECTEDPOSITION);
        }

        repository = RankingRepository.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ButterKnife.bind(this, view);

        if (!connected)
            coverNoNetwork.setVisibility(View.VISIBLE);

        rankingAdapter = new RankingAdapter(getContext(), new ArrayList<Movie>());
        gridView.setAdapter(rankingAdapter);
        gridView.setOnItemClickListener(posterClickListener);

        return view;
    }

    private AdapterView.OnItemClickListener posterClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Movie movie = rankingAdapter.getItem(position);
            ((Callback.MovieSelected) getActivity()).onMovieSelected(movie);
            selectedPosition = position;
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (connected)
            repository.loadMovies(sortOrder, moviesLoadedCallback);
    }

    private Callback.MoviesLoaded moviesLoadedCallback = new Callback.MoviesLoaded()
    {
        @Override
        public void onMoviesLoaded(List<Movie> movies, int sortOrder, int resultCode)
        {
            switch (resultCode)
            {
                case RC_OK_NETWORK:
                    Toast.makeText(getContext(), R.string.msg_moviesupdated, Toast.LENGTH_SHORT).show();
                case RC_OK_CACHE:
                    updateMovies(movies, sortOrder);
                    break;
                case RC_NO_NETWORK:
                    handleFailedMovieUpdate(R.string.msg_nonetwork);
                    break;
                case RC_ERROR:
                    handleFailedMovieUpdate(R.string.msg_error);
                    break;
                default:
                    Log.e(TAG, "unknown result code");
            }
            if (startup && App.getInstance().isTwoPane())
                ((Callback.MoviesLoaded) getActivity()).onMoviesLoaded(movies, sortOrder, resultCode);
        }
    };

    private void updateMovies(List<Movie> movies, int sortOrder)
    {
        rankingAdapter.clear();
        rankingAdapter.addAll(movies);
        setTitle(sortOrder);
        selectMovie(selectedPosition);
        RankingFragment.this.sortOrder = sortOrder;
    }

    private void handleFailedMovieUpdate(int stringId)
    {
        RankingFragment.this.sortOrder = SORTORDER_INVALID;
        Toast.makeText(getContext(), stringId, Toast.LENGTH_LONG).show();
    }

    private void setTitle(int sortOrder)
    {
        String title;
        if (sortOrder == SORTORDER_INVALID)
            title = getString(R.string.title);
        else
            title = String.format(getString(R.string.format_title), titles.get(sortOrder));
        getActivity().setTitle(title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setTitle(sortOrder);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onNetworkStateChange(NetworkStateChangeEvent event)
    {
        if (event.connected)
        {
            enableNetworkChangeReceiver(getContext(), false);
            coverNoNetwork.setVisibility(View.GONE);
            repository.loadMovies(sortOrder, moviesLoadedCallback);
        }
    }

    public void selectMovie(int position)
    {
        gridView.smoothScrollToPosition(position);
        gridView.setItemChecked(position, true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.menu_refresh).setEnabled(connected);
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
                startActivity(new Intent(getContext(), FavouriteActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_SORTORDER, sortOrder);
        outState.putInt(STATE_SELECTEDPOSITION, selectedPosition);
    }
}
