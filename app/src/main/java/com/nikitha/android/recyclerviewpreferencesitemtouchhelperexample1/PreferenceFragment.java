package com.nikitha.android.recyclerviewpreferencesitemtouchhelperexample1;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.prefs.PreferenceChangeListener;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    // register and unregister OnSharedPreferenceChangeListener and also dont forget to add preference style to style.xml
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for(int i=0;i<count;i++){
            Preference p = prefScreen.getPreference(i);
            String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
        }
    }

    private void setPreferenceSummary(Preference preference, String value) {
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(value);
        if(index>=0){
            listPreference.setSummary(listPreference.getEntries()[index]);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null != preference) {
            // Updates the summary for the preference
            if ((preference instanceof ListPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }
}