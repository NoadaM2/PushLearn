package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
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
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;
import com.noadam.pushlearn.fragments.dialog.SetIterationTimesDialogFragment;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

import java.util.ArrayList;
import java.util.List;


public class MyPacksFragment extends Fragment{

    private RecyclerView packListRecyclerView;
    private PackListAdapter packListAdapter;
    private PushLearnDBHelper dbHelper;
    private Context context;
    private TextView textViewNoPacks;
    private String packLongClicked;
    private ArrayList<String> selectedPacks = new ArrayList<>();
    private String mode;
    private View longPressedView;
    private MenuItem deleteSelectedItemsMenuItem;
    private MenuItem createPackMenuItem;
    private MenuItem searchPackMenuItem;

    final int MENU_SELECT = 1;
    final int MENU_SHARE = 2;
    final int MENU_EDIT = 3;
    final int MENU_DELETE = 4;
    final int MENU_LEARN = 5;

    private void fillRecyclerView() {
        List<Pack> packList = dbHelper.getPackList();
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
                    if(selectedPacks.isEmpty()) {
                    mode = "";
                    refactorToolBarForSelection(false);
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
        menu.add(0, MENU_SHARE, 2, R.string.share_to_community);
        menu.add(0, MENU_EDIT, 3, R.string.rename);
        menu.add(0, MENU_DELETE, 4, R.string.delete);
        menu.add(0, MENU_LEARN, 5, R.string.learn);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SELECT:
                selectedPacks.add(packLongClicked);
                mode = "selection";
                refactorToolBarForSelection(true);
                longPressedView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                break;
            case MENU_SHARE: // TODO SHARING PACKS
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String email = prefs.getString("login", "");
                String password = prefs.getString("password", "");
                TryToSignInToSharePack(email, password);
                break;
            case MENU_EDIT:
                CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance(packLongClicked);
                dialogFrag.setTargetFragment(this, 2);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                break;
            case MENU_DELETE:
                DeleteConfirmationDialogFragment dialogFragDelete = new DeleteConfirmationDialogFragment();
                dialogFragDelete.setTargetFragment(this, 51);
                dialogFragDelete.show(getFragmentManager().beginTransaction(), "packName");
                break;
            case MENU_LEARN:
                SetIterationTimesDialogFragment dialogFragIterationTimes = new SetIterationTimesDialogFragment().newInstance(5);
                dialogFragIterationTimes.setTargetFragment(this, 99);
                dialogFragIterationTimes.show(getFragmentManager().beginTransaction(), "");
                break;
        }
        return super.onContextItemSelected(item);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = container.getContext();
        dbHelper = new PushLearnDBHelper(context);
        View view = inflater.inflate(R.layout.frag_my_packs, null);
        Toolbar toolbar = view.findViewById(R.id.my_packs_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        textViewNoPacks = view.findViewById(R.id.no_items_textview);
        packListRecyclerView = view.findViewById(R.id.pack_list_recyclerview);
        registerForContextMenu(packListRecyclerView);
        fillRecyclerView();
        return view;
    }
    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_for_recycler_view, menu);
        createPackMenuItem = menu.findItem(R.id.menu_activity_create_item);
        searchPackMenuItem = menu.findItem(R.id.menu_activity_search);
        deleteSelectedItemsMenuItem = menu.findItem(R.id.menu_activity_selected_items_delete);
        refactorToolBarForSelection(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_create_item:
                CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance("");
                dialogFrag.setTargetFragment(this, 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                return true;
            case R.id.menu_activity_search:
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
                return true;
            case R.id.menu_activity_selected_items_delete:
                DeleteConfirmationDialogFragment dialogFragDelete = new DeleteConfirmationDialogFragment();
                dialogFragDelete.setTargetFragment(this, 52);
                dialogFragDelete.show(getFragmentManager().beginTransaction(), "packName");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refactorToolBarForSelection(boolean mode){
        createPackMenuItem.setVisible(!mode);
        searchPackMenuItem.setVisible(!mode);
        deleteSelectedItemsMenuItem.setVisible(mode);
    }

    private void onRecyclerViewItemClick(String packName) {
        CardsOfPackFragment nextFrag = new CardsOfPackFragment();
        nextFrag.setPackName(packName);
        getActivity().getFragmentManager().beginTransaction()
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
            case 51:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    if (dbHelper.getPackTypeByName(packLongClicked).equals("downloaded")) {
                        if (packLongClicked != "") {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            String email = prefs.getString("login", "");
                            String password = prefs.getString("password", "");
                            TryToSignInToUnStar(email, password);
                        }
                    } else {
                        dbHelper.deletePackByPackName(packLongClicked);
                        fillRecyclerView();
                    }
                }
                break;
            case 52:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    for (String packName : selectedPacks) {
                        dbHelper.deletePackByPackName(packName);
                    }
                    mode = "";
                    refactorToolBarForSelection(false);
                    fillRecyclerView();
                    selectedPacks.clear();
                }
                break;
            case 99: // Learn Pack
                if (resultCode == Activity.RESULT_OK) {
                    dbHelper.setCardsOfPackIterationTimes(packLongClicked, data.getIntExtra("iteration_times", 5));
                }
                fillRecyclerView();
                break;
        }
    }
    private void unStarPackResponse(int id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendUnStarPackByHashAndPackIDResponse(id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String answer) {
                if(answer.equals("ok")) {
                    dbHelper.deletePackByPackName(packLongClicked);
                    fillRecyclerView();
                }
            }
            @Override
            public void onError() {
            }
        });
    }

    private void TryToSignInToUnStar(String email, String password) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendSignInResponse(email, password, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String hash) {
                if (hash.contains("sign_in")) {
                    Toast.makeText(context, getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show();
                }  else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("account_hash", hash);
                    editor.apply();
                    unStarPackResponse(dbHelper.getPackComIDByName(packLongClicked), hash);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void TryToSignInToSharePack(String email, String password) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendSignInResponse(email, password, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String hash) {
                if (hash.contains("sign_in")) {
                    Toast.makeText(context, getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("account_hash", hash);
                    editor.apply();
                    CreatePackFragment fragment = new CreatePackFragment();
                    fragment.setBasePack(packLongClicked);
                    loadFragment(fragment);
                }
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
}
