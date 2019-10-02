package com.noadam.pushlearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.noadam.pushlearn.R;

public class PackListAdapter extends RecyclerView.Adapter<PackListAdapter.PackListHolder> {

    private int iterator;
    private int countOfElements;

    public PackListAdapter(int i){
        countOfElements = i;
        iterator = 0;
    }
    @NonNull
    @Override
    public PackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        Context context = parent.getContext();
        int layoutIDforListItem = R.layout.pack_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

       View view = inflater.inflate(layoutIDforListItem, parent, false);

       PackListHolder packHolder = new PackListHolder(view);
       packHolder.question_textView.setText("question"+String.valueOf(iterator));
        packHolder.answer_textView.setText("answer"+String.valueOf(iterator));
       iterator++;

       return packHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackListHolder holder, int i) {
        holder.bind(i);
    }

    @Override
    public int getItemCount() {
        return countOfElements;
    }

    class PackListHolder extends RecyclerView.ViewHolder {

        TextView question_textView;
        TextView answer_textView;

        public PackListHolder(View itemView) {
            super(itemView);

            question_textView = itemView.findViewById(R.id.question_text_view);
            answer_textView = itemView.findViewById(R.id.answer_text_view);
        }

        void bind(int iterator){
            question_textView.setText("question_index"+String.valueOf(iterator));
            answer_textView.setText("answer"+String.valueOf(iterator));
        }
    }
}
