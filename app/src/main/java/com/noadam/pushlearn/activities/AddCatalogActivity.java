package com.noadam.pushlearn.activities;


import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.internet.PushLearnServerStringCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

public class AddCatalogActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    
    private String catalog;
    private int directoryIDForSubdirectory;

    private TextInputLayout titleTextInputLayout;
    private CheckBox acceptPLRulesCheckBox;
    private TextView onlyPremiumCanAddCatsLabelTextView;
    private Button addCatButton;
    
    public static final String CATALOG_TYPE = "CATALOG_TYPE";
    public static final String CATEGORY = "CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("theme", "Light").equals("Light")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.DarkTheme);
        }
        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catalog);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //-----------------------------------INTENT UNPACKING----------------------------------------------------
        Intent intent = getIntent();
        catalog = intent.getStringExtra(CATALOG_TYPE);
        directoryIDForSubdirectory = intent.getIntExtra(CATEGORY,1);
        //------------------------------------ACTION BAR----------------------------------------------------
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.add_cat_toolbar);
        setSupportActionBar(mActionBarToolbar);
        if (catalog.equals(CATEGORY)) {
            getSupportActionBar().setTitle(R.string.add_category);
        } else {
            getSupportActionBar().setTitle(getString(R.string.add_subcategory));
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
        titleTextInputLayout = findViewById(R.id.title_input_layout);
        addCatButton = findViewById(R.id.add_cat_button);
        addCatButton.setClickable(false);
        addCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryButtonClicked();
            }
        });
        acceptPLRulesCheckBox = findViewById(R.id.accept_PL_rules_checkBox);
        acceptPLRulesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addCatButton.setClickable(true);
                } else {
                    addCatButton.setClickable(false);
                }
            }
        });
        onlyPremiumCanAddCatsLabelTextView = findViewById(R.id.only_premium_can_add_label_textView);
        onlyPremiumCanAddCatsLabelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new SubscribeActivity().createIntent(getApplicationContext(), 4));
            }
        });
        String hash = prefs.getString("account_hash","");
        getPremiumHashResponse(hash);

    }

    private void addCategoryButtonClicked() {
        String hash = prefs.getString("account_hash","");
        if(!validateTitle()) {
            return;
        }
        if (catalog.equals(CATEGORY)) {
            addCategoryResponse(titleTextInputLayout.getEditText().getText().toString(), hash);
        } else {
            addSubCategoryResponse(titleTextInputLayout.getEditText().getText().toString(), directoryIDForSubdirectory, hash);
        }
    }

    private boolean validateTitle() {
        String usernameInput = titleTextInputLayout.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            titleTextInputLayout.setError(getString(R.string.empty_field));
            return false;
        } else if (usernameInput.length() > 15) {
            titleTextInputLayout.setError(getString(R.string.username_too_long));
            return false;
        } else {
            titleTextInputLayout.setError(null);
            return true;
        }
    }

    public Intent createIntent(Context context, String catalogType, int directory_id) {
        return new Intent(context, AddCatalogActivity.class)
                .putExtra(CATALOG_TYPE, catalogType)
                .putExtra(CATEGORY, directory_id);
    }

    private void addCategoryResponse(String directory, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(this);
        response.sendCreateDirectoryByHashResponse(directory, hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String response) {
                if(response.equals("verify")) {
                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.directory_sent_to_verification), directory), Toast.LENGTH_LONG).show();
                }
                if(response.equals("added")) {
                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.directory_was_added), directory), Toast.LENGTH_LONG).show();
                }
                finish();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void addSubCategoryResponse(String subdirectory,int directory_id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(this);
        response.sendCreateSubDirectoryByHashResponse(subdirectory, directory_id, hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String response) {
                if(response.equals("verify")) {
                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.subdirectory_sent_to_verification), subdirectory), Toast.LENGTH_LONG).show();
                }
                if(response.equals("added")) {
                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.subdirectory_was_added), subdirectory), Toast.LENGTH_LONG).show();
                }
                finish();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getPremiumHashResponse(String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(this);
        response.sendGetPremiumByHashResponse(hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String premium) {
                if(Integer.parseInt(premium) > 0) {
                    onlyPremiumCanAddCatsLabelTextView.setText(getString(R.string.only_premiun_users_can_add_categories));
                    onlyPremiumCanAddCatsLabelTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), getString(R.string.you_are_happy_premium_user), Toast.LENGTH_LONG).show();
                        }
                    });
                    addCatButton.setText(getString(R.string.add_title));
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
