package com.dmanlancers.safesporttracker.GpsServices;




import com.dmanlancers.safesporttracker.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

public class DefGps extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		//enableDisableGps

		Preference enableDisablePref = (Preference) findPreference("enableDisableGps");
		enableDisablePref.setOnPreferenceClickListener(new OnPreferenceClickListener() 
		{

			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
				return true;
			}

		}
		);
	}

}
