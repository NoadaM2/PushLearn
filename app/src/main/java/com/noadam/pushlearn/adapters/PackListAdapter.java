package com.noadam.pushlearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.Pack;

import java.util.ArrayList;
import java.util.List;

public class PackListAdapter extends RecyclerView.Adapter<PackListAdapter.ViewHolder> implements Filterable {

    private PushLearnDBHelper dbHelper;
    private List<Pack> packList;
    private List<Pack> packFullList;
    private int layoutIDforListItem;
    private OnRecyclerViewItemClickListener mClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onClick(String packName);
    }


    public PackListAdapter(OnRecyclerViewItemClickListener clickListener) {
        mClickListener = clickListener;
        layoutIDforListItem = R.layout.pack_list_item;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (dbHelper == null) {
            dbHelper = new PushLearnDBHelper(context);
        }
       View view = inflater.inflate(layoutIDforListItem, parent, false);

        ViewHolder packHolder = new ViewHolder(view);

       return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            Pack pack = packList.get(i);
            List<Card> cardList = dbHelper.getCardListByPackName(pack.getPackName());
            holder.pack_name_textView.setText(pack.getPackName());
            if (!cardList.isEmpty()) {
                holder.pack_item_start_quiz_button.setClickable(true);
                holder.pack_item_start_quiz_button.setImageResource(R.drawable.ic_play_arrow_green_48dp);
            }
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pack_name_textView;
        ImageButton pack_item_start_quiz_button;

        public ViewHolder(View itemView) {
            super(itemView);
            pack_name_textView = itemView.findViewById(R.id.pack_name_text_view);
            pack_item_start_quiz_button = itemView.findViewById(R.id.pack_item_start_quiz_button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pack pack = packList.get(getAdapterPosition());
                    String packName = pack.getPackName();
                   // ArrayList<Card> cardList = dbHelper.getCardListByPackName(pack.getPackName());
                    //continue
                    if (mClickListener != null) {
                        mClickListener.onClick(packName);
                    }
                }
            });
        }


    }

    public void setPackList(List<Pack> packList) {
        this.packList = packList;
        this.packFullList = new ArrayList<>(packList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private  Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
          List<Pack> filteredList = new ArrayList<>();

          if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(packFullList);
          }
          else {
              String filterPattern = constraint.toString().toLowerCase().trim();

              for (Pack item : packFullList) {
                  if(item.getPackName().toLowerCase().contains(filterPattern)) {
                      filteredList.add(item);
                  }
              }
          }
          FilterResults results = new FilterResults();
          results.values = filteredList;

          return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            packList.clear();
            packList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
