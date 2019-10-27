package com.noadam.pushlearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.Pack;

import java.util.ArrayList;
import java.util.List;

public class CardsOfPackAdapter extends RecyclerView.Adapter<CardsOfPackAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Card> cardList;
    private List<Card> cardFullList;
    private int layoutIDforListItem;
    private CardsOfPackAdapter.OnRecyclerViewItemClickListener mClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onClick();
    }


    public CardsOfPackAdapter(CardsOfPackAdapter.OnRecyclerViewItemClickListener clickListener) {
        mClickListener = clickListener;
        layoutIDforListItem = R.layout.card_of_pack_item;
    }

    @NonNull
    @Override
    public CardsOfPackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIDforListItem, parent, false);

        CardsOfPackAdapter.ViewHolder packHolder = new CardsOfPackAdapter.ViewHolder(view);

        return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardsOfPackAdapter.ViewHolder holder, int i) {
        Card card = cardList.get(i);
        holder.question_textView.setText(card.getQuestion());
        holder.answer_textView.setText(card.getAnswer());
        int iteratingTimes = card.getIteratingTimes();
        holder.iterating_times_textView.setText(Integer.toString(iteratingTimes));
        if (iteratingTimes > 4) {
            holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.lime));
        }
        else {
            switch (iteratingTimes) {
                case (4):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.yellowGreen));
                    break;
                case (3):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                    break;
                case (2):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                    break;
                case (1):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.tomato));
                    break;
                case (0):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.red));
                    break;
            }
        }




    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView question_textView;
        TextView answer_textView;
        TextView iterating_times_textView;


        public ViewHolder(View itemView) {
            super(itemView);
            question_textView = itemView.findViewById(R.id.question_text_view);
            answer_textView = itemView.findViewById(R.id.answer_text_view);
            iterating_times_textView = itemView.findViewById(R.id.iterating_times_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mClickListener != null) {
                        mClickListener.onClick();
                    }
                }
            });
            // itemView.setOnLongClickListener();
        }


    }

    public void setCardList(List<Card> cardList, Context context) {
        this.context = context;
        this.cardList = cardList;
        this.cardFullList = new ArrayList<>(cardList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private  Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Card> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(cardFullList);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Card item : cardFullList) {
                    if(item.getQuestion().toLowerCase().contains(filterPattern)) {
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
            cardList.clear();
            cardList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
