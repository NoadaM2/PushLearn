package com.noadam.pushlearn.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.ComCard;

import java.util.List;

public class CardsOfComPackAdapter extends RecyclerView.Adapter<CardsOfComPackAdapter.ViewHolder> {
    private PushLearnDBHelper dbHelper;
    private List<ComCard> comCardList;
    private CardsOfComPackAdapter.OnRecyclerViewItemClickListener mClickListener;
    private CardsOfComPackAdapter.OnRecyclerViewItemLongClickListener mLongClickListener;
    private Context context;

    public interface OnRecyclerViewItemClickListener {
        void onClick(ComCard myComPack, View v);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onLongClick(ComCard myComPack, View v);
    }

    public CardsOfComPackAdapter(CardsOfComPackAdapter.OnRecyclerViewItemClickListener clickListener, CardsOfComPackAdapter.OnRecyclerViewItemLongClickListener onLongClickListner) {
        mClickListener = clickListener;
        mLongClickListener = onLongClickListner;
    }

    @NonNull
    @Override
    public CardsOfComPackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (dbHelper == null) {
            dbHelper = new PushLearnDBHelper(context);
        }
        View view = inflater.inflate(R.layout.card_of_com_pack_item, parent, false);
        CardsOfComPackAdapter.ViewHolder packHolder = new CardsOfComPackAdapter.ViewHolder(view);
        return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardsOfComPackAdapter.ViewHolder holder, int i) {
        ComCard comCard = comCardList.get(i);
        String question = comCard.getQuestion();
        String answer = comCard.getAnswer();
        holder.question_textView.setText(question);
        holder.answer_textView.setText(String.valueOf(answer));
    }

    @Override
    public int getItemCount() {
        return comCardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView question_textView;
        TextView answer_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            question_textView = itemView.findViewById(R.id.question_text_view);
            answer_textView = itemView.findViewById(R.id.answer_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComCard card = comCardList.get(getAdapterPosition());
                    if (mClickListener != null) {
                        mClickListener.onClick(card, v);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ComCard card = comCardList.get(getAdapterPosition());
                    if (mLongClickListener != null) {
                        mLongClickListener.onLongClick(card, v);
                    }
                    return false;
                }
            });
        }
    }

    public void setComCardList(List<ComCard> comCardList) {
        this.comCardList = comCardList;
        notifyDataSetChanged();
    }
}
