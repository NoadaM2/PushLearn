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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.CardsOfComPackAdapter;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComCard;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommunityPackFragment extends Fragment {
    private Context context;
    private PushLearnDBHelper dbHelper;
   private TextView creatorTextView;
   private TextView noMyComCardsInComPackTextView;
   private TextView ratingOfComPackTextView;
   private TextView numberOfCardsInComPackTextView;
   private TextView directoryTextView;
   private TextView subdirectoryTextView;
   private TextView descriptionOfComPackTextView;
   private ImageView flagImageView;
   private ImageButton downloadComPack;
    private RecyclerView cardsOfComPackRecyclerView;
    private ComPack comPack;
    private ArrayList<ComCard> cardsOfComPackList;
    boolean comPackStarred = false;

    public void setComPack(ComPack comPack) {
        this.comPack = comPack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frag_community_pack, null);
        context = getActivity();
        dbHelper = new PushLearnDBHelper(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash = prefs.getString("account_hash","");
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = view.findViewById(R.id.only_options_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(comPack.getComPackName());
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        creatorTextView = view.findViewById(R.id.creator_nickname_textView);
        creatorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileFragment fragment = new UserProfileFragment();
                fragment.setUserID(comPack.getComPackOwnerID());
                loadFragment(fragment);
            }
        });
        noMyComCardsInComPackTextView = view.findViewById(R.id.no_cards_of_com_pack_textView);
        ratingOfComPackTextView = view.findViewById(R.id.ratingOfComPack_number_textView);
        directoryTextView = view.findViewById(R.id.directory_value_textView);
        subdirectoryTextView = view.findViewById(R.id.subdirectory_value_textView);
        ratingOfComPackTextView = view.findViewById(R.id.ratingOfComPack_number_textView);
        numberOfCardsInComPackTextView = view.findViewById(R.id.number_of_cards_textView);
        descriptionOfComPackTextView = view.findViewById(R.id.pack_description_textView);
        flagImageView = view.findViewById(R.id.flagOfCreator_imageView);
        downloadComPack = view.findViewById(R.id.download_community_pack_button);
        downloadComPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comPackStarred) {
                    if (!dbHelper.doesPackExistByPackName(comPack.getComPackName())) {
                        starPack(comPack.getComPackID(), hash);
                    } else {
                        Toast.makeText(context, getString(R.string.you_already_have_pack_with_such_name), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // delete pack from user
                    unStarPackResponse(comPack.getComPackID(), hash);
                }
            }
        });
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        cardsOfComPackRecyclerView = view.findViewById(R.id.cards_of_com_pack_RecyclerView);
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
        CardsOfComPackAdapter cardsOfComPackAdapter = new CardsOfComPackAdapter(new CardsOfComPackAdapter.OnRecyclerViewItemClickListener() {
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

    private void starPack(int packID, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendStarPackByHashResponse(packID, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                dbHelper.addNewPack(new Pack(comPack.getComPackName(),"downloaded", comPack.getComPackID()));
                for(ComCard card : cardsOfComPackList) {
                    dbHelper.addNewCard(new Card(comPack.getComPackName(), card.getQuestion(), card.getAnswer()));
                }
                ratingOfComPackTextView.setText(String.valueOf(Integer.valueOf(String.valueOf(ratingOfComPackTextView.getText())) + 1));
                downloadComPack.setImageResource(R.drawable.ic_star_filled_72dp);
                comPackStarred = true;
            }
            @Override
            public void onError( Throwable t) {

            }
        });
    }

    private void fillCardsOfComPackListAndRecyclerView(int packID, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetCardsOfComPackByPackIDResponse(packID, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                ParserFromJSON parser = new ParserFromJSON();
               cardsOfComPackList = parser.parseJsonComCardsArray(value);
               numberOfCardsInComPackTextView.setText(String.valueOf(cardsOfComPackList.size()));
               fillRecyclerView();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setValuesForViews() {
         ratingOfComPackTextView.setText(String.valueOf(comPack.getComPackRating()));
         descriptionOfComPackTextView.setText(comPack.getComPackDescription());
         setNickNameByIDResponse(comPack.getComPackOwnerID());
         setDirectoryTextViewResponse(comPack.getComPackDirectoryId());
         setSubDirectoryTextViewResponse(comPack.getComPackSubdirectoryId());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String nickname = prefs.getString("nickname","");
        compareUserIdAndHashResponse(nickname);
    }

    private void setNickNameByIDResponse(int id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNickNameByIDResponse(id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String nickName) {
                creatorTextView.setText(nickName);
                setLanguageIDByNickName(nickName);

            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void setDirectoryTextViewResponse(int id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoryByIDResponse(id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String response) {
                ParserFromJSON parser = new ParserFromJSON();
                String directory = parser.parseJsonDirectory(response).getName();
                directoryTextView.setText(directory);
            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void setSubDirectoryTextViewResponse(int id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetSubDirectoryByIDResponse(id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String response) {
                ParserFromJSON parser = new ParserFromJSON();
                subdirectoryTextView.setText(parser.parseJsonSubDirectory(response).getName());
            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void compareUserIdAndHashResponse(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetIdByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                if(Integer.valueOf(value) == comPack.getComPackOwnerID()) {
                    downloadComPack.setClickable(false);
                    downloadComPack.setImageResource(R.drawable.ic_key_72dp);
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String hash = prefs.getString("account_hash","");
                    setStarButtonResponse(comPack.getComPackID(), hash);
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void unStarPackResponse(int id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendUnStarPackByHashAndPackIDResponse(id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String answer) {
                if(answer.equals("ok")) {
                    dbHelper.deletePackByPackName(comPack.getComPackName());
                    ratingOfComPackTextView.setText(String.valueOf(Integer.valueOf(String.valueOf(ratingOfComPackTextView.getText())) - 1));
                    downloadComPack.setImageResource(R.drawable.ic_star_only_outline_yellow_72dp);
                    comPackStarred = false;
                }
            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void setStarButtonResponse(int packID, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendIfUserStaredPackByHashAndPackIDResponse(packID, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String answer) {
                if(answer.equals("no")) {
                    downloadComPack.setImageResource(R.drawable.ic_star_only_outline_yellow_72dp);
                    comPackStarred = false;
                } else {
                    downloadComPack.setImageResource(R.drawable.ic_star_filled_72dp);
                    comPackStarred = true;
                }
            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void setLanguageIDByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetLanguageIDByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                switch (value) {
                    case "0":
                        break;
                    case "1":
                        flagImageView.setImageResource(R.drawable.ic_united_kingdom);
                        break;
                    case "11":
                        flagImageView.setImageResource(R.drawable.ic_united_states);
                        break;
                    case "2":
                        flagImageView.setImageResource(R.drawable.ic_flag_russia);
                        break;
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
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

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
