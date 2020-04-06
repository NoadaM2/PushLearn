package com.noadam.pushlearn.activities;


import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

import java.util.Collections;
import java.util.List;

public class LearnPackActivity extends AppCompatActivity {

    private static final String PACK_NAME = "packName";
    private static final String CURRENT_INDEX = "currentCardIndex";
    private static final String CURRENT_LOOP = "currentLoop";
    private InterstitialAd mInterstitialAd;
    private String packName;
    private String mode;
    private int cardListSize;
    private PushLearnDBHelper dbHelper;
    private String answer;
    private int cardsLearnt;
    private int currentLoop;
    private List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkTheme);
        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            packName = savedInstanceState.getString(PACK_NAME);
            mode = savedInstanceState.getString("mode");
        }
        setContentView(R.layout.activity_quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //-----------------------------------INTENT UNPACKING----------------------------------------------------
        Intent intent = getIntent();
        packName = intent.getStringExtra(PACK_NAME);
        mode = intent.getStringExtra("mode");
        dbHelper = new PushLearnDBHelper(this);
        refreshCardList();
        //------------------------------------ACTION BAR----------------------------------------------------
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.quiz_activity_toolbar);
        setSupportActionBar(mActionBarToolbar);
          if(!packName.equals("")) {
              getSupportActionBar().setTitle(packName);
          } else {
              getSupportActionBar().setTitle(getString(R.string.title_nowLearning));
          }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
         //   setContentView(R.layout.frag_community_packs);
        }
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
       AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
      //  mAdView.setVisibility(View.GONE);
        mAdView.loadAd(adRequest);
//----------------------------------I_KNOW_BUTTON ON CLICK LISTENER--------------------------------------------------
        i_know_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card;
                card = cardList.get(0);
                if (card.getIteratingTimes() == 1) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("LearntCards",  prefs.getInt("LearntCards",0) + 1);
                    editor.apply();
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
                    setContentView(R.layout.frag_community_packs);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardsLearnt > 8) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        cardsLearnt = 0;
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
                cardsLearnt++;
                answerTextView.setText(answer);
                i_know_button.setVisibility(View.VISIBLE);
                i_do_not_know_button.setVisibility(View.VISIBLE);
            }
        });

        Button disableAdvertisementButton = findViewById(R.id.disable_advertisement_button);
        if(false) { // TODO Subscribe check here
            disableAdvertisementButton.setVisibility(View.GONE);
        } else {
            disableAdvertisementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new SubscribeActivity().createIntent(getApplicationContext(), 8);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            });
        }
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

    public Intent createIntent(Context context, String packName, String mode) {
        return new Intent(context, LearnPackActivity.class)
                .putExtra(PACK_NAME, packName)
                .putExtra("mode", mode);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PACK_NAME, packName);
        outState.putString("mode", mode);
        super.onSaveInstanceState(outState);
    }

}