package com.jueggs.popularmovies.event;

public class NetworkStateChangeEvent
{
    public boolean connected;

    public NetworkStateChangeEvent(boolean connected)
    {
        this.connected = connected;
    }
}
