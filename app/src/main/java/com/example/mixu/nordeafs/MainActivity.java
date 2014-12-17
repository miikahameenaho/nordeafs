/*
 * Copyright (c) 2014. Miika Hämeenaho - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Miika Hämeenaho <miika.hameenaho@ovi.com>
 */

package com.example.mixu.nordeafs;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

public class MainActivity extends ActionBarActivity implements VenueListFragment.OnFragmentInteractionListener, InputTxtFragment.OnSearchQueryChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationTracker location = LocationTracker.getInstance(this);
        location.stopUsingGPS();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TODO commented out currently since there is no need for settings in application
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
        // TODO Not currently needed in VenueListFragment
    }

    @Override
    public void onSearchQueryChanged(String search_query) {
        Log.d("mainActivity", "onSearchQueryChanged:" +search_query );

        VenueListFragment venue_list_fragment = (VenueListFragment)
                getFragmentManager().findFragmentById(R.id.venue_list_fragment);

        if (venue_list_fragment != null) {
            venue_list_fragment.execute(search_query);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
