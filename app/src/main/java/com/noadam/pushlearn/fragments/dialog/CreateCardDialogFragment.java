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
import android.widget.SeekBar;
import android.widget.TextView;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.entities.Card;

public class CreateCardDialogFragment extends DialogFragment {

    private int id;
    public static CreateCardDialogFragment newInstance(Card card){

        CreateCardDialogFragment dialogFragment = new CreateCardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", card.get_id());
        bundle.putString("question", card.getQuestion());
        bundle.putString("answer", card.getAnswer());
        bundle.putInt("iteratingTimes", card.getIteratingTimes());
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_card, null);
        TextView QuestionEditText = view.findViewById(R.id.dialog_card_question_edit_text);
        TextView AnswerEditText = view.findViewById(R.id.dialog_card_answer_edit_text);
        SeekBar horizontalCounter = view.findViewById(R.id.dialog_card_iterating_times_horizontal_counter);
        int iteratingTimes = 3;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
            String question = bundle.getString("question");
            QuestionEditText.setText(question);
            String answer = bundle.getString("answer");
            AnswerEditText.setText(answer);
            iteratingTimes = bundle.getInt("iteratingTimes");
            horizontalCounter.setProgress(iteratingTimes);
            horizontalCounter.refreshDrawableState();
        }


        switch (getTargetRequestCode()) {
            case 1:
                builder.setView(view)
                        .setTitle(R.string.new_card)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent intent = getActivity().getIntent();
                                        intent.putExtra("question", String.valueOf(QuestionEditText.getText()));
                                        intent.putExtra("answer", String.valueOf(AnswerEditText.getText()));
                                        intent.putExtra("iteratingTimes", (int) Math.round(horizontalCounter.getProgress()));
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

            case 2:
                builder.setView(view)
                        .setTitle(R.string.edit_card)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent intent = getActivity().getIntent();
                                        intent.putExtra("id", id);
                                        intent.putExtra("question", String.valueOf(QuestionEditText.getText()));
                                        intent.putExtra("answer", String.valueOf(AnswerEditText.getText()));
                                        intent.putExtra("iteratingTimes", (int) Math.round(horizontalCounter.getProgress()));
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
        }
            return builder.create();
        }

    }


