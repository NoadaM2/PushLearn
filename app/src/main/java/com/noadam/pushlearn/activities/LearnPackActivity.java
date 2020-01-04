package com.noadam.pushlearn.activities;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

import java.util.Collections;
import java.util.List;

public class LearnPackActivity extends AppCompatActivity {

    private static final String QUIZ_MODE = "quizMode";
    private static final String PACK_NAME = "packName";
    private static final String CURRENT_INDEX = "currentCardIndex";
    private static final String CURRENT_LOOP = "currentLoop";

    private Intent intent;
    private String packName;
    private String mode;
    private int cardListSize;
    private PushLearnDBHelper dbHelper;
    private String answer;
    private int currentCardIndex;
    private int currentLoop;
    private List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //------------------------------------ACTION BAR----------------------------------------------------
        //  setHasOptionsMenu(true);
        //  пу().setTitle(packName);

        //-----------------------------------INTENT UNPACKING----------------------------------------------------
        intent = getIntent();
        packName = intent.getStringExtra(PACK_NAME);
        mode = intent.getStringExtra("mode");
        dbHelper = new PushLearnDBHelper(this);
        refreshCardList();

        //------------------------------------VIEWS INITIALIZATION-------------------------------------------
        final TextView questionTextView = findViewById(R.id.activity_quiz_question);
        final TextView answerTextView = findViewById(R.id.activity_quiz_answer);
        ImageButton i_know_button = findViewById(R.id.activity_quiz_i_know_button);
        ImageButton i_do_not_know_button = findViewById(R.id.activity_quiz_i_do_not_know_button);
        i_know_button.setVisibility(View.INVISIBLE);
        i_do_not_know_button.setVisibility(View.INVISIBLE);
        FloatingActionButton fab = findViewById(R.id.activity_quiz_fab);

        //----------------------------------SET THE FIRST ELEMENT--------------------------------------------------
        while (!(cardList.get(0).getIteratingTimes() > 0)) {
            cardList.remove(0);
        }
        if (cardList.get(0) != null) {
            answer = cardList.get(0).getAnswer();
            questionTextView.setText(cardList.get(0).getQuestion());
        } else {
            // NO CARD IN PACK TO LEARN
            setContentView(R.layout.frag_com_packs);
        }

//----------------------------------I_KNOW_BUTTON ON CLICK LISTENER--------------------------------------------------
        i_know_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card;
                card = cardList.get(0);
                if (card.getIteratingTimes() == 1) {
                    Toast.makeText(getApplicationContext(), R.string.congrats_you_learnt_card, Toast.LENGTH_SHORT).show();
                }
                dbHelper.editCardById(card.get_id(), card.getQuestion(), card.getAnswer(), card.getIteratingTimes() - 1, true);
                cardList.remove(0);

                Card nextCard;
                if (cardList.size() < 1) {
                    refreshCardList();
                }
                if (cardList.size() > 0) {
                    nextCard = cardList.get(0);
                    questionTextView.setText(nextCard.getQuestion());
                    answer = nextCard.getAnswer();
                    answerTextView.setText(R.string.three_dots);
                    i_know_button.setVisibility(View.INVISIBLE);
                    i_do_not_know_button.setVisibility(View.INVISIBLE);
                } else {
                    // NO CARD IN PACK TO LEARN

                    Intent intent = new Intent(LearnPackActivity.this, MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    switch (mode) {
                        case "pack": Toast.makeText(getApplicationContext(), R.string.congrats_you_learnt_pack, Toast.LENGTH_SHORT).show();
                        intent.putExtra("fragment", "my_packs");
                        break;
                        case "now_learning":
                            Toast.makeText(getApplicationContext(), R.string.congrats_you_learnt_all_cards, Toast.LENGTH_SHORT).show();
                            intent.putExtra("fragment", "now_learning");
                            break;
                    }
                    startActivity(intent);
                }
            }
        });

//----------------------------------I_DO_NOT_KNOW_BUTTON ON CLICK LISTENER--------------------------------------------------
        i_do_not_know_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card = cardList.get(0);
                dbHelper.editCardById(card.get_id(), card.getQuestion(), card.getAnswer(), card.getIteratingTimes() + 1, true);
                cardList.remove(0);

                Card nextCard;
                if (cardList.size() < 1) {
                    refreshCardList();
                }
                nextCard = cardList.get(0);
                if (nextCard != null) {
                    questionTextView.setText(nextCard.getQuestion());
                    answer = nextCard.getAnswer();
                    answerTextView.setText(R.string.three_dots);
                    i_know_button.setVisibility(View.INVISIBLE);
                    i_do_not_know_button.setVisibility(View.INVISIBLE);
                } else {
                    // NO CARD IN PACK TO LEARN
                    setContentView(R.layout.frag_com_packs);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerTextView.setText(answer);
                i_know_button.setVisibility(View.VISIBLE);
                i_do_not_know_button.setVisibility(View.VISIBLE);
            }
        });
    }

    private void refreshCardList() {
        switch (mode) {
            case "pack":
            dbHelper.setCardsOfPackUnShown(packName); // need to be tested
            cardList = dbHelper.getCardListByPackName(packName, 0);
            break;
            case "now_learning":
                dbHelper.setCardsUnShown();
                cardList = dbHelper.getNowLearningCardList(0);
                break;
        }
        Collections.shuffle(cardList);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public Intent createIntent(Context context, String packName, String mode) {
        return new Intent(context, LearnPackActivity.class)
                .putExtra(PACK_NAME, packName)
                .putExtra("mode", mode);

    }
}