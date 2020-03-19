package com.noadam.pushlearn.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreatePackFragment extends Fragment {
    private EditText enterTitleEditText;
    private EditText enterDescriptionEditText;
    private Spinner subdirectorySpinner;
    private Spinner directorySpinner;
    private ImageButton createPackNextImageButton;
    private String packName;
    private Context context;
    PushLearnDBHelper dbHelper;
    private ArrayList<Directory> directories = new ArrayList<>();
    private ArrayList<SubDirectory> subDirectories = new ArrayList<>();

    public void setBasePack(String packName) {
        this.packName = packName;
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

        enterTitleEditText = view.findViewById(R.id.enter_title_editText);
        enterTitleEditText.setText(packName);
        enterDescriptionEditText = view.findViewById(R.id.enter_description_editText);

        directorySpinner = view.findViewById(R.id.directory_spinner);
        getDirectoriesListResponse();


        subdirectorySpinner = view.findViewById(R.id.subdirectory_spinner);

        createPackNextImageButton = view.findViewById(R.id.create_pack_next_imageButton);
        createPackNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onClickPublishButton();
            }
        });

        return view;
    }

    private void getDirectoriesListResponse() {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoriesResponse(new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                directories = parseJsonDirectoriesArray(jsonResponse);
                ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(context,
                        R.layout.large_text_appereance_textview, directories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                directorySpinner.setAdapter(adapter);
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
            public void onError() {

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
        createPackResponse(title, description,  directory_id, subdirectory_id, hash);
        dbHelper.setPackTypeByName(packName,"owned");
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
            public void onError() {

            }
        });
    }

    private void getSubDirectoriesListByDirectoryIDResponse(int directory_id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetSubDirectoriesByDirectoryIDResponse(directory_id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                subDirectories = parseJsonSubDirectoriesArray(jsonResponse);
                ArrayAdapter<SubDirectory> adapter = new ArrayAdapter<SubDirectory>(context,
                        R.layout.large_text_appereance_textview, subDirectories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subdirectorySpinner.setAdapter(adapter);
            }
            @Override
            public void onError() {

            }
        });
    }

    private void createPackResponse(String packName, String description, int directory_id, int subdirectory_id,String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendCreatePackResponse(packName, description, directory_id, subdirectory_id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                int pack_id = Integer.valueOf(jsonResponse);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = prefs.getString("account_hash","");
                ArrayList<Card> cards = dbHelper.getCardListByPackName(packName,-1);
                for(Card card : cards) {
                    createCardResponse(pack_id, card.getQuestion(), card.getAnswer(), hash);
                }
                Toast.makeText(context, getString(R.string.successful_pack_publication), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError() {

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

    private ArrayList<Directory> parseJsonDirectoriesArray(String jsonResponse) {
        ArrayList<Directory> directories = new ArrayList<Directory>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int dirID = jsonObject.getInt("directory_id");
                String dirName = jsonObject.getString("directory");
                directories.add(new Directory(dirID,dirName));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return directories;
    }

    private ArrayList<SubDirectory> parseJsonSubDirectoriesArray(String jsonResponse) {
        ArrayList<SubDirectory> directories = new ArrayList<SubDirectory>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int subDirID = jsonObject.getInt("subdirectory_id");
                int dirID = jsonObject.getInt("directory_id");
                String dirName = jsonObject.getString("subdirectory");
                directories.add(new SubDirectory(subDirID, dirID, dirName));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return directories;
    }
}
