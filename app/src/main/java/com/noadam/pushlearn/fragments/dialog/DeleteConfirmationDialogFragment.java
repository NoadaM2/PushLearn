package com.noadam.pushlearn.fragments.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.activities.MenuActivity;
import com.noadam.pushlearn.activities.SettingsActivity;

public class DeleteConfirmationDialogFragment extends DialogFragment {

    public static final String LOG_OUT = "log_out";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Integer action = getTargetRequestCode();
        SpannableString title = new SpannableString("");
        TypedValue tV = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.blackcolor, tV, true);
        SpannableString s = new SpannableString(getString(R.string.are_u_sure_to_delete_card));
        switch (action) {
            case 41: // Delete card
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                title = s;
                break;
            case 42: // Delete cards
                s = new SpannableString(getString(R.string.are_u_sure_to_delete_cards));
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                title = s;
                break;
            case 51: // Delete pack
                s = new SpannableString(getString(R.string.are_u_sure_to_delete_pack));
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                title = s;
                break;
            case 52: // Delete packs
                s = new SpannableString(getString(R.string.are_u_sure_to_delete_packs));
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                title = s;
                break;
                case 53: // Delete pack from community
                    s = new SpannableString(getString(R.string.are_u_sure_to_delete_pack_from_community));
                    s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                    title = s;
                break;
        }
        if (getTag() == LOG_OUT) {
            s = new SpannableString(getString(R.string.are_u_sure_to_log_out));
            s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
            title = s;
        }
            builder
                    .setTitle(title)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (getTag() == LOG_OUT) {
                                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("login", "");
                                        editor.putString("hash", "");
                                        editor.putString("password", "");
                                        editor.putString("vk_access_token", "");
                                        editor.putString("vk_user_id", "");
                                        editor.apply();
                                        getActivity().finish();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("fragment","my_profile");
                                        startActivity(intent);
                                    } else {
                                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                                    }
                                }
                            }
                    )
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!(getTag() == LOG_OUT)) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                            }
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