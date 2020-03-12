package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;
import com.noadam.pushlearn.fragments.dialog.settings.SetMaxNumberOfNotifiesInBarDialogFragment;
import com.noadam.pushlearn.fragments.dialog.settings.SetTimePeriodDialogFragment;


public class SettingsFragment extends PreferenceFragment {

    Preference prefHowManyNotifiesUWantToSee;
    Preference prefTimePeriodBetweenNotifies;
    Preference prefLogOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Context context = getActivity();
       // PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        // Load the preferences from an XML resource

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        int count_notifies_in_bar = prefs.getInt("number_of_notifies_in_bar",3);
        prefHowManyNotifiesUWantToSee = findPreference("pref_number_of_notifies_in_bar");
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
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        int minutesFull = prefs.getInt("minutesBetweenNotifies",1);
        int hours = Math.round(minutesFull / 60);
        int minutes = minutesFull % 60;
        prefTimePeriodBetweenNotifies = findPreference("pref_time_period_between_notifies");
        prefTimePeriodBetweenNotifies.setSummary(String.valueOf(hours)+":"+String.format("%02d", minutes));
        prefTimePeriodBetweenNotifies.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SetTimePeriodDialogFragment dialogFrag = SetTimePeriodDialogFragment.newInstance(minutesFull);
                dialogFrag.setTargetFragment(getTargetFragment(), 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                return true;
            }
        });
        //--------------------------------------------------------------------------------------------------------------------------------------------------------
        prefLogOut = findPreference("pref_log_out");
        prefLogOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DeleteConfirmationDialogFragment dialogFrag = new DeleteConfirmationDialogFragment();
                dialogFrag.setTargetFragment(getTargetFragment(), 666);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                return true;
            }
        });
    }

}
