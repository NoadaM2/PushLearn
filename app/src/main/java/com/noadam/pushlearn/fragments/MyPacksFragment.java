package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.PackListAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Pack;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MyPacksFragment extends Fragment {

    private RecyclerView pack_list_recyclerView;
    private PackListAdapter packListAdapter;
    private PushLearnDBHelper dbHelper;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_packs, null);
        Context context = container.getContext();
        dbHelper = new PushLearnDBHelper(context);

        pack_list_recyclerView = view.findViewById(R.id.pack_list_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pack_list_recyclerView.setLayoutManager(layoutManager);
        List<Pack> packList = dbHelper.getPackList();
        packListAdapter = new PackListAdapter(packList.size());
        packListAdapter.setPackList(packList);
        pack_list_recyclerView.setAdapter(packListAdapter);
        pack_list_recyclerView.getAdapter().notifyDataSetChanged();

        return view;
    }


}
