package com.example.runtracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

public class RunActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PagerAdapterFragment();
    }

    public void setActionBarTitle(String title) {
        getActionBar().setTitle(title);
    }
}

