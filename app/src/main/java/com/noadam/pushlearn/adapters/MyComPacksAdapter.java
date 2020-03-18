package com.noadam.pushlearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.activities.LearnPackActivity;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Pack;

import java.util.ArrayList;
import java.util.List;


public class MyComPacksAdapter extends RecyclerView.Adapter<MyComPacksAdapter.ViewHolder> {

    private PushLearnDBHelper dbHelper;
    private List<ComPack> comPackList;
    private OnRecyclerViewItemClickListener mClickListener;
    private OnRecyclerViewItemLongClickListener mLongClickListener;
    private Context context;

    public interface OnRecyclerViewItemClickListener {
        void onClick(ComPack myComPack, View v);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onLongClick(ComPack myComPack, View v);
    }

    public MyComPacksAdapter(OnRecyclerViewItemClickListener clickListener, OnRecyclerViewItemLongClickListener onLongClickListner) {
        mClickListener = clickListener;
        mLongClickListener = onLongClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (dbHelper == null) {
            dbHelper = new PushLearnDBHelper(context);
        }
        View view = inflater.inflate(R.layout.pack_of_user_item, parent, false);
        ViewHolder packHolder = new ViewHolder(view);
        return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ComPack pack = comPackList.get(i);
        String packName = pack.getComPackName();
        int myComPackRating = pack.getComPackRating();
        holder.myComPackName_textView.setText(packName);
        holder.myComPackRating_textView.setText(String.valueOf(myComPackRating));
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ComPack pack = comPackList.get(getAdapterPosition());
                    if (mLongClickListener != null) {
                        mLongClickListener.onLongClick(pack, v);
                    }
                    return false;
                }
            });
        }
    }

    public void setComPackList(List<ComPack> comPackList) {
        this.comPackList = comPackList;
        notifyDataSetChanged();
    }
}
