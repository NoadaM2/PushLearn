package com.noadam.pushlearn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.adapters.TopOfUsersAdapter;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.User;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import java.util.ArrayList;

public class TopOfUsersActivity extends AppCompatActivity {

    TextView countdownTextView;
    RecyclerView userTopRecyclerView;
    Context context;

   TextView previous_first_place_Rating_TextView;
   TextView previous_first_place_nickname_TextView;
   ImageView previous_first_place_flag_ImageView;
   TextView previous_second_place_Rating_TextView;
   TextView previous_second_place_nickname_TextView;
   ImageView previous_second_place_flag_ImageView;
   TextView previous_third_place_Rating_TextView;
   TextView previous_third_place_nickname_TextView;
   ImageView previous_third_place_flag_ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("theme","Light").equals("Light")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.DarkTheme);
        }
        context = getApplicationContext();
        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_of_users);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //------------------------------------ACTION BAR----------------------------------------------------
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.top_users_toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.top_of_pushlearn_community);
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
        previous_first_place_Rating_TextView = findViewById(R.id.previous_first_place_Rating_number_text_view);
        previous_first_place_nickname_TextView = findViewById(R.id.previous_first_place_nickname_textView);
        previous_first_place_flag_ImageView = findViewById(R.id.previous_first_place_flag_imageView);

        previous_second_place_Rating_TextView = findViewById(R.id.previous_second_place_Rating_number_text_view);
        previous_second_place_nickname_TextView = findViewById(R.id.previous_second_place_nickname_textView);
        previous_second_place_flag_ImageView = findViewById(R.id.previous_second_place_flag_imageView);

        previous_third_place_Rating_TextView = findViewById(R.id.previous_third_place_Rating_number_text_view);
        previous_third_place_nickname_TextView = findViewById(R.id.previous_third_place_nickname_textView);
        previous_third_place_flag_ImageView = findViewById(R.id.previous_third_place_flag_imageView);
        getPreviousPlacesResponse();
        countdownTextView = findViewById(R.id.time_before_reset_textView);
        userTopRecyclerView = findViewById(R.id.top_users_RecyclerView);
        getTopUsersResponse();
    }

    private void getTopUsersResponse() {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetTopUsersResponse(new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                fillRecyclerView(parser.parseJsonUsersArray(jsonResponse));
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getPreviousPlacesResponse() {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPreviousTopUsersResponse(new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                fillPreviousTop(parser.parseJsonPreviousUsersArray(jsonResponse));
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void fillPreviousTop(ArrayList<User> usersList) {
        previous_first_place_Rating_TextView.setText(String.valueOf(usersList.get(0).getRating()));
        previous_first_place_nickname_TextView.setText(usersList.get(0).getNickname());
        switch (usersList.get(0).getLanguage_id()) {
            case 0:
                break;
            case 1:
                previous_first_place_flag_ImageView.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case 11:
                previous_first_place_flag_ImageView.setImageResource(R.drawable.ic_united_states);
                break;
            case 2:
                previous_first_place_flag_ImageView.setImageResource(R.drawable.ic_flag_russia);
                break;
        }
        previous_second_place_Rating_TextView.setText(String.valueOf(usersList.get(1).getRating()));
        previous_second_place_nickname_TextView.setText(usersList.get(1).getNickname());
        switch (usersList.get(1).getLanguage_id()) {
            case 0:
                break;
            case 1:
                previous_second_place_flag_ImageView.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case 11:
                previous_second_place_flag_ImageView.setImageResource(R.drawable.ic_united_states);
                break;
            case 2:
                previous_second_place_flag_ImageView.setImageResource(R.drawable.ic_flag_russia);
                break;
        }
        previous_third_place_Rating_TextView.setText(String.valueOf(usersList.get(2).getRating()));
        previous_third_place_nickname_TextView.setText(usersList.get(2).getNickname());
        switch (usersList.get(2).getLanguage_id()) {
            case 0:
                break;
            case 1:
                previous_third_place_flag_ImageView.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case 11:
                previous_third_place_flag_ImageView.setImageResource(R.drawable.ic_united_states);
                break;
            case 2:
                previous_third_place_flag_ImageView.setImageResource(R.drawable.ic_flag_russia);
                break;
        }
        }

    private void fillRecyclerView(ArrayList<User> usersList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        userTopRecyclerView.setLayoutManager(layoutManager);
        TopOfUsersAdapter myComPacksAdapter = new TopOfUsersAdapter(new TopOfUsersAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(User user, View v) {
                startActivity(new UserProfileActivity().createIntent(context, user.getId()));
            }
        });
        myComPacksAdapter.setUsersList(usersList);
        userTopRecyclerView.setAdapter(myComPacksAdapter);
        userTopRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public Intent createIntent(Context context) {
        return new Intent(context, TopOfUsersActivity.class);
    }
}
