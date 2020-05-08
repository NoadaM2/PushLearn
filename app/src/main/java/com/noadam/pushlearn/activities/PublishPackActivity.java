package com.noadam.pushlearn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.internet.PushLearnServerDirectoriesListCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;
import com.noadam.pushlearn.internet.PushLearnServerStringCallBack;
import com.noadam.pushlearn.logic.ParserFromJSON;
import com.noadam.pushlearn.logic.SystemLanguageGetter;

import java.util.ArrayList;

public class PublishPackActivity extends AppCompatActivity {
    private EditText enterTitleEditText;
    private EditText enterDescriptionEditText;
    private Spinner subdirectorySpinner;
    private Spinner directorySpinner;
    private String packName;
    private ComPack comPack;
    private Context context;
    PushLearnDBHelper dbHelper;
    private ArrayList<Directory> directories = new ArrayList<>();
    private ArrayList<SubDirectory> subDirectories = new ArrayList<>();
    private SharedPreferences prefs;
    public static final String PACK_NAME = "packName";
    public static final String COM_PACK = "comPack";

    private void ThemeSwitch() {
        if(prefs.getString("theme","Light").equals("Light")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.DarkTheme);
        }
    }

    private void ParamsInitialization() {
        context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dbHelper = new PushLearnDBHelper(context);
    }

    private void toolbarSetUp(int toolbarLayout) {
        Toolbar mActionBarToolbar = (Toolbar) findViewById(toolbarLayout);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.pack_publish);
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

    private void unpackIntent() {
        Intent intent = getIntent();
        comPack = ComPack.fromJson(intent.getStringExtra(COM_PACK));
        packName = intent.getStringExtra(PACK_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParamsInitialization();
        ThemeSwitch();
        //------------------------------------LAYOUT INITIALIZATION----------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_create_pack_on_server);
        toolbarSetUp(R.id.create_pack_toolbar);
        unpackIntent();
        enterTitleEditText = findViewById(R.id.enter_title_editText);
        if(packName != null) {
            enterTitleEditText.setText(packName);
        }
        if(comPack != null) {
            enterTitleEditText.setText(comPack.getComPackName());
        }
        enterDescriptionEditText = findViewById(R.id.enter_description_editText);
        if(comPack != null) {
            enterDescriptionEditText.setText(comPack.getComPackDescription());
        }
        subdirectorySpinner = findViewById(R.id.subdirectory_spinner);
        directorySpinner = findViewById(R.id.directory_spinner);
        SystemLanguageGetter languageGetter = new SystemLanguageGetter(context);
        getDirectoriesListResponse(languageGetter.get());

        ImageButton createPackNextImageButton = findViewById(R.id.create_pack_next_imageButton);
        createPackNextImageButton.setClickable(false);

        CheckBox acceptPLRulesCheckBox = findViewById(R.id.accept_PL_rules_in_create_checkBox);
        acceptPLRulesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    createPackNextImageButton.setClickable(true);
                } else {
                    createPackNextImageButton.setClickable(false);
                }
            }
        });

        createPackNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acceptPLRulesCheckBox.isChecked()) {
                    onClickPublishButton();
                }
            }
        });
    }

    private void getDirectoriesListResponse(int language_id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoriesResponse(language_id,new PushLearnServerDirectoriesListCallBack() {
            @Override
            public void onResponse(ArrayList<Directory> directories) {
                ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(context,
                        R.layout.large_white_text_appereance_textview, directories);
                adapter.setDropDownViewResource(R.layout.simple_spinner_my_item);
                directorySpinner.setAdapter(adapter);
                if(comPack != null) {
                    for (Directory subDir : directories) {
                        if (subDir.getId() == comPack.getComPackDirectoryId()) {
                            directorySpinner.setSelection(directories.indexOf(subDir));
                        }
                    }
                }
               directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Directory directory = (Directory) parent.getSelectedItem();
                        getSubDirectoriesListByDirectoryIDResponse(directory.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void onClickPublishButton() {
        String title = String.valueOf(enterTitleEditText.getText());
        String description = String.valueOf(enterDescriptionEditText.getText());
        Directory directory = (Directory) directorySpinner.getSelectedItem();
        int directory_id = directory.getId();
        SubDirectory subdirectory = (SubDirectory)subdirectorySpinner.getSelectedItem();
        int subdirectory_id = subdirectory.getId();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash = prefs.getString("account_hash","");
        if(packName != null) {
            createPackResponse(title, description, directory_id, subdirectory_id, hash);
            dbHelper.setPackTypeByName(packName, "owned");
        }
        if(comPack != null) {
            updatePackResponse(comPack.getComPackID(),title, description, directory_id, subdirectory_id, hash);
        }
       finish();
    }

    private void createCardResponse(int id_pack, String question, String answer, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendCreateCardResponse(id_pack, question, answer, hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getSubDirectoriesListByDirectoryIDResponse(int directory_id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetSubDirectoriesByDirectoryIDResponse(directory_id, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                subDirectories = parser.parseJsonSubDirectoriesArray(jsonResponse);
                ArrayAdapter<SubDirectory> adapter = new ArrayAdapter<SubDirectory>(context,
                        R.layout.large_white_text_appereance_textview, subDirectories);
                adapter.setDropDownViewResource(R.layout.simple_spinner_my_item);
                subdirectorySpinner.setAdapter(adapter);
                if(comPack != null) {
                    for (SubDirectory subDir : subDirectories) {
                        if (subDir.getId() == comPack.getComPackSubdirectoryId()) {
                            subdirectorySpinner.setSelection(subDirectories.indexOf(subDir));
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void createPackResponse(String packName, String description, int directory_id, int subdirectory_id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendCreatePackResponse(packName, description, directory_id, subdirectory_id, hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                int pack_id = Integer.parseInt(jsonResponse);
                ArrayList<Card> cards = dbHelper.getCardListByPackName(packName,-1);
                for(Card card : cards) {
                    createCardResponse(pack_id, card.getQuestion(), card.getAnswer(), hash);
                }
                Toast.makeText(context, getString(R.string.successful_pack_publication), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void updatePackResponse(int pack_id,String packName, String description, int directory_id, int subdirectory_id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendUpdatePackResponse(pack_id, packName, description, directory_id, subdirectory_id, hash, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String response) {
                if(response.equals("ok")) {
                    Toast.makeText(context, getString(R.string.successful_pack_update), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public Intent createIntent(Context context, ComPack comPack) {
        return new Intent(context, PublishPackActivity.class)
                .putExtra(COM_PACK, comPack.toJson());
    }

    public Intent createIntent(Context context, String packName) {
        return new Intent(context, PublishPackActivity.class)
                .putExtra(PACK_NAME, packName);
    }
}
