package com.noadam.pushlearn.fragments.dialog.settings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.activities.SettingsActivity;
import com.noadam.pushlearn.push.MyReceiver;

import java.util.Calendar;

import static java.lang.Thread.sleep;


public class SetTimePeriodDialogFragment extends DialogFragment {

    NumberPicker timePickerMinutes;
    NumberPicker timePickerHours;

    public static SetTimePeriodDialogFragment newInstance(int value) {
        final SetTimePeriodDialogFragment
                fragment = new SetTimePeriodDialogFragment();
        final Bundle b = new Bundle(1);
        b.putInt("minutes", value);
        fragment.setArguments(b);
        return fragment;
    }
    // ------ ONLY COPY-PASTE
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_time_picker, null);
        timePickerMinutes = view.findViewById(R.id.minutes_NumberPicker);
        timePickerMinutes.setMaxValue(59);
        timePickerMinutes.setMinValue(1);
        timePickerMinutes.setWrapSelectorWheel(false);
        timePickerHours = view.findViewById(R.id.hours_NumberPicker);
        timePickerHours.setMaxValue(1);
        timePickerHours.setMinValue(0);
        timePickerHours.setWrapSelectorWheel(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int minutes = bundle.getInt("minutes");
            timePickerHours.setValue(Math.round(minutes / 60));
            timePickerMinutes.setValue(minutes % 60);
        }
        TypedValue tV = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.blackcolor, tV, true);
        SpannableString s = new SpannableString(getString(R.string.pref_time_period_between_notifies));
        s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
        builder.setView(view)
                .setTitle(s)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = getActivity().getIntent();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = prefs.edit();
                                int hours = timePickerHours.getValue();
                                int minutes = timePickerMinutes.getValue();
                                editor.putInt("minutesBetweenNotifies", hours * 60 + minutes);
                                editor.apply();
                                startActivityForResult(new SettingsActivity().createIntent(getActivity()), SettingsActivity.TIME_BETWEEN_NOTIFIES_DIALOG_RESULT);
                                getActivity().finish();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        AlertDialog alert = builder.create();
        theme.resolveAttribute(R.attr.focusColor, tV, true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(tV.data));
        theme.resolveAttribute(R.attr.actionColorDark, tV, true);
        alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(tV.data);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(tV.data);
        return alert;
    }
}

