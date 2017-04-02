package com.example.runtracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by Yurii on 25.03.2017.
 */

public class PostRequestService extends Service {

    private BroadcastReceiver mLocationReceiver = new LocationReceiver(){
        @Override
        protected void onLocationReceived(Context context, Location loc) {
            mLastLocation = loc;
            new PostSenderTask().execute();
        }
    };

    private Location mLastLocation;
    boolean isRegistered = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!isRegistered){
            this.registerReceiver(mLocationReceiver,
                    new IntentFilter(RunManager.ACTION_LOCATION));
            isRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mLocationReceiver);
        isRegistered = false;
    }

    private class PostSenderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new PostRequest().postSender(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            return null;
        }
    }
}
