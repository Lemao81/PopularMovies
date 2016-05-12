package com.jueggs.popularmovies.ui.favourite;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback
{
    Callback.MovieSwiped callback;

    public ItemTouchHelperCallback(Callback.MovieSwiped callback)
    {
        this.callback = callback;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        return makeFlag(ACTION_STATE_SWIPE, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        callback.onMovieSwiped(viewHolder.getAdapterPosition());
    }

}
