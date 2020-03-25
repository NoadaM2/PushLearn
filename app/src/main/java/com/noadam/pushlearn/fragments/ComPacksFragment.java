package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.ComPacksAdapter;
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.EndLessRecyclerView;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ComPacksFragment extends Fragment implements EndLessRecyclerView.OnLoadMoreListener {

    private Context context;
    private EditText enterPackNameEditText;
    private CheckBox enableDirectorySearch;
    private CheckBox enableSubDirectorySearch;
    private Spinner subdirectorySpinner;
    private Spinner directorySpinner;
    private ImageButton searchImageButton;
    private CheckBox searchInDescriptionCheckBox;
    private EndLessRecyclerView searchedPacksRecyclerView;
    private TextView noComPacksFoundTextView;
    private ComPacksAdapter comPacksAdapter;
    private int currentPage = 0;

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
        enterPackNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentPage = 0;
                comPacksAdapter.clearItems();
                add25Packs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        directorySpinner = view.findViewById(R.id.directory_compacks_spinner);
        subdirectorySpinner = view.findViewById(R.id.subdirectory_compacks_spinner);
        getDirectoriesListResponse();
        searchInDescriptionCheckBox = view.findViewById(R.id.search_in_pack_description_checkBox);
        noComPacksFoundTextView = view.findViewById(R.id.no_packs_by_search_textView);


        enableSubDirectorySearch = view.findViewById(R.id.subdirectory_label_compacks_CheckBox);
        enableSubDirectorySearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(enableDirectorySearch.isChecked()) {
                    currentPage = 0;
                    comPacksAdapter.clearItems();
                    add25Packs();
                } else {
                    enableDirectorySearch.setChecked(true);
                }
            }
        });
        enableDirectorySearch = view.findViewById(R.id.directory_label_compacks_CheckBox);
        enableDirectorySearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentPage = 0;
                comPacksAdapter.clearItems();
                add25Packs();
                if(!isChecked) {
                    enableSubDirectorySearch.setChecked(false);
                }
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        ImageButton searchImageButton = view.findViewById(R.id.search_comPacks_imageButton);
        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        searchedPacksRecyclerView = view.findViewById(R.id.com_packs_RecyclerView);
        searchedPacksRecyclerView.setOnLoadMoreListener(this);
        setRecyclerViewAdapter();


        return view;
    }

    private void add25Packs() {
        if(enableDirectorySearch.isChecked()) {
            Directory directory = (Directory) directorySpinner.getSelectedItem();
            int directoryID = directory.getId();
            if(enableSubDirectorySearch.isChecked()) {
                SubDirectory subDirectory = (SubDirectory) subdirectorySpinner.getSelectedItem();
                int subdirectory_id = subDirectory.getId();
                getPacksByDirectoryIdAndSubDirectoryIdAndPackName(directoryID, subdirectory_id, String.valueOf(enterPackNameEditText.getText()));
            } else {
                getPacksByDirectoryIdAndPackName(directoryID, String.valueOf(enterPackNameEditText.getText()));
            }
        } else {
            getPacksByPackName(String.valueOf(enterPackNameEditText.getText()));
        }
    }

    private void setRecyclerViewAdapter() {

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //searchedPacksRecyclerView.setLayoutManager(layoutManager);
        comPacksAdapter = new ComPacksAdapter(context, new ComPacksAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(ComPack myComPack, View v) {
                CommunityPackFragment nextFrag= new CommunityPackFragment();
                nextFrag.setComPack(myComPack);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        searchedPacksRecyclerView.setAdapter(comPacksAdapter);
    }

    private void getDirectoriesListResponse() {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoriesResponse(new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
               ArrayList<Directory> directories = parser.parseJsonDirectoriesArray(jsonResponse);
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
                ParserFromJSON parser = new ParserFromJSON();
                ArrayList<SubDirectory> subDirectories = parser.parseJsonSubDirectoriesArray(jsonResponse);
                ArrayAdapter<SubDirectory> adapter = new ArrayAdapter<SubDirectory>(context,
                        R.layout.large_white_text_appereance_textview, subDirectories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subdirectorySpinner.setAdapter(adapter);
                subdirectorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SubDirectory subDirectory = (SubDirectory) parent.getSelectedItem();
                        comPacksAdapter.clearItems();
                        add25Packs();
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

    private void getPacksByPackName(String packName) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByPackNameResponse(packName, currentPage,new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                comPacksAdapter.addItem(parser.parseJsonComPacksArray(jsonResponse));
                comPacksAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getPacksByDirectoryIdAndPackName(int directory_id, String packName) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByDirectoryIdAndPackNameResponse(packName, directory_id, currentPage, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                comPacksAdapter.addItem(parser.parseJsonComPacksArray(jsonResponse));
                comPacksAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getPacksByDirectoryIdAndSubDirectoryIdAndPackName(int directory_id, int subdirectory_id, String packName) { // TODO Test here
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByDirectoryIdAndSubDirectoryIdAndPackNameResponse(packName, directory_id, subdirectory_id , currentPage, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                comPacksAdapter.addItem(parser.parseJsonComPacksArray(jsonResponse));
                comPacksAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        add25Packs();
    }
}
