package com.noadam.pushlearn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.internet.PushLearnServerStringCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserProfileActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("theme","Light").equals("Light")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.DarkTheme);
        }
        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = getApplicationContext();
        dbHelper = new PushLearnDBHelper(context);
//-----------------------------------INTENT UNPACKING----------------------------------------------------
        Intent intent = getIntent();
        userID = intent.getIntExtra("id",1);
        dbHelper = new PushLearnDBHelper(this);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        nickNameTextView = findViewById(R.id.user_nickname_textView);
        noComPacksTextView = findViewById(R.id.no_user_com_packs_textView);
        ratingTextView = findViewById(R.id.user_rating_number_textView);
        numberOfPacksTextView = findViewById(R.id.user_number_of_packs_textView);
        avatarImageView = findViewById(R.id.user_avatar_imageView);
        flagImageView = findViewById(R.id.user_flag_imageView);
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        userComPacksRecyclerView = findViewById(R.id.user_com_packs_RecyclerView);
        registerForContextMenu(userComPacksRecyclerView);

    }
    
    private void configureToolBar(String nickname){
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(mActionBarToolbar);
        if(!nickname.equals("")) {
            getSupportActionBar().setTitle(nickname);
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
        sortComPackList();
        if (!userComPackList.isEmpty()) {
            noComPacksTextView.setVisibility(View.GONE);
        }
        else {
            noComPacksTextView.setText(R.string.user_published_no_packs);
            noComPacksTextView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        userComPacksRecyclerView.setLayoutManager(layoutManager);
        userComPacksAdapter = new MyComPacksAdapter(new MyComPacksAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(ComPack myComPack, View v) {
                startActivity(new CommunityPackActivity().createIntent(context, myComPack));
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
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader.init(config);
        imageLoader.displayImage("http://pushlearn.hhos.ru.s68.hhos.ru/files/"+String.valueOf(userID)+".jpg", avatarImageView);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }

    private void setNumberOfComPacksByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNumberOfComPacksByNickNameResponse(nickname, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String value) {
                numberOfPacksTextView.setText(value);
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setRatingByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetRatingByNickNameResponse(nickname, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String value) {
                ratingTextView.setText(value);
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setLanguageIDByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetLanguageIDByNickNameResponse(nickname, new PushLearnServerStringCallBack() {
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

    private void setNickNameByIDResponse(int id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNickNameByIDResponse(id, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String nickName) {
                nickNameTextView.setText(nickName);
                setLanguageIDByNickName(nickName);
                setRatingByNickName(nickName);
                setNumberOfComPacksByNickName(nickName);
                configureToolBar(nickName);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = prefs.getString("account_hash","");
                fillUserComPacksByNickNameResponse(nickName, hash);
            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    public Intent createIntent(Context context,int userID) {
        return new Intent(context, UserProfileActivity.class)
                .putExtra("id", userID);
    }

    private void fillUserComPacksByNickNameResponse(String nickname, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByNickNameResponse(nickname, hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                userComPackList = parser.parseJsonComPacksArray(jsonResponse);
                fillRecyclerView();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
