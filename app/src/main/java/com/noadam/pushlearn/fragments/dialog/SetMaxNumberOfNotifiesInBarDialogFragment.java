package com.noadam.pushlearn.fragments.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.noadam.pushlearn.R;

public class SetMaxNumberOfNotifiesInBarDialogFragment extends DialogFragment {

    public static SetMaxNumberOfNotifiesInBarDialogFragment newInstance(int value){
        SetMaxNumberOfNotifiesInBarDialogFragment dialogFragment = new SetMaxNumberOfNotifiesInBarDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_max_number_of_notifies_in_bar, null);
        SeekBar seekBar = (SeekBar)view.findViewById(R.id.dialog_number_of_notifies_in_bar_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //  Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             //   getDialog().setTitle(String.valueOf(progress));
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            int value = bundle.getInt("value",3);
            seekBar.setProgress(value);
            seekBar.refreshDrawableState();
        }

        builder
                .setTitle(String.valueOf(seekBar.getProgress()))
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("number_of_notifies_in_bar", seekBar.getProgress());
                                editor.apply();
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                    }
                });

        return builder.create();
    }
}
