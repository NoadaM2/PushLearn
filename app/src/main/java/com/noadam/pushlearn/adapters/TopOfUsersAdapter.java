package com.noadam.pushlearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.entities.User;

import java.util.ArrayList;

public class TopOfUsersAdapter extends RecyclerView.Adapter<TopOfUsersAdapter.ViewHolder>{

    private ArrayList<User> usersList = new ArrayList<>();

    private TopOfUsersAdapter.OnRecyclerViewItemClickListener mClickListener;

    public TopOfUsersAdapter(TopOfUsersAdapter.OnRecyclerViewItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(User user, View v);
    }

    @Override
    public TopOfUsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_in_top_item, parent, false);
        TopOfUsersAdapter.ViewHolder pvh = new TopOfUsersAdapter.ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final TopOfUsersAdapter.ViewHolder holder, final int position) {
        holder.nickname_textView.setText(usersList.get(position).getNickname());
        holder.rating_textView.setText(String.valueOf(usersList.get(position).getRating()));
        if(position < 3) {
            switch (position) {
                case 0:
                    holder.position_imageView.setImageResource(R.drawable.ic_crown_gold);
                    break;
                case 1:
                    holder.position_imageView.setImageResource(R.drawable.ic_crown_silver);
                    break;
                case 2:
                    holder.position_imageView.setImageResource(R.drawable.ic_crown_bronze);
                    break;
            }
        } else {
            holder.position_textView.setText(String.valueOf(position + 1) + ".");
            holder.position_imageView.setImageResource(R.drawable.ic_crown_white);
            holder.position_imageView.setVisibility(View.INVISIBLE);
        }
        switch (usersList.get(position).getLanguage_id()) {
            case 0:
                break;
            case 1:
                holder.flag_imageView.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case 11:
                holder.flag_imageView.setImageResource(R.drawable.ic_united_states);
                break;
            case 2:
                holder.flag_imageView.setImageResource(R.drawable.ic_flag_russia);
                break;

        }
    }

    public void setUsersList(ArrayList<User> users) {
        this.usersList = users;
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nickname_textView;
        TextView rating_textView;
        ImageView flag_imageView;
        TextView position_textView;
        ImageView position_imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nickname_textView = itemView.findViewById(R.id.topUserName_text_view);
            rating_textView = itemView.findViewById(R.id.topUserRating_number_text_view);
            flag_imageView = itemView.findViewById(R.id.flag_of_topUser_imageView);
            position_textView = itemView.findViewById(R.id.topUserPosition_text_view);
            position_imageView = itemView.findViewById(R.id.topUserPosition_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = usersList.get(getAdapterPosition());
                    if (mClickListener != null) {
                        mClickListener.onClick(user, v);
                    }
                }
            });
        }
    }
}
