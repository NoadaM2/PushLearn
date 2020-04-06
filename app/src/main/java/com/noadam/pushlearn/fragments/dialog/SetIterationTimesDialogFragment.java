package com.noadam.pushlearn.fragments.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.EditText;
import android.widget.SeekBar;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.activities.LearnPackActivity;
import com.noadam.pushlearn.activities.MenuActivity;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

public class SetIterationTimesDialogFragment extends DialogFragment {

    public static SetIterationTimesDialogFragment newInstance(int value){
        SetIterationTimesDialogFragment dialogFragment = new SetIterationTimesDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static SetIterationTimesDialogFragment newInstance(Card card){
        SetIterationTimesDialogFragment dialogFragment = new SetIterationTimesDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("_id", card.get_id());
        bundle.putString("question", card.getQuestion());
        bundle.putString("answer", card.getAnswer());
        bundle.putInt("iterating_times", card.getIteratingTimes());
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
        if(getTargetRequestCode() == 99) {
            if (bundle != null) {
                int value = bundle.getInt("value", 3);
                seekBar.setProgress(value);
                seekBar.refreshDrawableState();
            }
        }
       final int _id = bundle.getInt("_id", 3);
       final String question = bundle.getString("question");
       final String answer  = bundle.getString("answer");
        if(getTargetRequestCode() == 3) {
            if (bundle != null) {
                int iterating_times = bundle.getInt("iterating_times",3);
                seekBar.setProgress(iterating_times);
                seekBar.refreshDrawableState();
            }
        }
        TypedValue tV = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.blackcolor, tV, true);
        SpannableString s = new SpannableString(getString(R.string.iterating_times_double_dot));
        s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
        builder
                .setView(view)
                .setTitle(s)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(getTargetRequestCode() == 99) {
                                    Intent intent = getActivity().getIntent();
                                    intent.putExtra("iteration_times", (int) Math.round(seekBar.getProgress()));
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                                }
                                if(getTargetRequestCode() == 3) {
                                   PushLearnDBHelper dbHelper = new PushLearnDBHelper(getActivity());
                                   dbHelper.editCardById(_id,question,answer,(int)Math.round(seekBar.getProgress()));
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                                }
                                }
                            }

                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
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
