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
import android.widget.ImageView;
import android.widget.TextView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.entities.Card;

import java.util.ArrayList;
import java.util.List;

public class CardsOfNowLearningAdapter extends RecyclerView.Adapter<CardsOfNowLearningAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Card> cardList;
    private List<Card> cardFullList;
    private int layoutIDforListItem;
    private CardsOfNowLearningAdapter.OnRecyclerViewItemClickListener mClickListener;
    private CardsOfNowLearningAdapter.OnRecyclerViewItemLongClickListener mLongClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onClick(Card card, View v);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onLongClick(Card card, View v);
    }

    public CardsOfNowLearningAdapter(CardsOfNowLearningAdapter.OnRecyclerViewItemClickListener clickListener, CardsOfNowLearningAdapter.OnRecyclerViewItemLongClickListener onLongClickListner) {
        mClickListener = clickListener;
        mLongClickListener = onLongClickListner;
        layoutIDforListItem = R.layout.card_of_now_learning_item;
    }

    @NonNull
    @Override
    public CardsOfNowLearningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIDforListItem, parent, false);

        CardsOfNowLearningAdapter.ViewHolder packHolder = new CardsOfNowLearningAdapter.ViewHolder(view);

        return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardsOfNowLearningAdapter.ViewHolder holder, int i) {
        Card card = cardList.get(i);
        String packIs = context.getString(R.string.pack_is)+" "+ card.getPackName();
        holder.packName_textView.setText(packIs);
        holder.question_textView.setText(card.getQuestion());
        holder.answer_textView.setText(card.getAnswer());
        int iteratingTimes = card.getIteratingTimes();
        holder.iterating_times_textView.setText(Integer.toString(iteratingTimes));
        if (iteratingTimes > 4) {
            holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.lime));
            holder.notification_imageView.setColorFilter(ContextCompat.getColor(context, R.color.lime));
        }
        else {
            switch (iteratingTimes) {
                case (4):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.yellowGreen));
                    holder.notification_imageView.setColorFilter(ContextCompat.getColor(context, R.color.yellowGreen));
                    break;
                case (3):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                    holder.notification_imageView.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
                    break;
                case (2):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                    holder.notification_imageView.setColorFilter(ContextCompat.getColor(context, R.color.orange));
                    break;
                case (1):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.red));
                    holder.notification_imageView.setColorFilter(ContextCompat.getColor(context, R.color.red));
                    break;
                case (0):
                    holder.iterating_times_textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                    holder.notification_imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
                    break;
            }
        }




    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView packName_textView;
        TextView question_textView;
        TextView answer_textView;
        TextView iterating_times_textView;
        ImageView notification_imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            packName_textView = itemView.findViewById(R.id.pack_name_text_view);
            question_textView = itemView.findViewById(R.id.question_text_view);
            answer_textView = itemView.findViewById(R.id.answer_text_view);
            iterating_times_textView = itemView.findViewById(R.id.rating_number_text_view);
            notification_imageView = itemView.findViewById(R.id.star_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Card card = cardList.get(getAdapterPosition());
                    if (mClickListener != null) {
                        mClickListener.onClick(card, v);
                    }
                }
            });
           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Card card = cardList.get(getAdapterPosition());
                   // String packName = card.getPackName();
                    if (mLongClickListener != null) {
                        mLongClickListener.onLongClick(card, v);
                    }
                    return false;
                }
            });*/
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
