package com.noadam.pushlearn.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import java.util.ArrayList;

public class CreatePackFragment extends Fragment {
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

    public void setBasePack(String packName) {
        this.packName = packName;
    }

    public void setBaseComPack(ComPack comPack) {
        this.comPack = comPack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity();
        dbHelper = new PushLearnDBHelper(context);
        View view = inflater.inflate(R.layout.frag_create_pack_on_server, null);
        Toolbar toolbar = view.findViewById(R.id.create_pack_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.pack_publish);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MyPacksFragment());
            }
        });
        enterTitleEditText = view.findViewById(R.id.enter_title_editText);
        if(packName != null) {
            enterTitleEditText.setText(packName);
        }
        if(comPack != null) {
            enterTitleEditText.setText(comPack.getComPackName());
        }
        enterDescriptionEditText = view.findViewById(R.id.enter_description_editText);
        if(comPack != null) {
            enterDescriptionEditText.setText(comPack.getComPackDescription());
        }
        subdirectorySpinner = view.findViewById(R.id.subdirectory_spinner);
        directorySpinner = view.findViewById(R.id.directory_spinner);
        getDirectoriesListResponse(getLanguageId(getSystemLanguage()));


        ImageButton createPackNextImageButton = view.findViewById(R.id.create_pack_next_imageButton);
        createPackNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onClickPublishButton();
            }
        });

        return view;
    }

    private String getSystemLanguage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return String.valueOf(context.getResources().getConfiguration().getLocales().get(0)); }
        else { return String.valueOf(context.getResources().getConfiguration().locale); }
    }

    private int getLanguageId(String language) {
        if(language.contains("en_EN")) { return 1; }
        if(language.contains("en_US")) { return 11; }
        if(language.contains("ru_")) { return 2; }
        return 1;
    }


    private void getDirectoriesListResponse(int language_id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoriesResponse(language_id,new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                directories = parser.parseJsonDirectoriesArray(jsonResponse);
                ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(context,
                        R.layout.large_text_appereance_textview, directories);
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
        Intent intent =  getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fragment","my_profile");
        startActivity(intent);
   }

    private void createCardResponse(int id_pack, String question, String answer, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendCreateCardResponse(id_pack, question, answer, hash, new PushLearnServerCallBack() {
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
        response.sendGetSubDirectoriesByDirectoryIDResponse(directory_id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                subDirectories = parser.parseJsonSubDirectoriesArray(jsonResponse);
                ArrayAdapter<SubDirectory> adapter = new ArrayAdapter<SubDirectory>(context, R.layout.large_text_appereance_textview, subDirectories);
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
        response.sendCreatePackResponse(packName, description, directory_id, subdirectory_id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                int pack_id = Integer.valueOf(jsonResponse);
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
        response.sendUpdatePackResponse(pack_id, packName, description, directory_id, subdirectory_id, hash, new PushLearnServerCallBack() {
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
