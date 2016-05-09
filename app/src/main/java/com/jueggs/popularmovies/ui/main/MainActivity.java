package com.jueggs.popularmovies.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jueggs.popularmovies.R;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.title));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new RankingFragment()).commit();
        }
    }
}
