package com.jueggs.popularmovies.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.util.UIUtils;

import static com.jueggs.popularmovies.util.UIUtils.*;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setLayout(percentOfScreen(WIDTH, 0.7f, getResources()), ViewGroup.LayoutParams.WRAP_CONTENT);

        setTitle(R.string.title_login);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
    }
}
