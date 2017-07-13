package com.wolff.wnews.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.wolff.wnews.R;

/**
 * Created by wolff on 13.07.2017.
 */

public class Settings_fragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences_general);
    }

    public static Settings_fragment newInstance(){
        return new Settings_fragment();
    }



}
