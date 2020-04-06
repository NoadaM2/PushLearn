package com.noadam.pushlearn.fragments.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
                TypedValue tV = new TypedValue();
                Resources.Theme theme = getActivity().getTheme();
                theme.resolveAttribute(R.attr.blackcolor, tV, true);
                SpannableString s = new SpannableString(getString(R.string.enter_packName));
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
            builder.setView(view)
                    .setTitle(s)
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
                tV = new TypedValue();
                theme = getActivity().getTheme();
                theme.resolveAttribute(R.attr.blackcolor, tV, true);
                s = new SpannableString(getString(R.string.edit_packName));
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                builder.setView(view)
                        .setTitle(s)
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
            case 3:  // Edit nick name
                tV = new TypedValue();
                theme = getActivity().getTheme();
                theme.resolveAttribute(R.attr.blackcolor, tV, true);
                s = new SpannableString(getString(R.string.edit_nickname));
                s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
                builder.setView(view)
                        .setTitle(s)
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
        TypedValue tV = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
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