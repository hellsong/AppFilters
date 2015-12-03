package com.hellsong.appfilters.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hellsong.appfilters.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiruyou on 2015/12/2.
 */
public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ItemHolder> {

    private List<String> mDataList = new ArrayList<>();
    private int mSelectedItemPos;
    private View.OnClickListener mItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            notifyItemChanged(mSelectedItemPos);
            mSelectedItemPos = pos;
            TextView tv = (TextView) v;
            tv.setTextColor(v.getResources().getColor(R.color.tab_item_text_selected_color));
        }
    };

    @Override
    public TabAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_list_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TabAdapter.ItemHolder holder, int position) {
        holder.bindData(mDataList.get(position), position, mItemOnClickListener, mSelectedItemPos == position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setData(List<String> list) {
        mDataList.clear();
        mDataList.addAll(list);
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;

        public ItemHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.tab_title);
        }

        public void bindData(String title, int position, View.OnClickListener mItemOnClickListener, boolean isSelected) {
            mTitleView.setText(title);
            if (position == 0) {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) mTitleView.getLayoutParams();
                lp.leftMargin = (int) mTitleView.getResources().getDimension(R.dimen.main_activity_card_margin);
            } else {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) mTitleView.getLayoutParams();
                lp.leftMargin = (int) mTitleView.getResources().getDimension(R.dimen.tab_list_item_margin_left);
            }
            if (isSelected) {
                mTitleView.setTextColor(mTitleView.getResources().getColor(R.color.tab_item_text_selected_color));
            } else {
                mTitleView.setTextColor(mTitleView.getResources().getColor(R.color.tab_item_text_normal_color));
            }
            mTitleView.setTag(position);
            mTitleView.setOnClickListener(mItemOnClickListener);
        }
    }
}
