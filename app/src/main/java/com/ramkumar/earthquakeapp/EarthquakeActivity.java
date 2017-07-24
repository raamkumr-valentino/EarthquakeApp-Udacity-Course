/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ramkumar.earthquakeapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";

    private List<Earthquake> earthquakeList = new ArrayList<Earthquake>();

    private ListView earthquakeListView;

    private EarthquakeAdapter adapter;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new adapter that takes the list of earthquakes as input
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        adapter = new EarthquakeAdapter(this,earthquakeList);
        earthquakeListView.setAdapter(adapter);
        ConnectivityManager conmanager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conmanager.getActiveNetworkInfo();
        if(info != null && info.isConnected()) {
            loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            LoaderManager manager = getLoaderManager();
            manager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
            Log.d(LOG_TAG,"init");
        } else {
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText("No Internet Connection");
        }
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent intent = new Intent(EarthquakeActivity.this,EarthquakeSetting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG,"onCreateLoader");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minMag =  preferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy = preferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));

        Uri baseuri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseuri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("eventtype","earthquake");
        uriBuilder.appendQueryParameter("orderby", "time");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMag);

        Toast.makeText(getBaseContext(),uriBuilder.toString(),Toast.LENGTH_LONG).show();

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        loadingIndicator.setVisibility(View.GONE);
        adapter.clear();
        if(earthquakes != null &&  !earthquakes.isEmpty()){
            adapter.addAll(earthquakes);
        }
        Log.d(LOG_TAG,"onFinishedLoader");
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();
        Log.d(LOG_TAG,"ondLoaderReset");
    }
}
