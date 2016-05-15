package com.jueggs.popularmovies.ui.favourite;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public class MockCursorLoader extends AsyncTaskLoader<Cursor>
{

    public MockCursorLoader(Context context)
    {
        super(context);
    }

    @Override
    public Cursor loadInBackground()
    {
        return null;
    }
}
