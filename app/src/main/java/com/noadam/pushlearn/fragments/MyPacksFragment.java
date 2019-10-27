package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.List;


public class MyPacksFragment extends Fragment{

    private RecyclerView pack_list_recyclerView;
    private PackListAdapter packListAdapter;
    private PushLearnDBHelper dbHelper;
    private Toolbar toolbar;
    private Context context;
    private TextView textViewNoPacks;
    private List<Pack> packList;
    private String PackLongClicked;

    final int MENU_SELECT = 1;
    final int MENU_SHARE = 2;
    final int MENU_EDIT = 3;
    final int MENU_DELETE = 4;

    private void fillRecyclerView()
    {
        packList = dbHelper.getPackList();
        if (!packList.isEmpty()) {
            textViewNoPacks.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            pack_list_recyclerView.setLayoutManager(layoutManager);
            packListAdapter = new PackListAdapter(new PackListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onClick(String packName) {
                    onRecyclerViewItemClick(packName);
                }
            }, new PackListAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public void onLongClick(String packName) {

                    PackLongClicked = packName;
                }
            });
            packListAdapter.setPackList(packList);

            pack_list_recyclerView.setAdapter(packListAdapter);
            pack_list_recyclerView.getAdapter().notifyDataSetChanged();
        }
        else {
            textViewNoPacks.setText(R.string.no_packs);
            textViewNoPacks.setVisibility(View.VISIBLE);
        }
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

                break;
            case MENU_SHARE:

                break;
            case MENU_EDIT:
                CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance(PackLongClicked);
                dialogFrag.setTargetFragment(this, 2);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                break;
            case MENU_DELETE:
                if ((PackLongClicked != "") && (dbHelper.doesPackExistByPackName(PackLongClicked))) {
                    dbHelper.deletePackByPackName(PackLongClicked);
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
        /*Card card = new Card(1,"1st","question1","answer1",1);
        dbHelper.addNewCard(card);
        card = new Card(2,"1st","question2","answer2",2);
        dbHelper.addNewCard(card);
        card = new Card(3,"1st","question3","answer3",3);
        dbHelper.addNewCard(card);
        card = new Card(2,"1st","question2","answer2",4);
        dbHelper.addNewCard(card);
        card = new Card(3,"1st","question3","answer3",0);
        dbHelper.addNewCard(card);*/
        View view = inflater.inflate(R.layout.frag_my_packs, null);
        toolbar = view.findViewById(R.id.mypacks_toolbar);
        textViewNoPacks = view.findViewById(R.id.no_items_textview);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        pack_list_recyclerView = view.findViewById(R.id.pack_list_recyclerview);
        registerForContextMenu(pack_list_recyclerView);
        fillRecyclerView();


        return view;
    }
    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_my_packs, menu);
        super.onCreateOptionsMenu(menu,inflater);
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
                 }
             }
               );
            default:
                return true;
        }
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
                            int id = dbHelper.getPackIdByName(PackLongClicked);
                            dbHelper.setPackNameById(id, PackLongClicked, packName);
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


