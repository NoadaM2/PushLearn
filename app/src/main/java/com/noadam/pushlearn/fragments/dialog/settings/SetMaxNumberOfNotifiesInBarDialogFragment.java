package com.noadam.pushlearn.fragments.dialog.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.noadam.pushlearn.R;
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
                builder.setView(view)
                        .setTitle(R.string.how_many_notifies_u_want_to_see)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent intent = getActivity().getIntent();
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
}
