package ua.com.webacademy.beginnerslection11;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferencesFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
