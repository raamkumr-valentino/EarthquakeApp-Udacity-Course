package com.ramkumar.earthquakeapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

public class EarthquakeSetting extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_setting);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        private void bindPreferenceSummaryToValue(Preference minMagnitude) {
            minMagnitude.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(minMagnitude.getContext());
            String preferenceString = preferences.getString(minMagnitude.getKey(), "");
            onPreferenceChange(minMagnitude, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String value = o.toString();
            if(preference instanceof ListPreference) {
                ListPreference list = (ListPreference)preference;
                int preIndex = list.findIndexOfValue(value);
                if(preIndex >=0 ){
                    CharSequence[] labels = list.getEntries();
                    preference.setSummary(labels[preIndex]);
                }
            } else {
                preference.setSummary(value);
            }
            return true;
        }
    }

}
