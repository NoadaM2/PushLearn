package com.noadam.pushlearn.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.noadam.pushlearn.R;
import com.noadam.pushlearn.entities.ComPack;

import java.util.ArrayList;


public class ComPacksAdapter extends RecyclerView.Adapter<ComPacksAdapter.ViewHolder>{

    private ArrayList<ComPack> comPackList = new ArrayList<>();
    private Context context;
    private ComPacksAdapter.OnRecyclerViewItemClickListener mClickListener;

    public ComPacksAdapter(Context context, ComPacksAdapter.OnRecyclerViewItemClickListener clickListener) {
        mClickListener = clickListener;
        this.context = context;
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(ComPack myComPack, View v);
    }

    public void addItem(ArrayList<ComPack> comPacks) {
        comPackList.addAll(comPacks);
    }

    public void clearItems() {
        comPackList.clear();
    }

    @Override
    public ComPacksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pack_of_user_item, parent, false);
        ComPacksAdapter.ViewHolder pvh = new ComPacksAdapter.ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ComPacksAdapter.ViewHolder holder, final int position) {
        holder.myComPackName_textView.setText(comPackList.get(position).getComPackName());
        holder.myComPackRating_textView.setText(String.valueOf(comPackList.get(position).getComPackRating()));
    }

    @Override
    public int getItemCount() {
        return comPackList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView myComPackName_textView;
        TextView myComPackRating_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            myComPackName_textView = itemView.findViewById(R.id.myComPackName_text_view);
            myComPackRating_textView = itemView.findViewById(R.id.myComPackRating_number_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComPack pack = comPackList.get(getAdapterPosition());
                    if (mClickListener != null) {
                        mClickListener.onClick(pack, v);
                    }
                }
            });
        }
    }
}
