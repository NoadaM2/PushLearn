package com.noadam.pushlearn.fragments.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.noadam.pushlearn.R;

public class DeleteConfirmationDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
        }
            builder
                    .setTitle(title)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = getActivity().getIntent();
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
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