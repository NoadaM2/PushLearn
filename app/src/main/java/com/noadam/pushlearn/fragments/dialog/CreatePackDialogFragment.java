package com.noadam.pushlearn.fragments.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.noadam.pushlearn.R;

public class CreatePackDialogFragment extends DialogFragment {

    EditText packNameEditText;



    public static CreatePackDialogFragment newInstance(String packName){
        CreatePackDialogFragment dialogFragment = new CreatePackDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("packName", packName);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_pack, null);
        packNameEditText = view.findViewById(R.id.dialog_add_pack_title_edit_text);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String packName = bundle.getString("packName");
            packNameEditText.setText(packName);
        }
        Integer action = getTargetRequestCode();
        switch (action) {
            case 1: // New pack
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
            break;
            case 2:  // Edit pack name
                builder.setView(view)
                        .setTitle(R.string.edit_packName)
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
                break;
            case 3:  // Edit pack name
                builder.setView(view)
                        .setTitle(R.string.edit_nickname)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if(String.valueOf(packNameEditText.getText()).length() <= 15) {
                                            Intent intent = getActivity().getIntent();
                                            intent.putExtra("nickName", String.valueOf(packNameEditText.getText()));
                                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                                        } else {
                                            Toast.makeText(getActivity(), getString(R.string.nickname_should_be_less_than_15), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        )
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                            }
                        });
                break;
        }
        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        return alert;
    }
}