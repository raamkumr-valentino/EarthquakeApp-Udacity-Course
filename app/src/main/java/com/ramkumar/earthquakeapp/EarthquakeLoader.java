package com.ramkumar.earthquakeapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Ramkumar on 7/14/2017.
 */

class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;

    EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG,"onStartLoading");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.d(LOG_TAG,"onloadBackgroung");
        if(mUrl == null)
            return null;
        return QueryUtils.extractJsonResponse(mUrl);
    }
}
