package com.noadam.pushlearn.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.PackListAdapter;


public class MyPacksFragment extends Fragment {

    private RecyclerView pack_list_recyclerView;
    private PackListAdapter packListAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_packs, null);
        pack_list_recyclerView = view.findViewById(R.id.pack_list_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pack_list_recyclerView.setLayoutManager(layoutManager);

        pack_list_recyclerView.setHasFixedSize(true);

        packListAdapter = new PackListAdapter(100);
        pack_list_recyclerView.setAdapter(packListAdapter);

        return view;
    }
}
