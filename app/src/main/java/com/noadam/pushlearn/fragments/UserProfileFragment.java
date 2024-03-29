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
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class UserProfileFragment extends Fragment {
    private Context context;
    private PushLearnDBHelper dbHelper;
    private TextView nickNameTextView;
    private TextView noComPacksTextView;
    private TextView ratingTextView;
    private TextView numberOfPacksTextView;
    private ImageView avatarImageView;
    private ImageView flagImageView;
    private RecyclerView userComPacksRecyclerView;
    private MyComPacksAdapter userComPacksAdapter;
    private ArrayList<ComPack> userComPackList;
    private int userID;

    public void setUserID(int id) {
        userID = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frag_user_profile, null);
        context = getActivity();
        dbHelper = new PushLearnDBHelper(context);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = view.findViewById(R.id.user_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        nickNameTextView = view.findViewById(R.id.user_nickname_textView);
        noComPacksTextView = view.findViewById(R.id.no_user_com_packs_textView);
        ratingTextView = view.findViewById(R.id.user_rating_number_textView);
        numberOfPacksTextView = view.findViewById(R.id.user_number_of_packs_textView);
        avatarImageView = view.findViewById(R.id.user_avatar_imageView);
        flagImageView = view.findViewById(R.id.user_flag_imageView);
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        userComPacksRecyclerView = view.findViewById(R.id.user_com_packs_RecyclerView);
        registerForContextMenu(userComPacksRecyclerView);

        return view;
    }

    private void sortComPackList() {
        Collections.sort(userComPackList, new Comparator<ComPack>() { // sorting
            @Override
            public int compare(ComPack lhs, ComPack rhs) {
                return Integer.valueOf(rhs.getComPackRating()).compareTo(Integer.valueOf(lhs.getComPackRating()));
            }
        });
    }

    private void fillRecyclerView() {
        userComPackList = dbHelper.getSavedMyComPacksList();
        sortComPackList();
        if (!userComPackList.isEmpty()) {
            noComPacksTextView.setVisibility(View.GONE);
        }
        else {
            noComPacksTextView.setText(R.string.you_published_no_packs);
            noComPacksTextView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        userComPacksRecyclerView.setLayoutManager(layoutManager);
        userComPacksAdapter = new MyComPacksAdapter(new MyComPacksAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(ComPack myComPack, View v) {
                CommunityPackFragment nextFrag= new CommunityPackFragment();
                nextFrag.setComPack(myComPack);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        }, new MyComPacksAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onLongClick(ComPack myComPack, View v) {

            }
        });
        userComPacksAdapter.setComPackList(userComPackList);
        userComPacksRecyclerView.setAdapter(userComPacksAdapter);
        userComPacksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void setValuesForViews() {
        setNickNameByIDResponse(userID);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_only_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }

    private void setNumberOfComPacksByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNumberOfComPacksByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                numberOfPacksTextView.setText(value);
            }
            @Override
            public void onError() {

            }
        });
    }

    private void setRatingByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetRatingByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                ratingTextView.setText(value);
            }
            @Override
            public void onError() {

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
            public void onError() {

            }
        });
    }

    private void setNickNameByIDResponse(int id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNickNameByIDResponse(id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String nickName) {
                nickNameTextView.setText(nickName);
                setLanguageIDByNickName(nickName);
                setRatingByNickName(nickName);
                setNumberOfComPacksByNickName(nickName);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = prefs.getString("account_hash","");
                fillUserComPacksByNickNameResponse(nickName, hash);
            }
            @Override
            public void onError() {
            }
        });
    }

    private void fillUserComPacksByNickNameResponse(String nickname, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByNickNameResponse(nickname, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                userComPackList = parseJsonComPacksArray(jsonResponse);
                fillRecyclerView();
            }
            @Override
            public void onError() {

            }
        });
    }

    private ArrayList<ComPack> parseJsonComPacksArray(String jsonResponse) {
        ArrayList<ComPack> comPacks = new ArrayList<ComPack>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int packID = jsonObject.getInt("pack_id");
                int userID = jsonObject.getInt("user_id");
                String packName = jsonObject.getString("name");
                String packDescription = jsonObject.getString("description");
                String packAccess = jsonObject.getString("access");
                int packRating = jsonObject.getInt("rating");
                int packDirectoryId = jsonObject.getInt("directory_id");
                int packSubdirectoryID = jsonObject.getInt("subdirectory_id");
                comPacks.add(new ComPack(packID,userID,packName,packRating,packDescription,packAccess,packDirectoryId,packSubdirectoryID));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return comPacks;
    }
}
