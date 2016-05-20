package com.jueggs.popularmovies.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

public class UIUtils
{
    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;

    public static void showLoading(boolean show, View coverLoading)
    {
        coverLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public static int percentOfScreen(int layout,float fraction, Resources resources)
    {
        DisplayMetrics dm = resources.getDisplayMetrics();
        int result = -1;
        switch (layout)
        {
            case WIDTH:
                result = (int) (dm.widthPixels * fraction);
                break;
            case HEIGHT:
                result = (int) (dm.heightPixels * fraction);
                break;
        }
        return result;
    }
}
