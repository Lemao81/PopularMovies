package com.jueggs.popularmovies.util;

import android.view.View;

public class UIUtils
{
    public static void showLoading(boolean show, View coverLoading)
    {
        coverLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
