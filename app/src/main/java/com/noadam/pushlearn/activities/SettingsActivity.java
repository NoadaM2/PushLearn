package com.noadam.pushlearn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;
import com.noadam.pushlearn.fragments.dialog.settings.SetMaxNumberOfNotifiesInBarDialogFragment;
import com.noadam.pushlearn.fragments.dialog.settings.SetTimePeriodDialogFragment;

public class SettingsActivity extends AppCompatActivity {

    private TextView number_of_notifies_in_bar_summary_textView;
    private TextView time_period_between_notifies_summary_textView;
    private SharedPreferences prefs;
    public static final int LIMIT_NOTIFIES_IN_BAR_DIALOG_RESULT = 1;
    public static final int TIME_BETWEEN_NOTIFIES_DIALOG_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getString("theme","Light").equals("Light")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.DarkTheme);
        }
        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

        }
        setContentView(R.layout.activity_preferences);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //------------------------------------ACTION BAR----------------------------------------------------
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.pushlearn_settings));
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
        TextView titleTextView = findViewById(R.id.title_settings_textView);
        SpannableString s2 = new SpannableString(getString(R.string.pushlearn_settings));
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s2.setSpan(new RelativeSizeSpan(2), 0, s2.length(), flag);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s2);
        titleTextView.setText(builder);
        //....................................................................................................
        int count_notifies_in_bar = prefs.getInt("number_of_notifies_in_bar",3);
        TextView number_of_notifies_in_bar_title_textView = findViewById(R.id.number_of_notifies_in_bar_title_textView);
        number_of_notifies_in_bar_title_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMaxNumberOfNotifiesInBarDialogFragment dialogFrag = SetMaxNumberOfNotifiesInBarDialogFragment.newInstance(count_notifies_in_bar);
                dialogFrag.show(getFragmentManager(),"");
            }
        });
        //....................................................................................................
        number_of_notifies_in_bar_summary_textView = findViewById(R.id.number_of_notifies_in_bar_summary_textView);
        number_of_notifies_in_bar_summary_textView.setText(String.valueOf(count_notifies_in_bar));
        number_of_notifies_in_bar_summary_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMaxNumberOfNotifiesInBarDialogFragment dialogFrag = SetMaxNumberOfNotifiesInBarDialogFragment.newInstance(count_notifies_in_bar);
                dialogFrag.show(getFragmentManager(),"");
            }
        });
        //....................................................................................................
        int minutesFull = prefs.getInt("minutesBetweenNotifies",1);
        TextView time_period_between_notifies_title_textView = findViewById(R.id.time_period_between_notifies_title_textView);
        time_period_between_notifies_title_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetTimePeriodDialogFragment dialogFrag = SetTimePeriodDialogFragment.newInstance(minutesFull);
                dialogFrag.show(getFragmentManager(), "");
            }
        });
        //....................................................................................................
        int hours = Math.round(minutesFull / 60);
        int minutes = minutesFull % 60;
        time_period_between_notifies_summary_textView = findViewById(R.id.time_period_between_notifies_summary_textView);
        time_period_between_notifies_summary_textView.setText(String.valueOf(hours)+":"+String.format("%02d", minutes));
        time_period_between_notifies_summary_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetTimePeriodDialogFragment dialogFrag = SetTimePeriodDialogFragment.newInstance(minutesFull);
                dialogFrag.show(getFragmentManager(), "");
            }
        });
        //....................................................................................................
        TextView disable_advertisement_textView = findViewById(R.id.disable_advertisement_textView);
        disable_advertisement_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new SubscribeActivity().createIntent(getApplicationContext(), 8));
            }
        });
        //....................................................................................................
        TextView log_out_title_textView = findViewById(R.id.log_out_title_textView);
        log_out_title_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteConfirmationDialogFragment dialogFrag = new DeleteConfirmationDialogFragment();
                dialogFrag.show(getFragmentManager().beginTransaction(), DeleteConfirmationDialogFragment.LOG_OUT);
            }
        });
        //....................................................................................................
        TextView log_out_summary_textView = findViewById(R.id.log_out_summary_textView);
        log_out_summary_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteConfirmationDialogFragment dialogFrag = new DeleteConfirmationDialogFragment();
                dialogFrag.show(getFragmentManager().beginTransaction(), DeleteConfirmationDialogFragment.LOG_OUT);
            }
        });
        //....................................................................................................
        Switch theme_switch = findViewById(R.id.theme_switch);
        if(prefs.getString("theme","Light").equals("Light")) {
            theme_switch.setChecked(false);
        } else {
            theme_switch.setChecked(true);
        }
        theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putString("theme", "Dark");
                } else {
                    editor.putString("theme", "Light");
                }
                setResult(RESULT_OK, null);
                restartActivity();
                editor.apply();
            }
        });
    }

    private void restartActivity() {
        startActivity(new SettingsActivity().createIntent(getApplicationContext()));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            switch (resultCode) {
                case LIMIT_NOTIFIES_IN_BAR_DIALOG_RESULT:
                        int count_notifies_in_bar = prefs.getInt("number_of_notifies_in_bar", 3);
                        number_of_notifies_in_bar_summary_textView.setText(String.valueOf(count_notifies_in_bar));
                    break;
                    case TIME_BETWEEN_NOTIFIES_DIALOG_RESULT:
                        int minutesFull = prefs.getInt("minutesBetweenNotifies",1);
                        int hours = Math.round(minutesFull / 60);
                        int minutes = minutesFull % 60;
                        time_period_between_notifies_summary_textView.setText(String.valueOf(hours)+":"+String.format("%02d", minutes));
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);

    }
}
