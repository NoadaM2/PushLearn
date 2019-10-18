package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.PackListAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Pack;

import java.util.List;


public class MyPacksFragment extends Fragment {

    private RecyclerView pack_list_recyclerView;
    private PackListAdapter packListAdapter;
    private PushLearnDBHelper dbHelper;
    private Toolbar toolbar;
    private Context context;
    private FragmentActivity myContext;

    private void fillRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pack_list_recyclerView.setLayoutManager(layoutManager);

        List<Pack> packList = dbHelper.getPackList();
        packListAdapter = new PackListAdapter();
        packListAdapter.setPackList(packList);

        pack_list_recyclerView.setAdapter(packListAdapter);
        pack_list_recyclerView.getAdapter().notifyDataSetChanged();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = container.getContext();
        dbHelper = new PushLearnDBHelper(context);

        View view = inflater.inflate(R.layout.frag_my_packs, null);
        toolbar = view.findViewById(R.id.mypacks_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        pack_list_recyclerView = view.findViewById(R.id.pack_list_recyclerview);
        fillRecyclerView();


        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_my_packs, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_pack_create_item:
                CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance(123);
                dialogFrag.setTargetFragment(this, 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "packName");

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:

                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    String packName = data.getStringExtra("packName");;
                    dbHelper.addNewPack(new Pack(packName));
                    fillRecyclerView();
                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.

                }

                break;
        }
    } // добавление пакета
}


