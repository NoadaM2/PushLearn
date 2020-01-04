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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;

public class SetIterationTimesDialogFragment extends DialogFragment {

    public static SetIterationTimesDialogFragment newInstance(int value){
        SetIterationTimesDialogFragment dialogFragment = new SetIterationTimesDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_iteration_times, null);
        SeekBar seekBar = view.findViewById(R.id.dialog_pack_iterating_times_horizontal_counter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int value = bundle.getInt("value",3);
            seekBar.setProgress(value);
            seekBar.refreshDrawableState();
        }

        builder
                .setView(view)
                .setTitle(R.string.iterating_times_double_dot)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = getActivity().getIntent();
                                switch (getTargetRequestCode()) {
                                    case 1:
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt("number_of_notifies_in_bar", (int) Math.round(seekBar.getProgress()));
                                        editor.apply();
                                        break;
                                    case 99: // Learn Pack
                                        intent.putExtra("iteration_times", (int) Math.round(seekBar.getProgress()));
                                        break;
                                }
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                                }
                            }

                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
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
}
