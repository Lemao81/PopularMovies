package com.jueggs.popularmovies.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareLayout extends RelativeLayout
{
    public static final float RATIO = 0.75f;

    public SquareLayout(Context context)
    {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int newHeight = (int) (height / RATIO);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}
