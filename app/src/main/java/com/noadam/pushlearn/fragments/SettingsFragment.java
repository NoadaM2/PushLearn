package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.fragments.dialog.CreatePackDialogFragment;
import com.noadam.pushlearn.fragments.dialog.SetMaxNumberOfNotifiesInBarDialogFragment;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Context context = getActivity();
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        // Load the preferences from an XML resource

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
       int count_notifies_in_bar = prefs.getInt("number_of_notifies_in_bar",3);
        Preference prefHowManyNotifiesUWantToSee = findPreference("pref_number_of_notifies_in_bar");

        prefHowManyNotifiesUWantToSee.setSummary(String.valueOf(count_notifies_in_bar));
        prefHowManyNotifiesUWantToSee.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SetMaxNumberOfNotifiesInBarDialogFragment dialogFrag = SetMaxNumberOfNotifiesInBarDialogFragment.newInstance(count_notifies_in_bar);
                dialogFrag.setTargetFragment(getTargetFragment(), 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                return true;
            }
        });
    }

   /* @Override
    public void onDisplayPreferenceDialog(Preference preference)
    {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference)
        {
            dialogFragment = new TimePreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null)
        {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        }
        else
        {
            super.onDisplayPreferenceDialog(preference);
        }
    }*/
}
