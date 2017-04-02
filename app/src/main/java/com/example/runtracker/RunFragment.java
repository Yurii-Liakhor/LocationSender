package com.example.runtracker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Service;

/**
 * Created by Yurii on 09.10.2016.
 */

public class RunFragment extends Fragment {
    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
        @Override
        protected void onLocationReceived(Context context, Location loc) {
            mLastLocation = loc;
            if (isVisible())
                updateUI();
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        }
    };

    private static final String STATE_LOCATION = "location";

    private RunManager mRunManager;
    private Location mLastLocation;

    private Button mStartButton, mStopButton;
    private TextView mLatitudeTextView, mLongitudeTextView, mAltitudeTextView;

    boolean isRegistered = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(STATE_LOCATION);
        }
        //setRetainInstance(true);
        if(!isRegistered) {
            mRunManager = RunManager.get(getActivity());
            getActivity().registerReceiver(mLocationReceiver,
                    new IntentFilter(RunManager.ACTION_LOCATION));
            getActivity().startService(
                    new Intent(getActivity(), PostRequestService.class));
            isRegistered = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_LOCATION, mLastLocation);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);

        mLatitudeTextView = (TextView)view.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView)view.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView)view.findViewById(R.id.run_altitudeTextView);
        mStartButton = (Button)view.findViewById(R.id.run_startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRunManager.startLocationUpdates();
                updateUI();
            }
        });
        mStopButton = (Button)view.findViewById(R.id.run_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRunManager.stopLocationUpdates();
                updateUI();
            }
        });
        updateUI();

        return view;
    }

    @Override
    public void onDestroy() {
    super.onDestroy();
    if(isRegistered) {
        getActivity().unregisterReceiver(mLocationReceiver);
        getActivity().stopService(new Intent(getActivity(), PostRequestService.class));
        isRegistered = false;
    }
}
    private void updateUI() {
        boolean started = mRunManager.isTrackingRun();
        if(mLastLocation != null) {
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));

            mStartButton.setEnabled(!started);
            mStopButton.setEnabled(started);
        }
    }
    /*private class PostSenderTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            new PostRequest().postSender(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            return null;
        }
    }*/
}