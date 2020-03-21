package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ComPacksFragment extends Fragment {

    private Context context;
    private EditText enterPackNameEditText;
    private EditText enterDescriptionEditText;
    private Spinner subdirectorySpinner;
    private Spinner directorySpinner;
    private ImageButton searchImageButton;
    private CheckBox searchInDescriptionCheckBox;
    private RecyclerView searchedPacksRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frag_community_packs, null);
        context = getActivity();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = view.findViewById(R.id.com_packs_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.pushlearn_community);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        enterPackNameEditText = view.findViewById(R.id.pack_name_EditText);
        directorySpinner = view.findViewById(R.id.directory_compacks_spinner);
        subdirectorySpinner = view.findViewById(R.id.subdirectory_compacks_spinner);

        searchInDescriptionCheckBox = view.findViewById(R.id.search_in_pack_description_checkBox);
        getDirectoriesListResponse();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        ImageButton searchImageButton = view.findViewById(R.id.search_comPacks_imageButton);
        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        searchedPacksRecyclerView = view.findViewById(R.id.com_packs_RecyclerView);

//        fillRecyclerView();

        return view;
    }


    private void getDirectoriesListResponse() {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoriesResponse(new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
               ArrayList<Directory> directories = parseJsonDirectoriesArray(jsonResponse);
                ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(context,
                        R.layout.large_white_text_appereance_textview, directories);
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
            public void onError(Throwable t) {

            }
        });
    }

    private void getSubDirectoriesListByDirectoryIDResponse(int directory_id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetSubDirectoriesByDirectoryIDResponse(directory_id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ArrayList<SubDirectory> subDirectories = parseJsonSubDirectoriesArray(jsonResponse);
                ArrayAdapter<SubDirectory> adapter = new ArrayAdapter<SubDirectory>(context,
                        R.layout.large_white_text_appereance_textview, subDirectories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subdirectorySpinner.setAdapter(adapter);
            }
            @Override
            public void onError(Throwable t) {

            }
        });
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
