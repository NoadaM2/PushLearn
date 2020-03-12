package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.adapters.PackListAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.internet.GetComPackByIDCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;
import com.noadam.pushlearn.internet.UpdateMyComPacksCallBack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        nickNameTextView = view.findViewById(R.id.nickname_textView);
        noMyComPacksTextView = view.findViewById(R.id.no_my_com_packs_textView);
        ratingTextView = view.findViewById(R.id.rating_number_textView);
        numberOfPacksTextView = view.findViewById(R.id.number_of_packs_textView);
        avatarImageView = view.findViewById(R.id.avatar_imageView);
        flagImageView = view.findViewById(R.id.flag_imageView);
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        myComPacksRecyclerView = view.findViewById(R.id.my_com_packs_RecyclerView);
        registerForContextMenu(myComPacksRecyclerView);
        myComPackList = dbHelper.getSavedMyComPacksList();

        fillRecyclerView();

        return view;
    }


    private void fillRecyclerView() {
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

            }
        }, new MyComPacksAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onLongClick(ComPack myComPack, View v) {

            }
        });
        myComPacksAdapter.setComPackList(myComPackList);
        myComPacksRecyclerView.setAdapter(myComPacksAdapter);
        myComPacksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void setValuesForViews() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        nickNameTextView.setText(prefs.getString("nickname","nickname"));
       // ratingTextView.setText(getRatingOfUser(prefs.getString("account_hash","")));  TODO Integrate get_rating_by_nickname
       // numberOfPacksTextView.setText(getNumberOfPacksOfUser(prefs.getString("account_hash","")));  TODO Integrate get_number_of_packs_by_nickname
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
}
