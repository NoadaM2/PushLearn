package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.ContextMenu;
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
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MyProfileFragment extends Fragment {
    private Context context;
    private PushLearnDBHelper dbHelper;
    private TextView nickNameTextView;
    private TextView noMyComPacksTextView;
    private TextView ratingTextView;
    private TextView numberOfPacksTextView;
    private ImageView avatarImageView;
    private ImageView flagImageView;
    private RecyclerView myComPacksRecyclerView;
    private MyComPacksAdapter myComPacksAdapter;
    private ArrayList<ComPack> myComPackList;
    private ComPack longClickedComPack;

    final int MENU_DELETE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frag_my_profile, null);
        context = getActivity();
        dbHelper = new PushLearnDBHelper(context);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = view.findViewById(R.id.my_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        nickNameTextView = view.findViewById(R.id.my_nickname_textView);
        noMyComPacksTextView = view.findViewById(R.id.no_my_com_packs_textView);
        ratingTextView = view.findViewById(R.id.my_rating_number_textView);
        numberOfPacksTextView = view.findViewById(R.id.my_number_of_packs_textView);
        avatarImageView = view.findViewById(R.id.my_avatar_imageView);
        flagImageView = view.findViewById(R.id.my_flag_imageView);
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        myComPacksRecyclerView = view.findViewById(R.id.my_com_packs_RecyclerView);
        registerForContextMenu(myComPacksRecyclerView);

        fillRecyclerView();

        return view;
    }

    private void sortComPackList() {
        Collections.sort(myComPackList, new Comparator<ComPack>() { // sorting
            @Override
            public int compare(ComPack lhs, ComPack rhs) {
                return Integer.valueOf(rhs.getComPackRating()).compareTo(Integer.valueOf(lhs.getComPackRating()));
            }
        });
    }

    private void fillRecyclerView() {
        myComPackList = dbHelper.getSavedMyComPacksList();
        sortComPackList();
        if (!myComPackList.isEmpty()) {
            noMyComPacksTextView.setVisibility(View.GONE);
        }
        else {
            noMyComPacksTextView.setText(R.string.you_published_no_packs);
            noMyComPacksTextView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myComPacksRecyclerView.setLayoutManager(layoutManager);
        myComPacksAdapter = new MyComPacksAdapter(new MyComPacksAdapter.OnRecyclerViewItemClickListener() {
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
                longClickedComPack = myComPack;
            }
        });
        myComPacksAdapter.setComPackList(myComPackList);
        myComPacksRecyclerView.setAdapter(myComPacksAdapter);
        myComPacksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_DELETE, 1, R.string.delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                DeleteConfirmationDialogFragment dialogFragDelete = new DeleteConfirmationDialogFragment();
                dialogFragDelete.setTargetFragment(this, 53);
                dialogFragDelete.show(getFragmentManager().beginTransaction(), "packName");
                break;
        }
        return true;
    }

    private void deletePackResponse(int id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendDeletePackByHashAndPackIDResponse(id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String answer) {
                if(answer.equals("ok")) {
                }
            }
            @Override
            public void onError() {
            }
        });
    }

    private void setValuesForViews() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash = prefs.getString("account_hash","");
        nickNameTextView.setText(prefs.getString("nickname",""));
        ratingTextView.setText(prefs.getString("account_rating",""));
        String a = prefs.getString("account_count_of_packs","");
        numberOfPacksTextView.setText(String.valueOf(a));
        switch (prefs.getInt("account_language",0)) {
            case 0:
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
        }
        getNickNameByHashResponse(hash);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_for_my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile_toolbar_settings:
                Fragment fragment = new SettingsFragment();
                loadFragment(fragment);
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

    private void setNumberOfComPacksByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNumberOfComPacksByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("account_count_of_packs", value);
                editor.apply();
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("account_rating", value);
                editor.apply();
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("account_language", Integer.valueOf(value));
                editor.apply();
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

    private void getNickNameByHashResponse(String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNickNameByHashResponse(hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String nickName) {
                nickNameTextView.setText(nickName);
                setNumberOfComPacksByNickName(nickName);
                setRatingByNickName(nickName);
                setLanguageIDByNickName(nickName);
                saveMyComPacksByNickNameResponse(nickName,hash);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nickname", nickName);
                editor.apply();
            }
            @Override
            public void onError() {

            }
        });
    }

    private void saveMyComPacksByNickNameResponse(String nickname, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByNickNameResponse(nickname, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ArrayList<ComPack> comPacks = parseJsonComPacksArray(jsonResponse);
                dbHelper.saveMyComPacks(comPacks);
                dbHelper.close();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // добавление пакета
        switch (requestCode) {
            case 53:
                if (resultCode == Activity.RESULT_OK) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String hash = prefs.getString("account_hash","");
                    deletePackResponse(longClickedComPack.getComPackID(), hash);
                    setValuesForViews();
                }
        }
    }
}
