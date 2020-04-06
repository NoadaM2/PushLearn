package com.noadam.pushlearn.fragments.dialog.settings;

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
import androidx.core.app.NotificationManagerCompat;

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
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

import java.util.ArrayList;

//import static java.security.AccessController.getContext;

public class SetMaxNumberOfNotifiesInBarDialogFragment extends DialogFragment {

   NumberPicker mNumberPicker;

    public static SetMaxNumberOfNotifiesInBarDialogFragment newInstance(int value) {
        final SetMaxNumberOfNotifiesInBarDialogFragment
                fragment = new SetMaxNumberOfNotifiesInBarDialogFragment();
        final Bundle b = new Bundle(1);
        b.putInt("value", value);
        fragment.setArguments(b);
        return fragment;
    }
// ------ ONLY COPY-PASTE
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_only_number_picker, null);
        mNumberPicker = view.findViewById(R.id.dialog_number_picker);
        mNumberPicker.setMaxValue(10);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setWrapSelectorWheel(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int value = bundle.getInt("value");
            mNumberPicker.setValue(value);
        }
        TypedValue tV = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.blackcolor, tV, true);
        SpannableString s = new SpannableString(getString(R.string.how_many_notifies_u_want_to_see));
        s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                builder.setView(view)
                        .setTitle(s)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        SharedPreferences.Editor editor = prefs.edit();
                                        int numberPickerValue = Math.round(mNumberPicker.getValue());
                                        editor.putInt("number_of_notifies_in_bar", numberPickerValue );
                                        editor.putInt("ShownCards", 0);
                                        editor.apply();
                                        PushLearnDBHelper dbHelper = new PushLearnDBHelper(getActivity());
                                       ArrayList<Card> shownCards = dbHelper.getShownCardList();
                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                                       for (Card card : shownCards) {
                                           notificationManager.cancel(card.get_id());
                                       }
                                        dbHelper.setCardsUnShown();
                                        startActivityForResult(new SettingsActivity().createIntent(getActivity()), SettingsActivity.LIMIT_NOTIFIES_IN_BAR_DIALOG_RESULT);
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
