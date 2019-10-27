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
import android.widget.TextView;

import com.noadam.pushlearn.R;
public class CreatePackDialogFragment extends DialogFragment {

    EditText packNameEditText;

    public static CreatePackDialogFragment newInstance(){

        CreatePackDialogFragment dialogFragment = new CreatePackDialogFragment();
        Bundle bundle = new Bundle();
        //bundle.putInt("num", num);
        dialogFragment.setArguments(bundle);

        return dialogFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_pack, null);
        packNameEditText = view.findViewById(R.id.dialog_add_pack_title_edit_text);
        builder.setView(view)
                .setTitle(R.string.enter_packName)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = getActivity().getIntent();
                                intent.putExtra("packName", String.valueOf(packNameEditText.getText()));
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


