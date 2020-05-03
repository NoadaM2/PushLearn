package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.noadam.pushlearn.activities.AddCatalogActivity;
import com.noadam.pushlearn.activities.CommunityPackActivity;
import com.noadam.pushlearn.activities.TopOfUsersActivity;
import com.noadam.pushlearn.adapters.ComPacksAdapter;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.EndLessRecyclerView;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.internet.PushLearnServerDirectoriesListCallBack;
import com.noadam.pushlearn.internet.PushLearnServerStringCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

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
        getDirectoriesListResponse(getLanguageId(getSystemLanguage()));
        searchInDescriptionCheckBox = view.findViewById(R.id.search_in_pack_description_checkBox);
        searchInDescriptionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentPage = 0;
                comPacksAdapter.clearItems();
                add25Packs();
            }
        });
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
        ImageButton add_category_imageButton = view.findViewById(R.id.add_category_imageButton);
        add_category_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new AddCatalogActivity().createIntent(context, AddCatalogActivity.CATEGORY,0));
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        ImageButton add_subcategory_imageButton = view.findViewById(R.id.add_subcategory_imageButton);
        add_subcategory_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Directory directory = (Directory) directorySpinner.getSelectedItem();
                startActivity(new AddCatalogActivity().createIntent(context, "subcategory",directory.getId()));
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        searchedPacksRecyclerView = view.findViewById(R.id.com_packs_RecyclerView);
        searchedPacksRecyclerView.setOnLoadMoreListener(this);
        setRecyclerViewAdapter();


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

    private void add25Packs() {
        if(!searchInDescriptionCheckBox.isChecked()) {
            if (enableDirectorySearch.isChecked()) {
                Directory directory = (Directory) directorySpinner.getSelectedItem();
                int directoryID = directory.getId();
                if (enableSubDirectorySearch.isChecked()) {
                    SubDirectory subDirectory = (SubDirectory) subdirectorySpinner.getSelectedItem();
                    int subdirectory_id = subDirectory.getId();
                    getPacksByDirectoryIdAndSubDirectoryIdAndPackName(directoryID, subdirectory_id, String.valueOf(enterPackNameEditText.getText()));
                } else {
                    getPacksByDirectoryIdAndPackName(directoryID, String.valueOf(enterPackNameEditText.getText()));
                }
            } else {
                getPacksByPackName(String.valueOf(enterPackNameEditText.getText()));
            }
        } else {
            if (enableDirectorySearch.isChecked()) {
                Directory directory = (Directory) directorySpinner.getSelectedItem();
                int directoryID = directory.getId();
                if (enableSubDirectorySearch.isChecked()) {
                    SubDirectory subDirectory = (SubDirectory) subdirectorySpinner.getSelectedItem();
                    int subdirectory_id = subDirectory.getId();
                    getPacksByDirectoryIdAndSubDirectoryIdAndPackNameAndDescription(directoryID, subdirectory_id, String.valueOf(enterPackNameEditText.getText()));
                } else {
                    getPacksByDirectoryIdAndPackNameAndDescription(directoryID, String.valueOf(enterPackNameEditText.getText()));
                }
            } else {
                getPacksByPackNameAndDescription(String.valueOf(enterPackNameEditText.getText()));
            }
        }
    }

    private void setRecyclerViewAdapter() {

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //searchedPacksRecyclerView.setLayoutManager(layoutManager);
        comPacksAdapter = new ComPacksAdapter(context, new ComPacksAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(ComPack myComPack, View v) {
                context.startActivity(new CommunityPackActivity().createIntent(context, myComPack));
            }
        });
        searchedPacksRecyclerView.setAdapter(comPacksAdapter);
    }

    private void getDirectoriesListResponse(int language_id) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetDirectoriesResponse(language_id, new PushLearnServerDirectoriesListCallBack() {
            @Override
            public void onResponse(ArrayList<Directory> directories) {
                ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(context,
                R.layout.large_white_text_appereance_textview, directories);
                adapter.setDropDownViewResource(R.layout.simple_spinner_my_item);
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
        response.sendGetSubDirectoriesByDirectoryIDResponse(directory_id, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                ArrayList<SubDirectory> subDirectories = parser.parseJsonSubDirectoriesArray(jsonResponse);
                ArrayAdapter<SubDirectory> adapter = new ArrayAdapter<SubDirectory>(context,
                        R.layout.large_white_text_appereance_textview, subDirectories);
                adapter.setDropDownViewResource(R.layout.simple_spinner_my_item);
                subdirectorySpinner.setAdapter(adapter);
                subdirectorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SubDirectory subDirectory = (SubDirectory) parent.getSelectedItem();
                        currentPage = 0;
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
        response.sendGetPacksByPackNameResponse(packName, currentPage,new PushLearnServerStringCallBack() {
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
        response.sendGetPacksByDirectoryIdAndPackNameResponse(packName, directory_id, currentPage, new PushLearnServerStringCallBack() {
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

    private void getPacksByDirectoryIdAndSubDirectoryIdAndPackName(int directory_id, int subdirectory_id, String packName) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByDirectoryIdAndSubDirectoryIdAndPackNameResponse(packName, directory_id, subdirectory_id , currentPage, new PushLearnServerStringCallBack() {
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

    private void getPacksByPackNameAndDescription(String packName) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByPackNameAndDescriptionResponse(packName, packName, currentPage,new PushLearnServerStringCallBack() {
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

    private void getPacksByDirectoryIdAndPackNameAndDescription(int directory_id, String packName) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByDirectoryIdAndPackNameAndDescriptionResponse(packName, packName, directory_id, currentPage, new PushLearnServerStringCallBack() {
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

    private void getPacksByDirectoryIdAndSubDirectoryIdAndPackNameAndDescription(int directory_id, int subdirectory_id, String packName) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByDirectoryIdAndSubDirectoryIdAndPackNameResponse(packName, packName, directory_id, subdirectory_id , currentPage, new PushLearnServerStringCallBack() {
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_for_community_packs, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_for_com_packs_trophy:
                context.startActivity(new TopOfUsersActivity().createIntent(context));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        add25Packs();
    }
}
