package com.jueggs.popularmovies.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.MovieDbContract;

public class StartupFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        WebView webView = new WebView(getContext());
        webView.loadUrl(MovieDbContract.MOVIEDB_WEBSITE);
        return webView;
    }
}
