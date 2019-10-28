package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.PackListAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.fragments.dialog.CreatePackDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class MyPacksFragment extends Fragment{

    private RecyclerView packListRecyclerView;
    private PackListAdapter packListAdapter;
    private PushLearnDBHelper dbHelper;
    private Toolbar toolbar;
    private Toolbar selectionToolbar;
    private Context context;
    private TextView textViewNoPacks;
    private List<Pack> packList;
    private String packLongClicked;
    private ArrayList<String> selectedPacks = new ArrayList<>();
    private String mode;
    private View longPressedView;
    private MenuItem shareSelectedItemsMenuItem;
    private MenuItem deleteSelectedItemsMenuItem;
    private MenuItem createPackMenuItem;
    private MenuItem searchPackMenuItem;

    final int MENU_SELECT = 1;
    final int MENU_SHARE = 2;
    final int MENU_EDIT = 3;
    final int MENU_DELETE = 4;

    private void fillRecyclerView()
    {
        packList = dbHelper.getPackList();
        if (!packList.isEmpty()) {
            textViewNoPacks.setVisibility(View.GONE);
        }
        else {
            textViewNoPacks.setText(R.string.no_packs);
            textViewNoPacks.setVisibility(View.VISIBLE);
        }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            packListRecyclerView.setLayoutManager(layoutManager);
            packListAdapter = new PackListAdapter(new PackListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onClick(String packName, View v) {
                    if (mode == "selection") {
                        if (!selectedPacks.contains(packName)) {
                            // view selected
                            selectedPacks.add(packName);
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                        } else {
                            // view reselected
                            selectedPacks.remove(packName);
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.white_gray));
                        }

                        // we do not notify that an item has been selected
                        // because that work is done here.  we instead send
                        // notifications for items to be deselected
                    }
                    else {
                        onRecyclerViewItemClick(packName);
                    }
                }
            }, new PackListAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public void onLongClick(String packName, View v) {
                    packLongClicked = packName;
                    longPressedView = v;
                }
            });
            packListAdapter.setPackList(packList);
            packListRecyclerView.setAdapter(packListAdapter);
            packListRecyclerView.getAdapter().notifyDataSetChanged();
        }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_SELECT, 1, R.string.select);
        menu.add(0, MENU_SHARE, 2, R.string.share);
        menu.add(0, MENU_EDIT, 3, R.string.edit);
        menu.add(0, MENU_DELETE, 4, R.string.delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SELECT:
                selectedPacks.add(packLongClicked);
                mode = "selection";
                createPackMenuItem.setVisible(false);
                searchPackMenuItem.setVisible(false);
                shareSelectedItemsMenuItem.setVisible(true);
                deleteSelectedItemsMenuItem.setVisible(true);
                longPressedView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                break;
            case MENU_SHARE: // TODO SHARING PACKS

                break;
            case MENU_EDIT:
                CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance(packLongClicked);
                dialogFrag.setTargetFragment(this, 2);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                break;
            case MENU_DELETE:
                if (packLongClicked != ""){
                    dbHelper.deletePackByPackName(packLongClicked);
                }
                fillRecyclerView();
                break;
        }
        return super.onContextItemSelected(item);
        }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = container.getContext();
        dbHelper = new PushLearnDBHelper(context);
        View view = inflater.inflate(R.layout.frag_my_packs, null);
        toolbar = view.findViewById(R.id.mypacks_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        textViewNoPacks = view.findViewById(R.id.no_items_textview);
        packListRecyclerView = view.findViewById(R.id.pack_list_recyclerview);
        registerForContextMenu(packListRecyclerView);
        fillRecyclerView();
        return view;
    }
    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_my_packs, menu);
        createPackMenuItem = menu.findItem(R.id.menu_activity_pack_create_item);
        searchPackMenuItem = menu.findItem(R.id.menu_activity_pack_search);
        shareSelectedItemsMenuItem = menu.findItem(R.id.menu_activity_selected_pack_share);
        deleteSelectedItemsMenuItem = menu.findItem(R.id.menu_activity_selected_pack_delete);
        shareSelectedItemsMenuItem.setVisible(false);
        deleteSelectedItemsMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_pack_create_item:
                CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance("");
                dialogFrag.setTargetFragment(this, 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                return true;
            case R.id.menu_activity_pack_search:
               SearchView searchView = (SearchView)item.getActionView();
               searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                 @Override
                 public boolean onQueryTextSubmit(String s) {
                     return false;
                 }

                 @Override
                 public boolean onQueryTextChange(String s) {
                     packListAdapter.getFilter().filter(s);
                     return false;
                 }});
            case R.id.menu_activity_selected_pack_delete:
                    for (String packName : selectedPacks) {
                        dbHelper.deletePackByPackName(packName);
                    }
                    mode = "";
                fillRecyclerView();
            case R.id.menu_activity_selected_pack_share: // TODO SHARING LIST OF PACKS

            default:
                return true;
        }
    }

    private void refactorToolBarForSelection(){

        shareSelectedItemsMenuItem.setVisible(true);
        deleteSelectedItemsMenuItem.setVisible(true);
    }

    private void onRecyclerViewItemClick(String packName) {
        CardsOfPackFragment nextFrag= new CardsOfPackFragment();
        nextFrag.setPackName(packName);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // добавление пакета
        switch(requestCode) {
            case 1:

                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    String packName = data.getStringExtra("packName");
                    if (!dbHelper.doesPackExistByPackName(packName)) {
                        if (packName.trim().length() > 0) {
                            dbHelper.addNewPack(new Pack(packName));
                            fillRecyclerView();
                        }
                        else {
                            Toast.makeText(context, getString(R.string.enter_correct_pack_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(context, String.format(getString(R.string.pack_already_exists), packName), Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.

                }

                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    String packName = data.getStringExtra("packName");
                    if (!dbHelper.doesPackExistByPackName(packName)) {
                        if (packName.trim().length() > 0) {
                            int id = dbHelper.getPackIdByName(packLongClicked);
                            dbHelper.setPackNameById(id, packLongClicked, packName);
                            fillRecyclerView();
                        }
                        else {
                            Toast.makeText(context, getString(R.string.enter_correct_pack_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(context, String.format(getString(R.string.pack_already_exists), packName), Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.

                }
                break;
        }
    }
}
