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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.noadam.pushlearn.R;
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
        timePickerMinutes.setMaxValue(60);
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
        builder.setView(view)
                .setTitle(R.string.pref_time_period_between_notifies)
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
                                sendNotify(hours * 60 + minutes);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("fragment","settings");
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        return alert;
    }
    private void sendNotify(int minutes) {
            Intent alarmIntent = new Intent(getActivity(), MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            /*calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);*/
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * minutes , pendingIntent);

    }
}

