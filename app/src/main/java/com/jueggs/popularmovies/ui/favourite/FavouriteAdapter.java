package com.jueggs.popularmovies.ui.favourite;

import android.content.Context;
import android.database.Cursor;
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
import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jueggs.popularmovies.data.MovieDbContract.IMG_WIDTH_92;
import static com.jueggs.popularmovies.data.favourites.FavouriteColumns.ProjectionCompleteIndices.*;
import static com.jueggs.popularmovies.data.favourites.FavouritesProvider.*;
import static com.jueggs.popularmovies.util.Utils.*;

public class FavouriteAdapter extends CursorRecyclerViewAdapter<FavouriteAdapter.ViewHolder> implements Callback.MovieSwiped
{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    private Callback.MovieSelected callback;

    public FavouriteAdapter(Context context, Cursor cursor, Callback.MovieSelected callback)
    {
        super(context, cursor);
        this.callback = callback;
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

        loadImage(context, IMG_WIDTH_92, cursor.getString(POSTER_PATH), holder.thumbnail);
    }

    @Override
    public void onMovieSwiped(int position)
    {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int movieId = cursor.getInt(MOVIE_ID);
        context.getContentResolver().delete(Favourite.withMovieId(movieId), null, null);
        Toast.makeText(context, R.string.favourites_removed_msg, Toast.LENGTH_LONG).show();

        if (App.getInstance().isTwoPane())
            EventBus.getDefault().post(new FavouriteDeletedEvent(movieId));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
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

            callback.onMovieSelected(movie);
        }
    }


}
