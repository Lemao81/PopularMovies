package com.jueggs.popularmovies;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class CustomApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
    }
}
