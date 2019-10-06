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
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.Pack;

import java.util.List;

public class PackListAdapter extends RecyclerView.Adapter<PackListAdapter.ViewHolder> {
    private int countOfPacks;
    private PushLearnDBHelper dbHelper;
    private List<Pack> packList;
    private List<Card> cardList;

    public PackListAdapter(int countOfPacks){
        this.countOfPacks = countOfPacks;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        Context context = parent.getContext();
        dbHelper = new PushLearnDBHelper(context);
        int layoutIDforListItem = R.layout.pack_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

       View view = inflater.inflate(layoutIDforListItem, parent, false);

        ViewHolder packHolder = new ViewHolder(view);

       return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Pack pack = packList.get(i);
        cardList = dbHelper.getCardListByPackName(pack.getPackName());
        holder.pack_name_textView.setText(pack.getPackName());
        if (!cardList.isEmpty()) {
            holder.pack_item_start_quiz_button.setClickable(true);
            holder.pack_item_start_quiz_button.setImageResource(R.drawable.ic_play_arrow_green_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return countOfPacks;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pack_name_textView;
        ImageButton pack_item_start_quiz_button;

        public ViewHolder(View itemView) {
            super(itemView);
            pack_name_textView = itemView.findViewById(R.id.pack_name_text_view);
            pack_item_start_quiz_button = itemView.findViewById(R.id.pack_item_start_quiz_button);
        }

    }

    public void setPackList(List<Pack> packList) {
        this.packList = packList;
        notifyDataSetChanged();
    }
}
