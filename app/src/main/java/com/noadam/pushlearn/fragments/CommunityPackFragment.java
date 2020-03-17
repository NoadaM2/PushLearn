package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.CardsOfComPackAdapter;
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.ComCard;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunityPackFragment extends Fragment {
    private Context context;
    private PushLearnDBHelper dbHelper;
   private TextView creatorTextView;
   private TextView noMyComCardsInComPackTextView;
   private TextView ratingOfComPackTextView;
   private TextView numberOfCardsInComPackTextView;
   private TextView descriptionOfComPackTextView;
   private ImageView flagImageView;
    private RecyclerView cardsOfComPackRecyclerView;
    private ComPack comPack;
    private CardsOfComPackAdapter cardsOfComPackAdapter;
    private ArrayList<ComCard> cardsOfComPackList;

    public void setComPack(ComPack comPack)
    {
        this.comPack = comPack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frag_community_pack, null);
        context = getActivity();
        dbHelper = new PushLearnDBHelper(context);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = view.findViewById(R.id.only_options_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(comPack.getComPackName());
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        creatorTextView = view.findViewById(R.id.creator_nickname_textView);
        noMyComCardsInComPackTextView = view.findViewById(R.id.no_cards_of_com_pack_textView);
        ratingOfComPackTextView = view.findViewById(R.id.ratingOfComPack_number_textView);
        numberOfCardsInComPackTextView = view.findViewById(R.id.number_of_cards_textView);
        descriptionOfComPackTextView = view.findViewById(R.id.pack_description_textView);
        flagImageView = view.findViewById(R.id.flagOfCreator_imageView);
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        cardsOfComPackRecyclerView = view.findViewById(R.id.cards_of_com_pack_RecyclerView);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash = prefs.getString("account_hash","");
        fillCardsOfComPackListAndRecyclerView(comPack.getComPackID(), hash);
        return view;
    }

    private void fillRecyclerView() {
        if (!cardsOfComPackList.isEmpty()) {
            noMyComCardsInComPackTextView.setVisibility(View.GONE);
        }
        else {
            noMyComCardsInComPackTextView.setText(R.string.no_cards_in_com_pack);
            noMyComCardsInComPackTextView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cardsOfComPackRecyclerView.setLayoutManager(layoutManager);
        cardsOfComPackAdapter = new CardsOfComPackAdapter(new CardsOfComPackAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(ComCard myComPack, View v) {

            }
        }, new CardsOfComPackAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onLongClick(ComCard myComPack, View v) {

            }
        });
        cardsOfComPackAdapter.setComCardList(cardsOfComPackList);
        cardsOfComPackRecyclerView.setAdapter(cardsOfComPackAdapter);
        cardsOfComPackRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void fillCardsOfComPackListAndRecyclerView(int packID, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetCardsOfComPackByPackIDResponse(packID, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
               cardsOfComPackList = parseJsonComCardsArray(value);
               numberOfCardsInComPackTextView.setText(String.valueOf(cardsOfComPackList.size()));
               fillRecyclerView();
            }
            @Override
            public void onError() {

            }
        });
    }

    private void setValuesForViews() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
         ratingOfComPackTextView.setText(String.valueOf(comPack.getComPackRating()));
         descriptionOfComPackTextView.setText(comPack.getComPackDescription());
      // switch (prefs.getInt("account_language",0)) { TODO Integrate get_language_id_by_nickname
      /*      case 0:
                break;
            case 1:
                flagImageView.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case 11:
                flagImageView.setImageResource(R.drawable.ic_united_states);
                break;
            case 2:
                flagImageView.setImageResource(R.drawable.ic_flag_russia);
                break;

        }*/
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_only_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.only_options_toolbar_options:

                break;
        }
        return true;
    }

    private ArrayList<ComCard> parseJsonComCardsArray(String jsonResponse) {
        ArrayList<ComCard> comCards = new ArrayList<ComCard>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int card_id = jsonObject.getInt("card_id");
                int pack_id = jsonObject.getInt("pack_id");
                String question = jsonObject.getString("question");
                String answer = jsonObject.getString("answer");
                comCards.add(new ComCard(card_id,pack_id,question,answer));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return comCards;
    }
}
