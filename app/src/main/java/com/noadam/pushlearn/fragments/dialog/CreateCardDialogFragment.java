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
import android.widget.TextView;

import com.gildaswise.horizontalcounter.HorizontalCounter;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.entities.Card;

public class CreateCardDialogFragment extends DialogFragment {

    TextView QuestionEditText;
    TextView AnswerEditText;
    HorizontalCounter horizontalCounter;

    public static CreateCardDialogFragment newInstance(){

        CreateCardDialogFragment dialogFragment = new CreateCardDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_card, null);
        QuestionEditText = view.findViewById(R.id.dialog_card_question_edit_text);
        AnswerEditText = view.findViewById(R.id.dialog_card_answer_edit_text);
        horizontalCounter = view.findViewById(R.id.dialog_card_iterating_times_horizontal_counter);
        builder.setView(view)
                .setTitle(R.string.new_card)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = getActivity().getIntent();
                                intent.putExtra("question", String.valueOf(QuestionEditText.getText()));
                                intent.putExtra("answer", String.valueOf(AnswerEditText.getText()));
                                intent.putExtra("iteratingTimes", (int)Math.round(horizontalCounter.getCurrentValue()));
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


