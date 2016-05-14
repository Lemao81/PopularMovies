package com.jueggs.popularmovies.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jueggs.popularmovies.event.NetworkStateChangeEvent;
import com.jueggs.popularmovies.util.NetUtils;
import org.greenrobot.eventbus.EventBus;

public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        EventBus.getDefault().post(new NetworkStateChangeEvent(NetUtils.isNetworkAvailable(context)));
    }
}
