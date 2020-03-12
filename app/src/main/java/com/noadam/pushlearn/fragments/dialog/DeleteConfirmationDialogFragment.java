package com.noadam.pushlearn.fragments.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.DialogFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.noadam.pushlearn.R;

public class DeleteConfirmationDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Integer action = getTargetRequestCode();
        String title = "";
        switch (action) {
            case 41: // Delete card
                title = getResources().getString( R.string.are_u_sure_to_delete_card);
                break;
            case 42: // Delete cards
                title = getResources().getString( R.string.are_u_sure_to_delete_cards);
                break;
            case 51: // Delete pack
                title = getResources().getString( R.string.are_u_sure_to_delete_pack);
                break;
            case 52: // Delete packs
                title = getResources().getString( R.string.are_u_sure_to_delete_packs);
                break;
            case 666: // Delete card
                title = getResources().getString( R.string.are_u_sure_to_log_out);
                break;
        }
            builder
                    .setTitle(title)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    switch (action) {
                                        case 666:
                                            Intent intent = getActivity().getIntent();
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("login", "");
                                            editor.putString("password", "");
                                            editor.apply();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("fragment","settings");
                                            startActivity(intent);
                                            break;
                                        default:
                                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                                    break;
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
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        return alert;
    }
}