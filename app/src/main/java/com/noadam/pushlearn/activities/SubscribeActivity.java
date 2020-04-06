package com.noadam.pushlearn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.CompoundButtonCompat;

import com.noadam.pushlearn.R;

public class SubscribeActivity extends AppCompatActivity {

    public final String FEATURE = "feature";
    private int feature = 0;
    private CheckBox firstFeatureCheckBox;
    private CheckBox secondFeatureCheckBox;
    private CheckBox thirdFeatureCheckBox;
    private CheckBox fourthFeatureCheckBox;
    private CheckBox fifthFeatureCheckBox;
    private CheckBox sixthFeatureCheckBox;
    private CheckBox seventhFeatureCheckBox;
    private CheckBox eighthFeatureCheckBox;

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
        if (savedInstanceState != null) {

        }
        setContentView(R.layout.activity_subscribe);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //-----------------------------------INTENT UNPACKING------------------------------------------------
        Intent intent = getIntent();
        feature = intent.getIntExtra(FEATURE,0);
        //------------------------------------ACTION BAR----------------------------------------------------
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.subscribe_activity_toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.pushlearn_premium));
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
        TextView titleTextView = findViewById(R.id.pushlearn_premium_header_textView);
        SpannableString s1 = new SpannableString(getString(R.string.discover_new_opportunities_with));
        SpannableString s2 = new SpannableString(getString(R.string.pushlearn_premium));
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s1.setSpan(new StyleSpan(Typeface.NORMAL), 0, s1.length(), flag);
        s2.setSpan(new RelativeSizeSpan(2), 0, s2.length(), flag);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(s2);
        titleTextView.setText(builder);
        //....................................................................................................
        Button subscribe_1_month_button = findViewById(R.id.subscribe_1_month_button);
        s1 = new SpannableString(getString(R.string.pay_monthly));
        s2 = new SpannableString(getString(R.string.price_for_1_month));
        s2.setSpan(new RelativeSizeSpan(2), 0, s2.length(), flag);
        builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(s2);
        subscribe_1_month_button.setText(builder);
        //....................................................................................................
        Button subscribe_3_months_button = findViewById(R.id.subscribe_3_months_button);
        s1 = new SpannableString(getString(R.string.pay_once_three_months));
        s2 = new SpannableString(getString(R.string.price_for_3_months));
        SpannableString s3 = new SpannableString(getString(R.string.price_for_1_month_3_months_subscribe));
        s2.setSpan(new RelativeSizeSpan(2), 0, s2.length(), flag);
        builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(s2);
        builder.append(s3);
        subscribe_3_months_button.setText(builder);
        //....................................................................................................
        firstFeatureCheckBox = findViewById(R.id.first_feature_checkBox);
        secondFeatureCheckBox = findViewById(R.id.second_feature_checkBox);
        thirdFeatureCheckBox = findViewById(R.id.third_feature_checkBox);
        fourthFeatureCheckBox = findViewById(R.id.fourth_feature_checkBox);
        fifthFeatureCheckBox = findViewById(R.id.fifth_feature_checkBox);
        sixthFeatureCheckBox = findViewById(R.id.sixth_feature_checkBox);
        seventhFeatureCheckBox = findViewById(R.id.seventh_feature_checkBox);
        eighthFeatureCheckBox = findViewById(R.id.eighth_feature_checkBox);
        animateCheckBoxes();
    }

    public Intent createIntent(Context context, int feature) {
        return new Intent(context, SubscribeActivity.class)
                .putExtra(FEATURE, feature);

    }

    private void animateCheckBoxes() {
                switch (feature) {
                    case 1:
                            firstFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                            firstFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                            firstFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 2:
                            secondFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                        secondFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        secondFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 3:
                            thirdFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                        thirdFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        thirdFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 4:
                            fourthFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                        fourthFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        fourthFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 5:
                            fifthFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                        fifthFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        fifthFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 6:
                            sixthFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                        sixthFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        sixthFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 7:
                            seventhFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                        seventhFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        seventhFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                        case 8:
                            eighthFeatureCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                            eighthFeatureCheckBox.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                            eighthFeatureCheckBox.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                }
    }
}
