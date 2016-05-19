package com.jueggs.popularmovies.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jueggs.popularmovies.R;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
    }
}
