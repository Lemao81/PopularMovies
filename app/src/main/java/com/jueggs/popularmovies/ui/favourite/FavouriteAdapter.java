package com.jueggs.popularmovies.ui.favourite;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.App;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.event.FavouriteDeletedEvent;
import com.jueggs.popularmovies.model.Movie;
import com.jueggs.popularmovies.ui.detail.DeleteFavouriteTask;
import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_92;
import static com.jueggs.popularmovies.data.favourites.FavouriteColumns.ProjectionCompleteIndices.*;
import static com.jueggs.popularmovies.data.favourites.FavouritesProvider.*;
import static com.jueggs.popularmovies.ui.detail.Callback.*;
import static com.jueggs.popularmovies.util.Utils.*;

public class FavouriteAdapter extends CursorRecyclerViewAdapter<FavouriteAdapter.ViewHolder> implements Callback.MovieSwiped
{
    public static final int NO_SELECTION = -1;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    private Callback.MovieSelected movieSelectedCallback;
    private FavouriteCRUDstarted crudStartedCallback;
    private FavouriteCRUDcompleted crudCompletedCallbackExternal;
    private ViewGroup previousSelection;
    private int selectedPosition;
    private int swipedPosition;
    private RecyclerView recycler;

    public FavouriteAdapter(Context context, Callback.MovieSelected movieSelectedCallback, FavouriteCRUDstarted crudStartedCallback,
                            FavouriteCRUDcompleted crudCompletedCallback, int selectedPosition, RecyclerView recycler)
    {
        super(context, null);
        this.movieSelectedCallback = movieSelectedCallback;
        this.selectedPosition = selectedPosition;
        this.recycler = recycler;
        this.crudStartedCallback = crudStartedCallback;
        this.crudCompletedCallbackExternal = crudCompletedCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor)
    {
        holder.title.setText(cursor.getString(TITLE));
        holder.genre.setText(createGenreString(decodeGenreIds(cursor.getLong(GENRE_IDS))));
        holder.release_date.setText(dateFormat.format(new Date(cursor.getLong(REL_DATE))));
        holder.vote_average.setText(String.format(context.getString(R.string.format_vote_average_short), cursor.getFloat(VOTE_AVERAGE)));
        holder.itemView.setOnClickListener(holder);

        if (selectedPosition != NO_SELECTION && selectedPosition == holder.getAdapterPosition())
            setSelectedMovie(holder.container);

        byte[] posterBytes = cursor.getBlob(POSTER);
        if (hasElements(posterBytes))
            holder.thumbnail.setImageDrawable(convertByteArrayToDrawable(context.getResources(), posterBytes));
        else
            loadImage(context, IMG_WIDTH_92, cursor.getString(POSTER_PATH), holder.thumbnail);
    }

    //TODO fix buggy selection
    public void setSelectedMovie(ViewGroup container)
    {
        container.setBackgroundColor(ContextCompat.getColor(context, R.color.scrim_item_selection));
        if (previousSelection != null)
            previousSelection.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        previousSelection = container;
    }

    @Override
    public void onMovieSwiped(int position)
    {
        swipedPosition = position;

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int movieId = cursor.getInt(MOVIE_ID);

        new DeleteFavouriteTask(crudStartedCallback, crudCompletedCallbackInternal, context.getContentResolver())
                .execute(Favourite.withMovieId(movieId));

        if (App.getInstance().isTwoPane())
            EventBus.getDefault().post(new FavouriteDeletedEvent(movieId));
    }

    private FavouriteCRUDcompleted crudCompletedCallbackInternal = new FavouriteCRUDcompleted()
    {
        @Override
        public void onFavouriteCRUDcompleted(int result, CRUD operation)
        {
            int stringId;
            if (result > 0)
            {
                stringId = R.string.favourites_removed_msg;
                if (selectedPosition > swipedPosition)
                    selectedPosition--;
            }
            else
            {
                stringId = R.string.favourites_removed_error;
            }

            Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();

            crudCompletedCallbackExternal.onFavouriteCRUDcompleted(result, operation);
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @Bind(R.id.container) ViewGroup container;
        @Bind(R.id.title) TextView title;
        @Bind(R.id.genre) TextView genre;
        @Bind(R.id.vote_average) TextView vote_average;
        @Bind(R.id.release_date) TextView release_date;
        @Bind(R.id.thumbnail) ImageView thumbnail;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v)
        {
            getCursor().moveToPosition(getAdapterPosition());
            Movie movie = transformCurrentCursorPositionToMovie(getCursor());

            if (previousSelection != container)
            {
                selectedPosition = getAdapterPosition();
                setSelectedMovie(container);
            }

            movieSelectedCallback.onMovieSelected(movie, getAdapterPosition());
        }
    }


}
